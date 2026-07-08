package frc.robot.subsystems.Score.claw;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.ResetMode;
import com.revrobotics.PersistMode;

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
  public void periodic() {}
}
