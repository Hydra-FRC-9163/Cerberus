// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Score.claw;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.ResetMode;
import com.revrobotics.PersistMode;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClawHardware extends SubsystemBase {
  public SparkMax clawMotor = new SparkMax(5, SparkMax.MotorType.kBrushless);
 

  SparkMaxConfig clawMotorConfig = new SparkMaxConfig();
  public ClawHardware() {
    clawMotorConfig.idleMode(SparkBaseConfig.IdleMode.kBrake);
    clawMotorConfig.inverted(false);
    clawMotorConfig.smartCurrentLimit(30);

    clawMotor.configure(clawMotorConfig,ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  public void setClawMotorSpeed(double speed) {
    clawMotor.set(speed);
  }

  public void stopClawMotor() {
    clawMotor.set(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
