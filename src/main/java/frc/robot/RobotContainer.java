package frc.robot;

import java.io.File;
import java.util.function.DoubleSupplier;

import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotBase;
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
import frc.robot.adl.core.ActionExecutionService;
import frc.robot.adl.core.ActionRegistry;
import frc.robot.adl.core.LegacyRobotContextFactsAdapter;
import frc.robot.adl.core.ModularADLManager;
import frc.robot.adl.core.NetworkTablesActionIntentSource;
import frc.robot.adl.core.SeasonRegistrationContext;
import frc.robot.commands.DefaultDriveCommand;
import frc.robot.subsystems.Drivetrain.Drivetrain;
import frc.robot.subsystems.Score.angular.AngularHardware;
import frc.robot.subsystems.Score.angular.AngularManager;
import frc.robot.subsystems.Score.claw.ClawHardware;
import frc.robot.subsystems.Score.linear.LinearHardware;

import frc.robot.subsystems.Score.claw.ClawManager;
import frc.robot.subsystems.Score.linear.LinearManager;
import frc.robot.subsystems.Sensors.ThroughBore.ThroughBoreHardware;
import frc.robot.utils.Constants;
import frc.robot.utils.simulation.AngularArmSim;
import frc.robot.utils.simulation.DrivetrainSim;
import frc.robot.utils.simulation.LinearArmSim;

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

  private final AngularHardware angularHardware;
  private final AngularManager angularManager;

  private final RobotStressMonitor stressMonitor;
  private final RobotStressController stressController;
  private final DashboardPublisherStress stressPublisher;
  private final DriveModePublisher modePublisher;

  private final SequentialCommandGroup autonomousCommand;
  private ThroughBoreHardware throughBore;

  private DrivetrainSim drivetrainSim;
  private AngularArmSim angularSim;
  private LinearArmSim linearSim;

    public RobotContainer() {
  
      controller          = new CommandPS5Controller(Constants.PS5_ID);
      logitech            = new Joystick(Constants.LOGITECH_ID);
  
      drivetrain          = new Drivetrain();
      defaultDriveCommand = new DefaultDriveCommand(drivetrain, controller);
  
      clawHardware        = new ClawHardware();
      linearHardware      = new LinearHardware();
  
      clawManager         = new ClawManager(clawHardware);
      linearManager       = new LinearManager(linearHardware);

      angularHardware     = new AngularHardware();
      angularManager      = new AngularManager(angularHardware);
  
      stressMonitor       = new RobotStressMonitor();
      stressController    = new RobotStressController();
      stressPublisher     = new DashboardPublisherStress();
      modePublisher       = new DriveModePublisher();
      throughBore         = new ThroughBoreHardware();
      
      if (RobotBase.isSimulation()) {
        drivetrainSim = new DrivetrainSim(throughBore, drivetrain);
        drivetrain.attachSimulation(drivetrainSim);
        linearSim = new LinearArmSim(linearHardware);
        angularSim = new AngularArmSim(clawHardware, linearHardware, linearSim, angularHardware);
      }

    autonomousCommand   = new SequentialCommandGroup();

    drivetrain.setDefaultCommand(defaultDriveCommand);
    configureBindings();
  }

  private void configureBindings() {
    
    controller.options().onTrue(new InstantCommand(() -> defaultDriveCommand.toggleDriveMode()));

    controller.L2().whileTrue(new InstantCommand(() -> angularManager.AngularUp()));
    controller.R2().whileTrue(new InstantCommand(() -> angularManager.AngularDown()));

    controller.L1().whileTrue(new InstantCommand(() -> linearManager.LinearUp()));
    controller.R1().whileTrue(new InstantCommand(() -> linearManager.LinearDown()));

    controller.circle().whileTrue(new InstantCommand(() -> clawManager.Intake()));
    controller.cross().whileTrue(new InstantCommand(() -> clawManager.Outtake()));
  }

  public void periodic() {
    var stressData = stressMonitor.generateData(drivetrain);
    stressController.update(stressData);
    stressPublisher.publish(
        stressData,
        //drivetrain.getChassisSpeed(),    
        stressController.getSpeedScale()
    );
}

    public Command getAutonomousCommand() {
      return autonomousCommand;
  }

  public Drivetrain getDrivetrain() {
    return drivetrain;
  }

}
