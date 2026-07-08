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

  public void LinearGoToPosition(double position) {

    if (linearHardware.LinearMotor.getEncoder().getPosition() >= position) {
      linearHardware.stopLinear();
    } else if (linearHardware.LinearMotor.getEncoder().getPosition() < position 
          || linearHardware.LinearMotor.getEncoder().getPosition() > position) {
    
      linearHardware.LinearMotor.getClosedLoopController()
    .setSetpoint(position, ControlType.kPosition);

    } else {
      linearHardware.stopLinear();
    }

  }

  public void AngularGoToPosition(double position) {
  if (linearHardware.AngularMotor.getEncoder().getPosition() >= position) {
      linearHardware.stopAngular();
    } else if (linearHardware.AngularMotor.getEncoder().getPosition() < position 
          || linearHardware.AngularMotor.getEncoder().getPosition() > position) {
    
      linearHardware.AngularMotor.getClosedLoopController()
    .setSetpoint(position, ControlType.kPosition);

    } else {
      linearHardware.stopAngular();
    }
  }

  public void LinearUp() {
    linearHardware.setLinearSpeed(0.5);
  }

  public void AngularUp() {
    linearHardware.setAngularSpeed(0.5);
  }

  public void LinearDown() {
    linearHardware.setLinearSpeed(-0.5);
  }

  public void AngularDown() {
    linearHardware.setAngularSpeed(-0.5);
  } 

  public void StopAll() {
    linearHardware.StopAll();
  }
}