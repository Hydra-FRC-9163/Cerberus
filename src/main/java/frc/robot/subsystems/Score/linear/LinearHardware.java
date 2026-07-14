package frc.robot.subsystems.Score.linear;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.ResetMode;
import com.revrobotics.PersistMode;

public class LinearHardware extends SubsystemBase {
   public SparkMax LinearMotor = new SparkMax(3, MotorType.kBrushless);

   SparkMaxConfig config1 = new SparkMaxConfig();

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

    LinearMotor.configure(config1, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

   public void setLinearSpeed(double speed) {
    LinearMotor.set(speed);
  }
  
  public void stopLinear() {
    LinearMotor.set(0);
  }

  @Override
  public void periodic() {}
}
