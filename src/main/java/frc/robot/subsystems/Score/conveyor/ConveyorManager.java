package frc.robot.subsystems.Score.conveyor;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ConveyorManager extends SubsystemBase {
  private final ConveyorHardware conveyorHardware;

  public ConveyorManager(ConveyorHardware conveyorHardware) {
    this.conveyorHardware = conveyorHardware;
  }

  public void setRollerSpeed() {
    conveyorHardware.setRollerSpeed(0.5);
  }

  public void stopRollers() {
    conveyorHardware.setRollerSpeed(0);
  }
  
  @Override
  public void periodic() {
  }
}
