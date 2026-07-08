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
      linearHardware.stopLinear1();
    } else if (linearHardware.LinearMotor.getEncoder().getPosition() < position 
          || linearHardware.LinearMotor.getEncoder().getPosition() > position) {
    
      linearHardware.LinearMotor.getClosedLoopController()
    .setSetpoint(position, ControlType.kPosition);

    } else {
      linearHardware.stopLinear1();
    }

  }

  public void GoToPosition2(double position) {
  if (linearHardware.LinearMotor2.getEncoder().getPosition() >= position) {
      linearHardware.stopLinear2();
    } else if (linearHardware.LinearMotor2.getEncoder().getPosition() < position 
          || linearHardware.LinearMotor2.getEncoder().getPosition() > position) {
    
      linearHardware.LinearMotor2.getClosedLoopController()
    .setSetpoint(position, ControlType.kPosition);

    } else {
      linearHardware.stopLinear2();
    }
  }
}
