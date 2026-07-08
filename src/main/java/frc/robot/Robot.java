// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Drivetrain.Drivetrain;

public class Robot extends LoggedRobot {
  private Command m_autonomousCommand;

  private final RobotContainer m_robotContainer;
  private final Drivetrain drivetrain;

  public Robot() {
    m_robotContainer = new RobotContainer();
    drivetrain = m_robotContainer.getDrivetrain();
     Logger.recordMetadata("ProjectName", "Binga"); 
     Logger.recordMetadata("RuntimeType", RobotBase.getRuntimeType().toString());
   
     if (RobotBase.isSimulation()) {
       // Salva no ./logs (dentro da pasta do projeto) + envia via NT4
       Logger.addDataReceiver(new WPILOGWriter("logs"));
       Logger.addDataReceiver(new NT4Publisher());

       Logger.addDataReceiver(new WPILOGWriter("logs"));
       Logger.addDataReceiver(new NT4Publisher());
   
     } else {
       // Robo real → salva no USB (/U) + envia via NT4
       Logger.addDataReceiver(new WPILOGWriter("/U/logs"));
       Logger.addDataReceiver(new NT4Publisher());
     }
     //Logger.start();
     try {
      Logger.start();
  } catch (Exception e) {
      e.printStackTrace();
  } 
  }

  @Override
  public void simulationPeriodic(){
     m_robotContainer.getSimulation().simulationPeriodic();
  }

  @Override
  public void robotPeriodic() {

      CommandScheduler.getInstance().run();

    System.out.println(
        CommandScheduler.getInstance().requiring(drivetrain));
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      CommandScheduler.getInstance().schedule(m_autonomousCommand);
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}
}
