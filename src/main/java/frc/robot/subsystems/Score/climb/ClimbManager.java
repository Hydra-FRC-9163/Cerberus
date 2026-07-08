package frc.robot.subsystems.Score.climb;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.Constants;

public class ClimbManager extends SubsystemBase { 

  private final ClimbHardware climber;
  private final PIDController pidController;

  public ClimbManager() {
    climber = new ClimbHardware();
    pidController = new PIDController(
      Constants.ClimbConstants.CLIMBER_kP, 
      Constants.ClimbConstants.CLIMBER_kI, 
      Constants.ClimbConstants.CLIMBER_kD);

      pidController.setTolerance(Constants.ClimbConstants.CLIMBER_TOLERANCE);
  }

  public void ClimbUp(double power) {
    climber.setClimbPower(0.4);
  }

  public void ClimbDown(double power) {
    climber.setClimbPower(-0.4);
  }

  public void stopClimb() {
    climber.stopClimb();
  }

  @Override
  public void periodic() {}
}
