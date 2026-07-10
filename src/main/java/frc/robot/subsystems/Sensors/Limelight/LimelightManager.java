package frc.robot.subsystems.Sensors.Limelight;

public class LimelightManager {
    public LimelightHardware limelightHardware;
  
    public LimelightManager(LimelightHardware limelightHardware) {
        this.limelightHardware = limelightHardware;
    }

    public double getFrontTX() { return limelightHardware.llFront.getEntry("tx").getDouble(0); }
    public double getFrontTY() { return limelightHardware.llFront.getEntry("ty").getDouble(0); }
    public double getFrontTA() { return limelightHardware.llFront.getEntry("ta").getDouble(0); }
    public int getFrontTargetId() { return (int) limelightHardware.llFront.getEntry("tid").getInteger(-1); }
    public boolean frontHasTarget() { return limelightHardware.llFront.getEntry("tv").getDouble(0) == 1; }

    public double getLeftTX() { return limelightHardware.llBack.getEntry("tx").getDouble(0); }
    public double getLeftTY() { return limelightHardware.llBack.getEntry("ty").getDouble(0); }
    public double getLeftTA() { return limelightHardware.llBack.getEntry("ta").getDouble(0); }
    public int getLeftTargetId() { return (int) limelightHardware.llBack.getEntry("tid").getInteger(-1); }
    public boolean leftHasTarget() { return limelightHardware.llBack.getEntry("tv").getDouble(0) == 1; }

    public void setFrontAligned(boolean value) {
        limelightHardware.frontAligned = value;
    }

    public boolean isFrontAligned() {
        return limelightHardware.frontAligned;
    }

    public double getLatencyFront() {
        return limelightHardware.llFront.getEntry("tl").getDouble(0);
    }

    public double[] getBotPoseFront() {
        return limelightHardware.llFront.getEntry("botpose").getDoubleArray(new double[6]);
    }

    public void setLeftAligned(boolean value) {
        limelightHardware.leftAligned = value;
    }

    public boolean isLeftAligned() {
        return limelightHardware.leftAligned;
    }

    public double getLatencyLeft() {
        return limelightHardware.llBack.getEntry("tl").getDouble(0);
    }

    public double getFrontTxRad() {
        return Math.toRadians(getFrontTX());
    }
    
    public double getLeftTxRad() {
        return Math.toRadians(getLeftTX());
    }
}
