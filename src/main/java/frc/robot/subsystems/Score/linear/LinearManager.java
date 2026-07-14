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

  public void LinearUp() {
    linearHardware.setLinearSpeed(0.5);
  }

  public void LinearDown() {
    linearHardware.setLinearSpeed(-0.5);
  }

  public void LinearStop() {
    linearHardware.stopLinear();
  }
}