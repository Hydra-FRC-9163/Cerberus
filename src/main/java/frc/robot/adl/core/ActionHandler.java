package frc.robot.adl.core;

import edu.wpi.first.wpilibj2.command.Command;

public interface ActionHandler {
    default boolean canHandle(ActionRequest request, RobotContextFacts context) {
        return true;
    }

    Command createCommand(ActionRequest request, RobotContextFacts context);
}
