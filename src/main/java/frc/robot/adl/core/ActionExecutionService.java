package frc.robot.adl.core;

import java.util.Optional;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StringPublisher;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public final class ActionExecutionService {
    private final ActionRegistry registry;
    private final StringPublisher activeActionPub;
    private final StringPublisher statePub;
    private Command activeCommand;
    private ActionRequest activeRequest;

    public ActionExecutionService(ActionRegistry registry) {
        this.registry = registry;
        activeActionPub = NetworkTableInstance.getDefault()
            .getStringTopic("/ADL/activeAction")
            .publish();
        statePub = NetworkTableInstance.getDefault()
            .getStringTopic("/ADL/state")
            .publish();
    }

    public boolean execute(ActionRequest request, RobotContextFacts context) {
        if (request == null) {
            throw new IllegalArgumentException("Action request is required");
        }
        Optional<RegisteredAction> registeredAction = registry.find(request.actionId());
        if (registeredAction.isEmpty()) {
            return false;
        }

        if (activeCommand != null && activeCommand.isScheduled()) {
            boolean activeInterruptible = registry.find(activeRequest.actionId())
                .map(action -> action.definition().interruptible())
                .orElse(true);
            if (!activeInterruptible) {
                return false;
            }
            boolean higherPriority = effectivePriority(request) > effectivePriority(activeRequest);
            if (!request.preemptCurrent() && !higherPriority) {
                return false;
            }
            activeCommand.cancel();
        }

        activeCommand = registeredAction.get().handler().createCommand(request, context);
        if (activeCommand == null) {
            throw new IllegalStateException("Handler returned null command for action " + request.actionId());
        }
        if (!registeredAction.get().definition().interruptible()) {
            activeCommand = activeCommand.withInterruptBehavior(Command.InterruptionBehavior.kCancelIncoming);
        }
        activeRequest = request;
        CommandScheduler.getInstance().schedule(activeCommand);
        activeActionPub.set(request.actionId().value());
        statePub.set(toDashboardState(registeredAction.get().definition()));
        return true;
    }

    private int effectivePriority(ActionRequest request) {
        return registry.find(request.actionId())
            .map(action -> {
                int requestedPriority = request.priority();
                return requestedPriority != 0
                    ? requestedPriority
                    : action.definition().defaultPriority();
            })
            .orElse(request.priority());
    }

    private String toDashboardState(ActionDefinition definition) {
        return switch (definition.category()) {
            case "intake" -> "ACQUIRING";
            case "score" -> "SCORING";
            // Charged Up 2023 nao tem climb - dashboard.json (Fase 1) ja usa
            // "BALANCING" para o estado do charge station.
            case "endgame" -> "BALANCING";
            case "safety" -> "EMERGENCY";
            case "movement" -> "MOVING";
            default -> definition.id().value().toUpperCase();
        };
    }
}