package frc.robot.subsystems.Sensors.ThroughBore;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ThroughBoreManager extends SubsystemBase {
  public ThroughBoreHardware throughBoreHardware;
  
  public ThroughBoreManager(ThroughBoreHardware throughBoreHardware) {
    this.throughBoreHardware = throughBoreHardware;
  }

    public double getDistanceMeters() { 
        return throughBoreHardware.rightEncoder.getDistance();
    }

    public double getSpeedMetersPerSecond() {
        return throughBoreHardware.rightEncoder.getRate();
    }

    public double getLeftMetersPerSecond() {
        return throughBoreHardware.leftEncoder.getRate();
    }

    public void reset() {
        throughBoreHardware.leftEncoder.reset();
        throughBoreHardware.rightEncoder.reset();
    }
}
