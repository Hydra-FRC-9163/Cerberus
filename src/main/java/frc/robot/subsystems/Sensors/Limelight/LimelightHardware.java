package frc.robot.subsystems.Sensors;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.Constants;

public class LimelightSubsystem extends SubsystemBase {

    private final NetworkTable llFront;
    private final NetworkTable llLeft;
    private boolean frontAligned = false;
    private boolean leftAligned = false;

    public LimelightSubsystem(String id) {
        llFront = NetworkTableInstance.getDefault().getTable(Constants.LimeLight.limeLightFront);
        llLeft = NetworkTableInstance.getDefault().getTable(Constants.LimeLight.limeLightLeft);
    }

    public double getFrontTX() { return llFront.getEntry("tx").getDouble(0); }
    public double getFrontTY() { return llFront.getEntry("ty").getDouble(0); }
    public double getFrontTA() { return llFront.getEntry("ta").getDouble(0); }
    public int getFrontTargetId() { return (int) llFront.getEntry("tid").getInteger(-1); }
    public boolean frontHasTarget() { return llFront.getEntry("tv").getDouble(0) == 1; }

    public double getLeftTX() { return llLeft.getEntry("tx").getDouble(0); }
    public double getLeftTY() { return llLeft.getEntry("ty").getDouble(0); }
    public double getLeftTA() { return llLeft.getEntry("ta").getDouble(0); }
    public int getLeftTargetId() { return (int) llLeft.getEntry("tid").getInteger(-1); }
    public boolean leftHasTarget() { return llLeft.getEntry("tv").getDouble(0) == 1; }

    public void setFrontAligned(boolean value) {
        frontAligned = value;
    }

    public boolean isFrontAligned() {
        return frontAligned;
    }

    public double getLatencyFront() {
        return llFront.getEntry("tl").getDouble(0);
    }

    public double[] getBotPoseFront() {
        return llFront.getEntry("botpose").getDoubleArray(new double[6]);
    }

    public void setLeftAligned(boolean value) {
        leftAligned = value;
    }

    public boolean isLeftAligned() {
        return leftAligned;
    }

    public double getLatencyLeft() {
        return llLeft.getEntry("tl").getDouble(0);
    }

    public double getFrontTxRad() {
        return Math.toRadians(getFrontTX());
    }
    
    public double getLeftTxRad() {
        return Math.toRadians(getLeftTX());
    }
}