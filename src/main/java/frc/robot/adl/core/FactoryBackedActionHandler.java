package frc.robot.adl.core;

import edu.wpi.first.wpilibj2.command.Command;

public final class FactoryBackedActionHandler implements ActionHandler {
    private final ActionCommandFactory commandFactory;

    public FactoryBackedActionHandler(ActionCommandFactory commandFactory) {
        if (commandFactory == null) {
            throw new IllegalArgumentException("Action command factory is required");
        }
        this.commandFactory = commandFactory;
    }

    @Override
    public Command createCommand(ActionRequest request, RobotContextFacts context) {
        return commandFactory.create(request);
    }
}
