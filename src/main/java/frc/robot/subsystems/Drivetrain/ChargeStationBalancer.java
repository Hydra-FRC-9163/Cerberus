package frc.robot.subsystems.Drivetrain;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ChargeStationBalancer extends SubsystemBase {

    private final Drivetrain drivetrain;
    private final PIDController pid;
    private boolean enabled = false;

    // Constantes PID seguras para testes iniciais
    private static final double kP = 0.02;
    private static final double kI = 0.0;
    private static final double kD = 0.004;

    private static final double TILT_TOLERANCE_DEG = 2.5;

    public ChargeStationBalancer(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        this.pid = new PIDController(kP, kI, kD);
        pid.setSetpoint(0.0);
        pid.setTolerance(TILT_TOLERANCE_DEG);
    }

    public void enable() {
        this.enabled = true;
        pid.reset();
    }

    public void disable() {
        this.enabled = false;
        drivetrain.stop();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isBalanced() {
        return Math.abs(drivetrain.getTilt()) < TILT_TOLERANCE_DEG;
    }

    @Override
    public void periodic() {
        double tilt = drivetrain.getTilt();
        boolean balanced = isBalanced();

        SmartDashboard.putNumber("ChargeStation/TiltDeg", tilt);
        SmartDashboard.putBoolean("ChargeStation/Balanced", balanced);
        SmartDashboard.putBoolean("ChargeStation/BalanceModeActive", enabled);

        if (enabled) {
            if (balanced) {
                drivetrain.stop();
            } else {
                double output = pid.calculate(tilt);
                double maxSpeed = 0.35; // Limite de velocidade física para segurança
                output = Math.max(-maxSpeed, Math.min(maxSpeed, output));

                drivetrain.drive(output, output);
            }
        }
    }
}