package frc.robot.subsystems.Score.claw;

import com.pathplanner.lib.config.RobotConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.RobotConfigures;

public class ClawManager extends SubsystemBase {

  public ClawHardware clawHardware;
  public RobotConfigures robotConfig;
  

  private GamePieceType currentPiece = GamePieceType.NONE;
  private GamePieceType nextIntakeTarget = GamePieceType.CONE;

  public ClawManager(ClawHardware clawHardware,RobotConfigures robotConfig) {
    this.clawHardware = clawHardware;
    this.robotConfig = robotConfig;
  }

  public void Intake() {
    clawHardware.setClawMotorSpeed(robotConfig.ClawSpeed);
  }

  public void Outtake() {
    clawHardware.setClawMotorSpeed(robotConfig.ClawOpenSpeed);
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
