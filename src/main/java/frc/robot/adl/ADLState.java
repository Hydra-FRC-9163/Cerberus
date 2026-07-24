package frc.robot.adl;

public enum ADLState {
    IDLE, MOVING, ACQUIRING, SCORING, BALANCING, BLOCKED, EMERGENCY;

    public boolean isBusy() {
        return this == MOVING || this == ACQUIRING || this == SCORING || this == BALANCING;
    }

    public boolean isCritical() {
        return this == BLOCKED || this == EMERGENCY;
    }
}