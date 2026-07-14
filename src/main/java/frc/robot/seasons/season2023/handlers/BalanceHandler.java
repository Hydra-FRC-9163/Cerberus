package frc.robot.seasons.season2023.handlers;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.adl.core.ActionHandler;
import frc.robot.adl.core.ActionRequest;
import frc.robot.adl.core.RobotContextFacts;
import frc.robot.subsystems.Drivetrain.ChargeStationBalancer;
import frc.robot.subsystems.Drivetrain.Drivetrain;

public final class BalanceHandler implements ActionHandler {

    private final ChargeStationBalancer chargeStationBalancer;
    private final Drivetrain drivetrain;

    public BalanceHandler(ChargeStationBalancer chargeStationBalancer, Drivetrain drivetrain) {
        this.chargeStationBalancer = chargeStationBalancer;
        this.drivetrain = drivetrain;
    }

    @Override
    public Command createCommand(ActionRequest request, RobotContextFacts context) {
        return new Command() {
            {
                addRequirements(drivetrain);
            }

            @Override
            public void initialize() {
                chargeStationBalancer.enable();
            }

            @Override
            public void end(boolean interrupted) {
                chargeStationBalancer.disable();
            }

            @Override
            public boolean isFinished() {
                return false;
            }
        };
    }
}