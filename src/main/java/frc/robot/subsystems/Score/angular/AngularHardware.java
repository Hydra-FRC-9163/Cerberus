// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Score.angular;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AngularHardware extends SubsystemBase {
  
  public SparkMax AngularMotor = new SparkMax(4, MotorType.kBrushless);
  
   SparkMaxConfig config = new SparkMaxConfig();

  public AngularHardware() {
    
    config.idleMode(SparkBaseConfig.IdleMode.kBrake)
    .closedLoop
    .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
    .pid(0.0001, 0.0, 0.001);

    config.closedLoop.feedForward
    .kV(0.1)
    .kS(0.1)
    .kA(0.1);

    config.inverted(false);
    config.smartCurrentLimit(40);

    AngularMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  public void setAngularSpeed(double speed) {
    AngularMotor.set(speed);
  }

   public void stopAngular() {
    AngularMotor.set(0);
  }

  @Override
  public void periodic() {}
}
