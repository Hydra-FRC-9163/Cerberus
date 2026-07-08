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
    linearHardware.LinearMotor.getClosedLoopController().setSetpoint(position, ControlType.kPosition);
  }

  public void AngularGoToPosition(double position) {
    linearHardware.AngularMotor.getClosedLoopController().setSetpoint(position, ControlType.kPosition);
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