package frc.robot.subsystems.Drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.Constants;
import frc.robot.utils.simulation.DrivetrainSim;

public class Drivetrain extends SubsystemBase {

    public final VictorSPX leftFront;
    public final VictorSPX leftBack;
    public final VictorSPX rightFront;
    public final VictorSPX rightBack;

    private final PowerDistribution pdh = new PowerDistribution();

    public final ADXRS450_Gyro gyro = new ADXRS450_Gyro();

    private DrivetrainSim drivetrainSim;
    private double leftCommandedOutput;
    private double rightCommandedOutput;

    public Drivetrain() {
        leftFront  = drivetrain(Constants.Drivetrain.frontLeft, Constants.Drivetrain.isLeftInverted);
        leftBack   = drivetrain(Constants.Drivetrain.backLeft, Constants.Drivetrain.isLeftInverted);
        rightFront = drivetrain(Constants.Drivetrain.frontRight, Constants.Drivetrain.isRightInverted);
        rightBack  = drivetrain(Constants.Drivetrain.backRight, Constants.Drivetrain.isRightInverted);

        leftBack.follow(leftFront);
        rightBack.follow(rightFront);
        leftBack.setInverted(InvertType.FollowMaster);
        rightBack.setInverted(InvertType.FollowMaster);
    }
     
    public VictorSPX drivetrain(int id, boolean invertido) {
        VictorSPX motor = new VictorSPX(id);
        motor.setNeutralMode(NeutralMode.Brake);
        motor.configNeutralDeadband(Constants.Drivetrain.deadzone);
        motor.setInverted(invertido);
        return motor;
    }

    public void drive(double leftSpeed, double rightSpeed) {
        leftCommandedOutput = leftSpeed;
        rightCommandedOutput = rightSpeed;
        leftFront.set(ControlMode.PercentOutput, leftSpeed);
        rightFront.set(ControlMode.PercentOutput, rightSpeed);
    }

    public void diffDrive(double forwardAxis, double turnAxis, double speed) {
        double forward = deadband(forwardAxis) * speed;
        double turn = deadband(turnAxis) * speed;

        double left = clamp(forward + turn);
        double right = clamp(forward - turn);

        drive(left, right);
    }

    private double deadband(double value) {
        return Math.abs(value) < Constants.Drivetrain.deadzone ? 0.0 : value;
    }

    private double clamp(double value) {
        return Math.max(-1.0, Math.min(1.0, value));
    }

    public void stop() {
        drive(0, 0);
    }

    public double getLeftMotorOutput() {
        return leftFront.getMotorOutputPercent();
    }

    public double getRightMotorOutput() {
        return rightFront.getMotorOutputPercent();
    }

    public double getLeftCommandedOutput() {
        return leftCommandedOutput;
    }

    public double getRightCommandedOutput() {
        return rightCommandedOutput;
    }

    public double getTotalRobotCurrent() {
        return pdh.getTotalCurrent();
    }

    public double getEstimatedDrivetrainCurrent() {
        if (!RobotBase.isSimulation()) {
            return getTotalRobotCurrent();
        }

        double leftLoad = Math.abs(getLeftCommandedOutput());
        double rightLoad = Math.abs(getRightCommandedOutput());
        return (leftLoad + rightLoad) * 40.0;
    }

    public double getEstimatedChassisSpeedMps() {
        double averageOutput = (Math.abs(getLeftCommandedOutput()) + Math.abs(getRightCommandedOutput())) / 2.0;
        return averageOutput * 3.6;
    }

    public void attachSimulation(DrivetrainSim drivetrainSim) {
        this.drivetrainSim = drivetrainSim;
    }

    // Método seguro que retorna a inclinação atual (ângulo do Gyro)
    public double getTilt() {
        return gyro.getAngle();
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Drive/LeftOutput", getLeftMotorOutput());
        SmartDashboard.putNumber("Drive/RightOutput", getRightMotorOutput());
        SmartDashboard.putNumber("Drive/LeftCommand", getLeftCommandedOutput());
        SmartDashboard.putNumber("Drive/RightCommand", getRightCommandedOutput());
        SmartDashboard.putNumber("Drive/GyroAngle", gyro.getAngle());

        if (drivetrainSim != null) {
            drivetrainSim.update();
        }
    }
}