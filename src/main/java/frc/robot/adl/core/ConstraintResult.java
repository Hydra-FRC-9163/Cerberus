package frc.robot.adl.core;

public record ConstraintResult(boolean allowed, String reason) {
    public static ConstraintResult allow() {
        return new ConstraintResult(true, "allowed");
    }

    public static ConstraintResult reject(String reason) {
        return new ConstraintResult(false, reason);
    }
}
