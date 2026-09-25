package frc.robot.subsystems.Drivetrain;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.Constants;
import frc.robot.utils.simulation.DrivetrainSim;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

public class Drivetrain extends SubsystemBase {

    public final SparkMax leftFront;
    public final SparkMax leftBack;
    public final SparkMax rightFront;
    public final SparkMax rightBack;

    public final SparkMaxConfig configE;
    public final SparkMaxConfig configD;
    public final SparkMaxConfig InvertedConfigE;
    public final SparkMaxConfig InvertedConfigD;

    private final PowerDistribution pdh = new PowerDistribution();

    public final ADXRS450_Gyro gyro = new ADXRS450_Gyro();

    private DrivetrainSim drivetrainSim;
    private double leftCommandedOutput;
    private double rightCommandedOutput;

    public Drivetrain() {



        leftFront  = new SparkMax(Constants.Drivetrain.frontLeft, MotorType.kBrushed);
        leftBack   = new SparkMax(Constants.Drivetrain.backLeft, MotorType.kBrushed);
        rightFront = new SparkMax(Constants.Drivetrain.frontRight, MotorType.kBrushed);
        rightBack = new SparkMax(Constants.Drivetrain.backRight, MotorType.
        kBrushed);

        configE = new SparkMaxConfig();
        configE.idleMode(SparkBaseConfig.IdleMode.kBrake);
        configE.inverted(false);

        configD = new SparkMaxConfig();
        configD.idleMode(SparkBaseConfig.IdleMode.kBrake);
        configD.inverted(true);

        InvertedConfigE = new SparkMaxConfig();
        InvertedConfigE.idleMode(SparkBaseConfig.IdleMode.kBrake);
        InvertedConfigE.inverted(false);
        InvertedConfigE.follow(leftFront);

        InvertedConfigD = new SparkMaxConfig();
        InvertedConfigD.idleMode(SparkBaseConfig.IdleMode.kBrake);
        InvertedConfigD.inverted(false);
        InvertedConfigD.follow(rightFront);

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
        leftFront.set(leftSpeed);
        rightFront.set(rightSpeed);
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
        return leftFront.getAppliedOutput();
    }

    public double getRightMotorOutput() {
        return rightFront.getAppliedOutput();
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