// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Score.conveyor;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ConveyorManager extends SubsystemBase {
  private final ConveyorHardware conveyorHardware;

  public ConveyorManager(ConveyorHardware conveyorHardware) {
    this.conveyorHardware = conveyorHardware;
  }

  public void setRollerSpeed(double speed) {
    conveyorHardware.RollerMotor1.set(speed);
    conveyorHardware.RollerMotor2.set(speed);
  }

  public void stopRollers() {
    conveyorHardware.RollerMotor1.set(0);
    conveyorHardware.RollerMotor2.set(0);
  }
  
  @Override
  public void periodic() {
  }
}
