// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Score.linear;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.FeedbackSensor;
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

public class LinearHardware extends SubsystemBase {
   public SparkMax LinearMotor = new SparkMax(3, MotorType.kBrushless);
   public SparkMax LinearMotor2 = new SparkMax(4, MotorType.kBrushless);


   SparkMaxConfig config1 = new SparkMaxConfig();
   SparkMaxConfig config2 = new SparkMaxConfig();

  public LinearHardware() {
    config1.idleMode(SparkBaseConfig.IdleMode.kBrake)
    .closedLoop
    .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
    .pid(0.0001, 0.0, 0.001);

    config1.closedLoop.feedForward
    .kV(0.1)
    .kS(0.1)
    .kA(0.1);

    config1.inverted(false);
    config1.smartCurrentLimit(40);

    config2.idleMode(SparkBaseConfig.IdleMode.kBrake)
    .closedLoop
    .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
    .pid(0.0001, 0.0, 0.001);

    config2.closedLoop.feedForward
    .kV(0.1)
    .kS(0.1)
    .kA(0.1);

    config2.inverted(false);
    config2.smartCurrentLimit(40);

    LinearMotor.configure(config1, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    LinearMotor2.configure(config2, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

   

  }

  @Override
  public void periodic() {

  }
}
