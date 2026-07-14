package frc.robot.subsystems.Score.claw;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClawManager extends SubsystemBase {

  public ClawHardware clawHardware;

  private GamePieceType currentPiece = GamePieceType.NONE;
  private GamePieceType nextIntakeTarget = GamePieceType.CONE;

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
 
  public void togglePieceType() {
    nextIntakeTarget = (nextIntakeTarget == GamePieceType.CONE) ? GamePieceType.CUBE : GamePieceType.CONE;
  }
 
  public void selectIntakeType(GamePieceType type) {
    nextIntakeTarget = type;
  }
 
  public void setPieceTypeOverride(GamePieceType type) {
    currentPiece = type;
  }
 
  public GamePieceType getCurrentPieceType() {
    return currentPiece;
  }
 
  public GamePieceType getSelectedIntakeType() {
    return nextIntakeTarget;
  }


  @Override
  public void periodic() {
  }
}
