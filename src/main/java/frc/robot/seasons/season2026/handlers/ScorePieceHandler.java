package frc.robot.seasons.season2026.handlers;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.adl.core.ActionHandler;
import frc.robot.adl.core.ActionRequest;
import frc.robot.adl.core.RobotContextFacts;
import frc.robot.subsystems.Score.PreShooter.PreShooterManager;
import frc.robot.subsystems.Score.Rollers.IntakeRollerManager;
import frc.robot.subsystems.Score.Shooter.ShooterManager;
import frc.robot.subsystems.Score.Spindexer.SpindexerManager;

public final class ScorePieceHandler implements ActionHandler {
    private final IntakeRollerManager intakeRoller;
    private final PreShooterManager preShooter;
    private final ShooterManager shooter;
    private final SpindexerManager spindexer;

    public ScorePieceHandler(
            IntakeRollerManager intakeRoller,
            PreShooterManager preShooter,
            ShooterManager shooter,
            SpindexerManager spindexer
    ) {
        this.intakeRoller = intakeRoller;
        this.preShooter = preShooter;
        this.shooter = shooter;
        this.spindexer = spindexer;
    }

    @Override
    public Command createCommand(ActionRequest request, RobotContextFacts context) {
        return Commands.sequence(
            Commands.runOnce(() -> intakeRoller.stop(), intakeRoller),
            Commands.runOnce(() -> shooter.start(), shooter),
            Commands.runOnce(() -> spindexer.start(), spindexer),
            Commands.runOnce(() -> preShooter.enableAuto(), preShooter)
        );
    }
}
