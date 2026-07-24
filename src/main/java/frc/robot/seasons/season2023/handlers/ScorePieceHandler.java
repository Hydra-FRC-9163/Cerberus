package frc.robot.seasons.season2023.handlers;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.adl.core.ActionHandler;
import frc.robot.adl.core.ActionRequest;
import frc.robot.adl.core.RobotContextFacts;
import frc.robot.subsystems.Score.angular.AngularManager;
import frc.robot.subsystems.Score.claw.ClawManager;

public final class ScorePieceHandler implements ActionHandler {

    private final ClawManager claw;
    private final AngularManager angular;

    public ScorePieceHandler(ClawManager claw, AngularManager angular) {
        this.claw = claw;
        this.angular = angular;
    }

    @Override
    public Command createCommand(ActionRequest request, RobotContextFacts context) {
        return Commands.runOnce(() -> {
            angular.AngularStop();
            claw.Outtake();
        }, claw, angular);
    }
}
