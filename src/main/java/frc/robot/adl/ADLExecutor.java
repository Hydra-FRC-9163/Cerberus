package frc.robot.adl;

import frc.robot.subsystems.Score.angular.AngularManager;
import frc.robot.subsystems.Score.claw.ClawManager;
import frc.robot.subsystems.Score.linear.LinearManager;

public class ADLExecutor {

    private final ClawManager claw;
    private final LinearManager linear;
    private final AngularManager angular;

    public ADLExecutor(
            ClawManager claw,
            LinearManager linear,
            AngularManager angular
    ) {
        this.claw = claw;
        this.linear = linear;
        this.angular = angular;
    }

    public void execute(ADLState state) {
        switch (state) {

case EMERGENCY:
    claw.stopClawMotor();
    linear.LinearStop();
    angular.AngularStop();
    break;

default:
    break;
        }
    }
}