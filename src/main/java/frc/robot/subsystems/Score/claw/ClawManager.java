// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Score.claw;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClawManager extends SubsystemBase {
  public ClawHardware clawHardware;
  public ClawManager(ClawHardware clawHardware) {
    this.clawHardware = clawHardware;
  }

  public void setClawMotorSpeed() {
    clawHardware.setClawMotorSpeed(0.5);
  }

  public void stopClawMotor() {
    clawHardware.stopClawMotor();
  }

  @Override
  public void periodic() {
  }
}
