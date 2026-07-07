package frc.robot.adl.core;

import edu.wpi.first.wpilibj2.command.Command;

public interface ActionCommandFactory {
    Command create(ActionRequest request);
}
