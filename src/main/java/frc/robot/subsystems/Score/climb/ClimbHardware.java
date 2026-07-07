package frc.robot.subsystems.Score.climb;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.Constants;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;

public class ClimbHardware extends SubsystemBase {
  private final SparkMax climb_left = new SparkMax(Constants.ClimbConstants.CLIMBER_LEFT_ID, MotorType.kBrushless); 
  private final SparkMax climb_right = new SparkMax(Constants.ClimbConstants.CLIMBER_RIGHT_ID, MotorType.kBrushless); 

  private final SparkMaxConfig cfg1;
  private final SparkMaxConfig cfg2; 
  
  public ClimbHardware() {
    cfg1 = new SparkMaxConfig();
    cfg1.idleMode(IdleMode.kBrake)
    .smartCurrentLimit(40);

    cfg2 = new SparkMaxConfig();
    cfg2.idleMode(IdleMode.kBrake)
    .smartCurrentLimit(40)
    .inverted(true);

    climb_right.configure(
        cfg2, 
        ResetMode.kResetSafeParameters, 
        PersistMode.kPersistParameters);

    climb_left.configure(cfg1, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  public void setClimbPower(double power) {
    climb_left.set(power);
    climb_right.set(power);
  } 

  public void stopClimb() {
    climb_left.stopMotor();
    climb_right.stopMotor();
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Left climb", climb_left.getEncoder().getPosition());
    SmartDashboard.putNumber("Right climb", climb_right.getEncoder().getPosition());
  }
}
