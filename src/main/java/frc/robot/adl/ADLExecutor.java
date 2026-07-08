package frc.robot.adl;

import frc.robot.subsystems.Score.claw.ClawManager;
import frc.robot.subsystems.Score.linear.LinearManager;

public class ADLExecutor {

    private final ClawManager claw;
    private final LinearManager linear;

    public ADLExecutor(
            ClawManager claw,
            LinearManager linear
    ) {
        this.claw = claw;
        this.linear = linear;
    }

    public void execute(ADLState state) {
        switch (state) {

case EMERGENCY:
    claw.stopClawMotor();
    linear.StopAll();
    break;

default:
    break;
        }
    }
}