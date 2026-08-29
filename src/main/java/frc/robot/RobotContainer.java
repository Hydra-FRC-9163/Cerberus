package frc.robot;

import java.util.Map;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import frc.robot.Dashboards.ADLDashboard.ADLController;
import frc.robot.Dashboards.ADLDashboard.ADLMonitor;
import frc.robot.Dashboards.ADLDashboard.DashboardPublisherADL;
import frc.robot.Dashboards.Drive.DriveModePublisher;
import frc.robot.Dashboards.RobotStress.DashboardPublisherStress;
import frc.robot.Dashboards.RobotStress.RobotStressController;
import frc.robot.Dashboards.RobotStress.RobotStressMonitor;
import frc.robot.adl.core.ADLDecisionEngine;
import frc.robot.adl.core.ActionExecutionService;
import frc.robot.adl.core.ActionRegistry;
import frc.robot.adl.core.ModularADLManager;
import frc.robot.adl.core.NetworkTablesActionIntentSource;
import frc.robot.adl.core.NetworkTablesRobotContextFactsProvider;
import frc.robot.adl.core.SeasonRegistrationContext;
import frc.robot.commands.DefaultDriveCommand;
import frc.robot.seasons.season2023.Season2023Module;
import frc.robot.subsystems.Drivetrain.ChargeStationBalancer;
import frc.robot.subsystems.Drivetrain.Drivetrain;
import frc.robot.subsystems.Score.angular.AngularHardware;
import frc.robot.subsystems.Score.angular.AngularManager;
import frc.robot.subsystems.Score.claw.ClawHardware;

import frc.robot.subsystems.Score.claw.ClawManager;
import frc.robot.subsystems.Sensors.ThroughBore.ThroughBoreHardware;
import frc.robot.utils.Constants;
import frc.robot.utils.simulation.AngularArmSim;
import frc.robot.utils.simulation.DrivetrainSim;

@SuppressWarnings("unused")
public class RobotContainer {

  private final CommandPS5Controller controller;
  private final Joystick logitech;

  private final DefaultDriveCommand defaultDriveCommand;
  private final Drivetrain drivetrain;
  private final ChargeStationBalancer chargeStationBalancer;
  
  private final ClawHardware clawHardware;
  private final ClawManager clawManager;

  private final AngularHardware angularHardware;
  private final AngularManager angularManager;

  private final RobotStressMonitor stressMonitor;
  private final RobotStressController stressController;
  private final DashboardPublisherStress stressPublisher;
  private final DriveModePublisher modePublisher;
  private final ADLController adlController;
  private final ADLMonitor adlMonitor;
  private final DashboardPublisherADL adlPublisher;
  private final ModularADLManager modularADLManager;

  private final SequentialCommandGroup autonomousCommand;
  private ThroughBoreHardware throughBore;

  private DrivetrainSim drivetrainSim;
  private AngularArmSim angularSim;

  private AddressableLED m_led;
  private AddressableLEDBuffer m_ledBuffer;

    public RobotContainer() {
  
      controller          = new CommandPS5Controller(Constants.PS5_ID);
      logitech            = new Joystick(Constants.LOGITECH_ID);
  
      drivetrain          = new Drivetrain();
      chargeStationBalancer = new ChargeStationBalancer(drivetrain);
      defaultDriveCommand = new DefaultDriveCommand(drivetrain, controller);
  
      clawHardware        = new ClawHardware();
      clawManager         = new ClawManager(clawHardware);

      angularHardware     = new AngularHardware();
      angularManager      = new AngularManager(angularHardware);
  
      stressMonitor       = new RobotStressMonitor();
      stressController    = new RobotStressController();
      stressPublisher     = new DashboardPublisherStress();
      modePublisher       = new DriveModePublisher();
      adlController       = new ADLController();
      adlMonitor          = new ADLMonitor();
      adlPublisher        = new DashboardPublisherADL();
      throughBore         = new ThroughBoreHardware();
      
      if (RobotBase.isSimulation()) {
        drivetrainSim = new DrivetrainSim(throughBore, drivetrain);
        drivetrain.attachSimulation(drivetrainSim);
        angularSim = new AngularArmSim(clawHardware, angularHardware);
      }

    autonomousCommand   = new SequentialCommandGroup();
    modularADLManager = configureADL();

    drivetrain.setDefaultCommand(defaultDriveCommand);
    configureBindings();
    Leds();
  }

  private ModularADLManager configureADL() {
    if (!Constants.ADLManager.USE_MODULAR_ADL) {
      DriverStation.reportWarning(
          "ADL disabled: legacy ADL is deprecated and is not instantiated by RobotContainer.",
          false
      );
      return null;
    }

    if (Constants.ADLManager.RUN_LEGACY_ADL_IN_PARALLEL) {
      DriverStation.reportWarning(
          "RUN_LEGACY_ADL_IN_PARALLEL is ignored because the legacy ADL executor is deprecated.",
          false
      );
    }

    ActionRegistry actionRegistry = new ActionRegistry();
    SeasonRegistrationContext seasonContext = new SeasonRegistrationContext(actionRegistry);
    new Season2023Module(clawManager, angularManager, chargeStationBalancer, drivetrain)
        .register(seasonContext);

    NetworkTablesActionIntentSource intentSource = new NetworkTablesActionIntentSource(
        Map.of(
            "ACQUIRE_PIECE", new NetworkTablesActionIntentSource.LegacyIntentMapping(
                Season2023Module.ACQUIRE_PIECE, "pieces", 5, false),
            "SCORE_PIECE", new NetworkTablesActionIntentSource.LegacyIntentMapping(
                Season2023Module.SCORE_PIECE, "grids", 8, false),
            "BALANCE", new NetworkTablesActionIntentSource.LegacyIntentMapping(
                Season2023Module.BALANCE, "station", 10, true),
            "ABORT", new NetworkTablesActionIntentSource.LegacyIntentMapping(
                Season2023Module.ABORT, "unknown", 1000, true)
        )
    );

    ADLDecisionEngine decisionEngine = new ADLDecisionEngine(actionRegistry, seasonContext);
    ActionExecutionService executionService = new ActionExecutionService(actionRegistry);

    return new ModularADLManager(
        intentSource,
        new NetworkTablesRobotContextFactsProvider(),
        decisionEngine,
        executionService,
        Constants.ADLManager.MIN_DECISION_INTERVAL
    );
  }

  private void configureBindings() {
    
    controller.options().onTrue(new InstantCommand(() -> defaultDriveCommand.toggleDriveMode()));
    controller.create().onTrue(new InstantCommand(() -> publishAdlIntent(
        Season2023Module.ABORT, "unknown", "", 1000, true
    )));

    controller.circle().onTrue(new InstantCommand(() -> publishAdlIntent(
        Season2023Module.ACQUIRE_PIECE, "pieces", "", 5, false
    )));
    controller.cross().onTrue(new InstantCommand(() -> publishAdlIntent(
        Season2023Module.SCORE_PIECE, "grids", "", 8, false
    )));
  }

  private void publishAdlIntent(
      String actionId,
      String zoneId,
      String parameters,
      int priority,
      boolean preempt
  ) {
    adlController.request(actionId, zoneId, parameters, priority, preempt);
  }

  public void Leds(){
      m_led = new AddressableLED(0);

     m_ledBuffer = new AddressableLEDBuffer(720);
     m_led.setLength(m_ledBuffer.getLength());

     LEDPattern gradient = LEDPattern.gradient(LEDPattern.GradientType.kContinuous, Color.kGreen, Color.kBlack, Color.kGreen);
    
     gradient.applyTo(m_ledBuffer);
     m_led.setData(m_ledBuffer);  

     m_led.setData(m_ledBuffer);
     m_led.start();
  }

  public void periodic() {
    var stressData = stressMonitor.generateData(drivetrain);
    stressController.update(stressData);
    stressPublisher.publish(
        stressData,
        //drivetrain.getChassisSpeed(),    
        stressController.getSpeedScale()
    );
    if (modularADLManager != null) {
      modularADLManager.periodic();
    }
    adlPublisher.publish(adlMonitor.read(), modularADLManager != null);
}

    public Command getAutonomousCommand() {
      return autonomousCommand;
  }

  public Drivetrain getDrivetrain() {
    return drivetrain;
  }

}
