package frc.robot.adl;

import frc.robot.subsystems.Score.angular.AngularManager;
import frc.robot.subsystems.Score.claw.ClawManager;

@Deprecated(since = "2026", forRemoval = false)
public class ADLExecutor {

    private final ClawManager claw;
    private final AngularManager angular;

    public ADLExecutor(
            ClawManager claw,
            AngularManager angular
    ) {
        this.claw = claw;
        this.angular = angular;
    }

    public void execute(ADLState state) {
        switch (state) {

        case EMERGENCY:
        claw.stopClawMotor();
        angular.AngularStop();
        break;
        
        default:
        break;
        }
    }
}
