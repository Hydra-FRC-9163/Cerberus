package frc.robot.subsystems.Sensors.Limelight;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.Constants;

public class LimelightHardware extends SubsystemBase {

    public final NetworkTable llFront;
    public final NetworkTable llBack;
    public boolean frontAligned = false;
    public boolean leftAligned = false;

    public LimelightHardware(String id) {
        llFront = NetworkTableInstance.getDefault().getTable(Constants.LimeLight.limeLightFront);
        llBack = NetworkTableInstance.getDefault().getTable(Constants.LimeLight.limeLightLeft);
    }
}