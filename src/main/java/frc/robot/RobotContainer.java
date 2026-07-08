package frc.robot;

import java.io.File;
import java.util.function.DoubleSupplier;

import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
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
  private final Joystick logitech;

  private final DefaultDriveCommand defaultDriveCommand;
  private final Drivetrain drivetrain;
  
  private final ClawHardware clawHardware;
  private final ClimbHardware climbHardware;
  private final ConveyorHardware conveyorHardware;
  private final LinearHardware linearHardware;

  private final ClawManager clawManager;
  private final ClimbManager climbManager;
  private final ConveyorManager conveyorManager;
  private final LinearManager linearManager;

  private final RobotStressMonitor stressMonitor;
  private final RobotStressController stressController;
  private final DashboardPublisherStress stressPublisher;
  private final DriveModePublisher modePublisher;

  private final SendableChooser<String> autoChooser;

  private final RobotContextProvider adlContextProvider;
  private final ADLManager legacyAdlManager;
  private final ModularADLManager modularAdlManager;

  private final SequentialCommandGroup autonomousCommand;

  public RobotContainer() {

    controller          = new CommandPS5Controller(Constants.PS5_ID);
    logitech            = new Joystick(Constants.LOGITECH_ID);

    drivetrain          = new Drivetrain();
    defaultDriveCommand = new DefaultDriveCommand( drivetrain, logitech);

    clawHardware        = new ClawHardware();
    climbHardware       = new ClimbHardware();
    conveyorHardware    = new ConveyorHardware();
    linearHardware      = new LinearHardware();

    clawManager         = new ClawManager(clawHardware);
    climbManager        = new ClimbManager();
    conveyorManager     = new ConveyorManager(conveyorHardware);
    linearManager       = new LinearManager(linearHardware);

    stressMonitor       = new RobotStressMonitor();
    stressController    = new RobotStressController();
    stressPublisher     = new DashboardPublisherStress();
    modePublisher       = new DriveModePublisher();

    autonomousCommand   = new SequentialCommandGroup();

    drivetrain.setDefaultCommand(defaultDriveCommand);
    configureBindings();

    adlContextProvider = new RobotContextProvider();
    // legacyAdlManager = new ADLManager(
    //     new HumanIntentSource(),
    //     adlContextProvider,
    //     new ADLExecutor(
    //         intake,
    //         rollerManager,
    //         climberManager,
    //         preShooterManager,
    //         shooterManager,
    //         spindexerManager
    //     )
    // );

    // modularAdlManager = buildModularAdlManager();
  }

  // private ModularADLManager buildModularAdlManager() {
    // ActionRegistry actionRegistry = new ActionRegistry();
    // SeasonRegistrationContext seasonContext = new SeasonRegistrationContext(actionRegistry);
    // new Season2026Module(
    //     intake,
    //     rollerManager,
    //     climberManager,
    //     preShooterManager,
    //     shooterManager,
    //     spindexerManager
    // ).register(seasonContext);

    // NetworkTablesActionIntentSource intentSource = new NetworkTablesActionIntentSource(
    //     java.util.Map.of(
    //         "ACQUIRE_PIECE", new NetworkTablesActionIntentSource.LegacyIntentMapping(
    //             Season2026Module.ACQUIRE_PIECE, "bump", 60, false),
    //         "SCORE", new NetworkTablesActionIntentSource.LegacyIntentMapping(
    //             Season2026Module.SCORE_PIECE, "hub", 80, false),
    //         "CLIMB", new NetworkTablesActionIntentSource.LegacyIntentMapping(
    //             Season2026Module.CLIMB, "tower", 100, true),
    //         "ABORT", new NetworkTablesActionIntentSource.LegacyIntentMapping(
    //             Season2026Module.ABORT, "unknown", 1000, true)
    //     )
    // );

    // return new ModularADLManager(
    //     intentSource,
    //     () -> LegacyRobotContextFactsAdapter.from(adlContextProvider.build()),
    //     new ADLDecisionEngine(actionRegistry, seasonContext),
    //     new ActionExecutionService(actionRegistry),
    //     Constants.ADLManager.MIN_DECISION_INTERVAL
    // );
  // }

  private void configureBindings() {}

  public void periodic() {

    // if (Constants.ADLManager.USE_MODULAR_ADL) {
    //   modularAdlManager.periodic();
    //   if (Constants.ADLManager.RUN_LEGACY_ADL_IN_PARALLEL) {
    //     legacyAdlManager.periodic();
    //   }
    // } else {
    //   legacyAdlManager.periodic();
    // }

    // var stressData   = stressMonitor.generateData(drivebase);
    // stressController.update(stressData);

    // stressPublisher.publish(stressData, speedScale, chassisSpeed);

    // modePublisher.publishAim(aimLockBack.isActive() ? 1 : 0);
    // modePublisher.publishAlign(
    //     preShooterManager.getMode() == PreShooterManager.ControlMode.AUTO_DISTANCE ? 1 : 0);
    // modePublisher.publishShooterLime2Plus(shooterManager.isSpinning() ? 1 : 0);
    // modePublisher.publishAimLime4(aimLockFront.isActive() ? 1 : 0);
    // modePublisher.publishAlignPiece(alignWithPiece.isActive() ? 1 : 0);
  }

    public Command getAutonomousCommand() {
      return autonomousCommand;
  }
}
