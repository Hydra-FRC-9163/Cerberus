// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Score.linear;

import com.revrobotics.spark.SparkBase.ControlType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LinearManager extends SubsystemBase {
  private final LinearHardware linearHardware;

  public LinearManager(LinearHardware linearHardware) {
    this.linearHardware = linearHardware;
  }


  @Override
  public void periodic() {
  }


  public void GoToPosition1(double position) {

    if (linearHardware.LinearMotor.getEncoder().getPosition() >= position) {
      stopLinear1();
    } else if (linearHardware.LinearMotor.getEncoder().getPosition() < position 
          || linearHardware.LinearMotor.getEncoder().getPosition() > position) {
    
      linearHardware.LinearMotor.getClosedLoopController()
    .setSetpoint(position, ControlType.kPosition);

    } else {
      linearHardware.LinearMotor.set(0);
    }

  }

  public void GoToPosition2(double position) {
  if (linearHardware.LinearMotor2.getEncoder().getPosition() >= position) {
      stopLinear2();
    } else if (linearHardware.LinearMotor2.getEncoder().getPosition() < position 
          || linearHardware.LinearMotor2.getEncoder().getPosition() > position) {
    
      linearHardware.LinearMotor2.getClosedLoopController()
    .setSetpoint(position, ControlType.kPosition);

    } else {
      linearHardware.LinearMotor2.set(0);
    }
  }


    public void setLinearSpeed(double speed) {
    linearHardware.LinearMotor.set(speed);
  }
  public void setLinearSpeed2(double speed) {
    linearHardware.LinearMotor.set(speed);
  }
  
  public void stopLinear1() {
    linearHardware.LinearMotor.set(0);
  }

  public void stopLinear2() {
    linearHardware.LinearMotor.set(0);
  }

  public void StopALL() {
    linearHardware.LinearMotor.set(0);
    linearHardware.LinearMotor2.set(0);
  }

}
