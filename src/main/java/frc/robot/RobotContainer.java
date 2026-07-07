package frc.robot;

import java.io.File;
import java.util.function.DoubleSupplier;

import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import frc.robot.Dashboards.Drive.DriveModePublisher;
import frc.robot.Dashboards.RobotStress.DashboardPublisherStress;
import frc.robot.Dashboards.RobotStress.RobotStressController;
import frc.robot.Dashboards.RobotStress.RobotStressMonitor;
// import frc.robot.adl.ADLExecutor;
import frc.robot.adl.ADLManager;
import frc.robot.adl.HumanIntentSource;
import frc.robot.adl.RobotContextProvider;
import frc.robot.adl.core.ADLDecisionEngine;
import frc.robot.adl.core.ActionExecutionService;
import frc.robot.adl.core.ActionRegistry;
import frc.robot.adl.core.LegacyRobotContextFactsAdapter;
import frc.robot.adl.core.ModularADLManager;
import frc.robot.adl.core.NetworkTablesActionIntentSource;
import frc.robot.adl.core.SeasonRegistrationContext;
import frc.robot.commands.DefaultDriveCommand;
// import frc.robot.seasons.season2026.Season2026Module;
import frc.robot.subsystems.Drivetrain.Drivetrain;
import frc.robot.subsystems.Score.claw.ClawHardware;
import frc.robot.subsystems.Score.climb.ClimbHardware;
import frc.robot.subsystems.Score.conveyor.ConveyorHardware;
import frc.robot.subsystems.Score.linear.LinearHardware;

import frc.robot.subsystems.Score.claw.ClawManager;
import frc.robot.subsystems.Score.climb.ClimbManager;
import frc.robot.subsystems.Score.conveyor.ConveyorManager;
import frc.robot.subsystems.Score.linear.LinearManager;
import frc.robot.utils.Constants;

@SuppressWarnings("unused")
public class RobotContainer {

  private final CommandPS5Controller controller;
  private final CommandPS5Controller logitech;

  private final DoubleSupplier xSupplier;
  private final DoubleSupplier ySupplier;

  private final DefaultDriveCommand drivetrain;
  
  private final ClawHardware clawHardware;
  private final ClimbHardware climbHardware;
  private final ConveyorHardware conveyorHardware;
  private final LinearHardware linearHardware;

  private final ClawHardware clawManager;
  private final ClimbHardware climbManager;
  private final ConveyorHardware conveyorManager;
  private final LinearHardware linearManager;

  private final RobotStressMonitor stressMonitor;
  private final RobotStressController stressController;
  private final DashboardPublisherStress stressPublisher;
  private final DriveModePublisher modePublisher;

  private final SendableChooser<String> autoChooser;

  private final RobotContextProvider adlContextProvider;
  private final ADLManager legacyAdlManager;
  private final ModularADLManager modularAdlManager;

  public RobotContainer() {

    controller = new CommandPS5Controller(Constants.PS5_ID);
    logitech   = new CommandPS5Controller(Constants.LOGITECH_ID);

    xSupplier = () -> controller.getLeftX();
    ySupplier = () -> controller.getLeftY();

    drivetrain        = new DefaultDriveCommand(drivetrain,joystick);
    clawHardware      = new ClawHardware();
    climbHardware     = new ClimbHardware();
    conveyorHardware  = new ConveyorHardware();
    linearHardware    = new LinearHardware();

    clawManager      = new ClawManager();
    climbManager     = new ClimbManager();
    conveyorManager  = new ConveyorManager();
    linearManager    = new LinearManager();

    stressMonitor    = new RobotStressMonitor();
    stressController = new RobotStressController();
    stressPublisher  = new DashboardPublisherStress();
    modePublisher    = new DriveModePublisher();

    configureBindings();

    adlContextProvider = new RobotContextProvider();
    legacyAdlManager = new ADLManager(
        new HumanIntentSource(),
        adlContextProvider,
        new ADLExecutor(
            intake,
            rollerManager,
            climberManager,
            preShooterManager,
            shooterManager,
            spindexerManager
        )
    );

    modularAdlManager = buildModularAdlManager();
  }

  private ModularADLManager buildModularAdlManager() {
    ActionRegistry actionRegistry = new ActionRegistry();
    SeasonRegistrationContext seasonContext = new SeasonRegistrationContext(actionRegistry);
    new Season2026Module(
        intake,
        rollerManager,
        climberManager,
        preShooterManager,
        shooterManager,
        spindexerManager
    ).register(seasonContext);

    NetworkTablesActionIntentSource intentSource = new NetworkTablesActionIntentSource(
        java.util.Map.of(
            "ACQUIRE_PIECE", new NetworkTablesActionIntentSource.LegacyIntentMapping(
                Season2026Module.ACQUIRE_PIECE, "bump", 60, false),
            "SCORE", new NetworkTablesActionIntentSource.LegacyIntentMapping(
                Season2026Module.SCORE_PIECE, "hub", 80, false),
            "CLIMB", new NetworkTablesActionIntentSource.LegacyIntentMapping(
                Season2026Module.CLIMB, "tower", 100, true),
            "ABORT", new NetworkTablesActionIntentSource.LegacyIntentMapping(
                Season2026Module.ABORT, "unknown", 1000, true)
        )
    );

    return new ModularADLManager(
        intentSource,
        () -> LegacyRobotContextFactsAdapter.from(adlContextProvider.build()),
        new ADLDecisionEngine(actionRegistry, seasonContext),
        new ActionExecutionService(actionRegistry),
        Constants.ADLManager.MIN_DECISION_INTERVAL
    );
  }

  private void configureBindings() {

    drivebase.setDefaultCommand(
        new DriveCommand(
            drivebase,
            () -> ySupplier.getAsDouble(),
            () -> xSupplier.getAsDouble(),
            () -> controller.getRightX()
        )
    );

    // --- Main controller (PS5) ---

    controller.L1().onTrue(new InstantCommand(() -> intake.setManualOutput(0.3),  intake))
                   .onFalse(new InstantCommand(() -> intake.stop(), intake));

    controller.R1().onTrue(new InstantCommand(() -> intake.setManualOutput(-0.3), intake))
                   .onFalse(new InstantCommand(() -> intake.stop(), intake));

    // Intake angle - L2/R2 on main controller (no conflict now)
    // new Trigger(() -> controller.getL2Axis() > 0.04)
    //     .onTrue(new InstantCommand(() -> intake.setManual(), intake))
    //     .whileTrue(new RunCommand(() -> intake.setManualOutput(0.3), intake))
    //     .onFalse(new InstantCommand(() -> intake.stop(), intake));

    // new Trigger(() -> controller.getR2Axis() > 0.04)
    //     .onTrue(new InstantCommand(() -> intake.setManual(), intake))
    //     .whileTrue(new RunCommand(() -> intake.setManualOutput(-0.3), intake))
    //     .onFalse(new InstantCommand(() -> intake.stop(), intake));

    controller.povUp().onTrue(new InstantCommand(() -> spindexerManager.toggleSpin(),          spindexerManager));
    controller.povUp().onTrue(new InstantCommand(() -> preShooterManager.toggleManualFeed(),    preShooterManager));
    controller.povRight().onTrue(new InstantCommand(() -> preShooterManager.toggleReverseFeed(),  preShooterManager));
    controller.povUp().onTrue(new InstantCommand(  () -> shooterManager.toggleShooter(),          shooterManager));

    // --- Logitech controller - Climber (no axis conflict) ---

    controller.R2()
        .onTrue(new InstantCommand(() -> climberManager.setManual(), climberManager))
        .whileTrue(new RunCommand(() -> climberManager.setClimbManual(-0.3), climberManager))
        .onFalse(new InstantCommand(() -> climberManager.setStopManualClimb()));

    controller.L2() 
        .onTrue(new InstantCommand(() -> climberManager.setManual(), climberManager))
        .whileTrue(new RunCommand(() -> climberManager.setClimbManual(0.3), climberManager))
        .onFalse(new InstantCommand(() -> climberManager.setStopManualClimb()));

    // --- Vision commands ---
    // Triangle: aim lock front camera (tower alignment)
    controller.triangle().whileTrue(aimLockFront);

    // Cross: aim lock back camera (hub alignment)
    controller.cross().whileTrue(aimLockBack);

    // Square: align with game piece using AI pipeline
    controller.square().whileTrue(alignWithPiece);
  }

  public Command getAutonomousCommand() {
    String selected = autoChooser.getSelected();
    if (selected == null || selected.isEmpty()) return Commands.none();
    return buildAutoCommand(selected);
  }

  /** Wraps a PathPlanner auto name with the standard shooter/spindexer sequence. */
  private Command buildAutoCommand(String autoName) {
    return Commands.sequence(
        Commands.runOnce(() -> shooterManager.start()),
        new PathPlannerAuto(autoName),
        Commands.runOnce(() -> preShooterManager.enableAuto()),
        Commands.runOnce(() -> spindexerManager.start()),
        Commands.waitSeconds(3.0),
        Commands.runOnce(() -> shooterManager.stop()),
        Commands.runOnce(() -> preShooterManager.stop()),
        Commands.runOnce(() -> spindexerManager.stop())
    );
  }


  public void periodic() {

    if (Constants.ADLManager.USE_MODULAR_ADL) {
      modularAdlManager.periodic();
      if (Constants.ADLManager.RUN_LEGACY_ADL_IN_PARALLEL) {
        legacyAdlManager.periodic();
      }
    } else {
      legacyAdlManager.periodic();
    }

    var stressData   = stressMonitor.generateData(drivebase);
    stressController.update(stressData);

    double speedScale   = stressController.getSpeedScale();
    double chassisSpeed = drivebase.getRobotVelocity().vxMetersPerSecond;

    stressPublisher.publish(stressData, speedScale, chassisSpeed);

    modePublisher.publishAim(aimLockBack.isActive() ? 1 : 0);
    modePublisher.publishAlign(
        preShooterManager.getMode() == PreShooterManager.ControlMode.AUTO_DISTANCE ? 1 : 0);
    modePublisher.publishShooterLime2Plus(shooterManager.isSpinning() ? 1 : 0);
    modePublisher.publishAimLime4(aimLockFront.isActive() ? 1 : 0);
    modePublisher.publishAlignPiece(alignWithPiece.isActive() ? 1 : 0);
  }

  public SwerveSubsystem getSwerveSubsystem() { return drivebase; }

  public void refreshTagSelection() {
    vision.selectAllHubTags();
    vision.selectAllTowerTags();
  }

  public void setMotorBrake(boolean brake) {
    drivebase.setMotorBrake(brake);
  }
}
