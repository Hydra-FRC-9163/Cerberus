// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Score.conveyor;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.ResetMode;
import com.revrobotics.PersistMode;

public class ConveyorHardware extends SubsystemBase {
  protected SparkMax RollerMotor1 = new SparkMax(1,MotorType.kBrushless);
  protected SparkMax RollerMotor2 = new SparkMax(2,MotorType.kBrushless);

  SparkMaxConfig config = new SparkMaxConfig();

  public ConveyorHardware() {
   
    config.idleMode(SparkBaseConfig.IdleMode.kBrake);
    config.inverted(false);
    config.smartCurrentLimit(30);
    
    RollerMotor1.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    RollerMotor2.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void periodic() {
  }
}
