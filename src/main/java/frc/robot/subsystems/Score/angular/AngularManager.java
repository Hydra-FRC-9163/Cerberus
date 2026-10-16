// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Score.angular;

import com.revrobotics.spark.SparkBase.ControlType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.RobotConfigures;

public class AngularManager extends SubsystemBase {

  private final AngularHardware AngularHardware;
  public final RobotConfigures robotConfig;

  public AngularManager(AngularHardware AngularHardware, RobotConfigures robotConfig) {
    this.AngularHardware = AngularHardware;
    this.robotConfig = robotConfig;
  }

  public void AngularGoToPosition(double position) {
    AngularHardware.AngularMotor.getClosedLoopController().setSetpoint(position, ControlType.kPosition);
  }

  public void AngularUp() {
    AngularHardware.setAngularSpeed(robotConfig.armSpeedUp);
  }

  public void AngularDown() {
    AngularHardware.setAngularSpeed(-robotConfig.armSpeedDown);
  }

  public void AngularStop() {
    AngularHardware.stopAngular();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
