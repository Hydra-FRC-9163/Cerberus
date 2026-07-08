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
import frc.robot.adl.ADLManager;
import frc.robot.adl.HumanIntentSource;
import frc.robot.adl.RobotContextProvider;
import frc.robot.adl.core.ADLDecisionEngine;
import frc.robot.adl.ADLExecutor;
import frc.robot.adl.core.ActionExecutionService;
import frc.robot.adl.core.ActionRegistry;
import frc.robot.adl.core.LegacyRobotContextFactsAdapter;
import frc.robot.adl.core.ModularADLManager;
import frc.robot.adl.core.NetworkTablesActionIntentSource;
import frc.robot.adl.core.SeasonRegistrationContext;
import frc.robot.commands.DefaultDriveCommand;
import frc.robot.seasons.season2023.Season2023Module;
import frc.robot.subsystems.Drivetrain.Drivetrain;
import frc.robot.subsystems.Score.claw.ClawHardware;
import frc.robot.subsystems.Score.linear.LinearHardware;

import frc.robot.subsystems.Score.claw.ClawManager;
import frc.robot.subsystems.Score.linear.LinearManager;
import frc.robot.subsystems.Sensors.ThroughBoreSubsystem;
import frc.robot.utils.Constants;
import frc.robot.utils.Simulation;

@SuppressWarnings("unused")
public class RobotContainer {

  private final CommandPS5Controller controller;
  private final Joystick logitech;

  private final DefaultDriveCommand defaultDriveCommand;
  private final Drivetrain drivetrain;
  
  private final ClawHardware clawHardware;
  private final LinearHardware linearHardware;

  private final ClawManager clawManager;
  private final LinearManager linearManager;

  private final RobotStressMonitor stressMonitor;
  private final RobotStressController stressController;
  private final DashboardPublisherStress stressPublisher;
  private final DriveModePublisher modePublisher;
  private final ThroughBoreSubsystem thou;

  private final RobotContextProvider adlContextProvider;
  private final ADLManager legacyAdlManager;
  private final ModularADLManager modularAdlManager;

  private final SequentialCommandGroup autonomousCommand;
  private Simulation simulation;
  
    public RobotContainer() {
  
      controller          = new CommandPS5Controller(Constants.PS5_ID);
      logitech            = new Joystick(Constants.LOGITECH_ID);
  
      drivetrain          = new Drivetrain();
      defaultDriveCommand = new DefaultDriveCommand( drivetrain, logitech);
  
      clawHardware        = new ClawHardware();
      linearHardware      = new LinearHardware();
  
      clawManager         = new ClawManager(clawHardware);
      linearManager       = new LinearManager(linearHardware);
  
      stressMonitor       = new RobotStressMonitor();
      stressController    = new RobotStressController();
      stressPublisher     = new DashboardPublisherStress();
      modePublisher       = new DriveModePublisher();
  
      autonomousCommand   = new SequentialCommandGroup();
      thou                = new ThroughBoreSubsystem();
      simulation          = new Simulation(thou, drivetrain);

    drivetrain.setDefaultCommand(defaultDriveCommand);
    configureBindings();

    adlContextProvider = new RobotContextProvider();
    legacyAdlManager = new ADLManager(
        new HumanIntentSource(),
        adlContextProvider,
        new ADLExecutor(
            clawManager,
            linearManager
        )
    );

    modularAdlManager = buildModularAdlManager();
  }

  private ModularADLManager buildModularAdlManager() {
    ActionRegistry actionRegistry = new ActionRegistry();
    SeasonRegistrationContext seasonContext = new SeasonRegistrationContext(actionRegistry);
    new Season2023Module(
            clawManager,
            linearManager
    ).register(seasonContext);

    NetworkTablesActionIntentSource intentSource = new NetworkTablesActionIntentSource(
        java.util.Map.of(
            "ACQUIRE_PIECE", new NetworkTablesActionIntentSource.LegacyIntentMapping(
                Season2023Module.ACQUIRE_PIECE, "bump", 60, false),
            "ABORT", new NetworkTablesActionIntentSource.LegacyIntentMapping(
                Season2023Module.ABORT, "unknown", 1000, true)
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

  private void configureBindings() {}

  public void periodic() {

    if (Constants.ADLManager.USE_MODULAR_ADL) {
      modularAdlManager.periodic();
      if (Constants.ADLManager.RUN_LEGACY_ADL_IN_PARALLEL) {
        legacyAdlManager.periodic();
      }
    } else {
      legacyAdlManager.periodic();
    }

    var stressData   = stressMonitor.generateData(drivetrain);
    stressController.update(stressData);

    double driveSpeed = 1;

    stressPublisher.publish(stressData, driveSpeed);
  }

    public Command getAutonomousCommand() {
      return autonomousCommand;
  }
}
