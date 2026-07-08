package frc.robot.subsystems.Score.claw;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClawManager extends SubsystemBase {
  public ClawHardware clawHardware;
  public ClawManager(ClawHardware clawHardware) {
    this.clawHardware = clawHardware;
  }

  public void Intake() {
    clawHardware.setClawMotorSpeed(0.5);
  }

  public void Outtake() {
    clawHardware.setClawMotorSpeed(-0.5);
  }

  public void stopClawMotor() {
    clawHardware.stopClawMotor();
  }

  @Override
  public void periodic() {
  }
}
