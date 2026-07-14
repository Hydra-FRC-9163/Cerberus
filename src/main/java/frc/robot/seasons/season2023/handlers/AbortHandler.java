package frc.robot.seasons.season2023.handlers;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.adl.core.ActionHandler;
import frc.robot.adl.core.ActionRequest;
import frc.robot.adl.core.RobotContextFacts;
import frc.robot.subsystems.Drivetrain.ChargeStationBalancer;
import frc.robot.subsystems.Drivetrain.Drivetrain;
import frc.robot.subsystems.Score.angular.AngularManager;
import frc.robot.subsystems.Score.claw.ClawManager;
import frc.robot.subsystems.Score.linear.LinearManager;

public final class AbortHandler implements ActionHandler {

    private final ClawManager claw;
    private final LinearManager linear;
    private final AngularManager angular;
    private final ChargeStationBalancer chargeStationBalancer;
    private final Drivetrain drivetrain;

    public AbortHandler(
            ClawManager claw,
            LinearManager linear,
            AngularManager angular,
            ChargeStationBalancer chargeStationBalancer,
            Drivetrain drivetrain
    ) {
        this.claw = claw;
        this.linear = linear;
        this.angular = angular;
        this.chargeStationBalancer = chargeStationBalancer;
        this.drivetrain = drivetrain;
    }

    @Override
    public Command createCommand(ActionRequest request, RobotContextFacts context) {
        return Commands.sequence(
            Commands.runOnce(() -> claw.stopClawMotor(), claw),
            Commands.runOnce(() -> linear.LinearStop(), linear),
            Commands.runOnce(() -> angular.AngularStop(), angular),
            Commands.runOnce(chargeStationBalancer::disable),
            Commands.runOnce(() -> drivetrain.stop(), drivetrain)
        );
    }
}