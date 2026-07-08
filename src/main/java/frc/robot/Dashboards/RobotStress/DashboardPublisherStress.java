package frc.robot.Dashboards.RobotStress;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class DashboardPublisherStress {
    private final NetworkTable stressTable = NetworkTableInstance.getDefault().getTable("RobotStress");
    
public void publish(RobotStressData data, double chassisSpeed) {
        publish(data, chassisSpeed, 1.0);
    }

public void publish(RobotStressData data, double chassisSpeed, double speedScale) {

        stressTable.getEntry("batteryVoltage").setDouble(data.batteryVoltage);
        stressTable.getEntry("totalCurrent").setDouble(data.totalCurrent);
        stressTable.getEntry("drivetrainCurrent").setDouble(data.drivetrainCurrent);

        stressTable.getEntry("stressScore").setDouble(data.stressScore);
        stressTable.getEntry("stressLevel").setString(data.stressLevel);

        stressTable.getEntry("chassisSpeed").setDouble(chassisSpeed);
        stressTable.getEntry("speedScale").setDouble(speedScale);

    }
}
