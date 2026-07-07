// package frc.robot.seasons.season2026.handlers;

// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.Commands;
// import frc.robot.adl.core.ActionHandler;
// import frc.robot.adl.core.ActionRequest;
// import frc.robot.adl.core.RobotContextFacts;
// import frc.robot.subsystems.Score.Angular.IntakeAngleManager;
// import frc.robot.subsystems.Score.Climb.ClimberManager;
// import frc.robot.subsystems.Score.PreShooter.PreShooterManager;
// import frc.robot.subsystems.Score.Rollers.IntakeRollerManager;
// import frc.robot.subsystems.Score.Shooter.ShooterManager;

// public final class AbortHandler implements ActionHandler {
//     private final IntakeAngleManager intakeAngle;
//     private final IntakeRollerManager intakeRoller;
//     private final ClimberManager climber;
//     private final PreShooterManager preShooter;
//     private final ShooterManager shooter;

//     public AbortHandler(
//             IntakeAngleManager intakeAngle,
//             IntakeRollerManager intakeRoller,
//             ClimberManager climber,
//             PreShooterManager preShooter,
//             ShooterManager shooter
//     ) {
//         this.intakeAngle = intakeAngle;
//         this.intakeRoller = intakeRoller;
//         this.climber = climber;
//         this.preShooter = preShooter;
//         this.shooter = shooter;
//     }

//     @Override
//     public Command createCommand(ActionRequest request, RobotContextFacts context) {
//         return Commands.sequence(
//             Commands.runOnce(() -> intakeRoller.stop(), intakeRoller),
//             Commands.runOnce(() -> preShooter.stop(), preShooter),
//             Commands.runOnce(() -> shooter.stop(), shooter),
//             Commands.runOnce(() -> climber.setStopManualClimb(), climber),
//             Commands.runOnce(() -> intakeAngle.stop(), intakeAngle)
//         );
//     }
// }
