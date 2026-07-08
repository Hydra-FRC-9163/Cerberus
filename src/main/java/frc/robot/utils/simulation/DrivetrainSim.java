package frc.robot.utils.simulation;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.simulation.ADXRS450_GyroSim;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import edu.wpi.first.wpilibj.simulation.EncoderSim;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Drivetrain.Drivetrain;
import frc.robot.subsystems.Sensors.ThroughBoreSubsystem;

/**
 * Renamed from Simulation -> DrivetrainSim to make room for LinearArmSim / AngularArmSim
 * without a generic "Simulation" name colliding in meaning.
 */
public class DrivetrainSim extends SubsystemBase {

    private final ThroughBoreSubsystem throughBoreSubsystem;
    private final Drivetrain drivetrain;
    private final DifferentialDriveOdometry odometry;

    private final Field2d field = new Field2d();
    private final NetworkTableEntry poseEntry;

    public EncoderSim leftEncoderSim;
    public EncoderSim rightEncoderSim;
    public ADXRS450_GyroSim gyroSim;
    public DifferentialDrivetrainSim driveSim;

    private final Pose3d poseA = new Pose3d();
    private final Pose3d poseB = new Pose3d();

    public DrivetrainSim(ThroughBoreSubsystem throughBoreSubsystem, Drivetrain drivetrain) {
        this.throughBoreSubsystem = throughBoreSubsystem;
        this.drivetrain = drivetrain;

        this.odometry = new DifferentialDriveOdometry(
            getHeading(),
            throughBoreSubsystem.leftEncoder.getDistance(),
            throughBoreSubsystem.rightEncoder.getDistance()
        );

        SmartDashboard.putData("Field", field);
        poseEntry = NetworkTableInstance.getDefault()
                    .getTable("SmartDashboard")
                    .getEntry("RobotPose");

        if (RobotBase.isSimulation()) {
            leftEncoderSim  = new EncoderSim(throughBoreSubsystem.leftEncoder);
            rightEncoderSim = new EncoderSim(throughBoreSubsystem.rightEncoder);
            gyroSim         = new ADXRS450_GyroSim(drivetrain.gyro);

            driveSim = DifferentialDrivetrainSim.createKitbotSim(
                DifferentialDrivetrainSim.KitbotMotor.kDualCIMPerSide,
                DifferentialDrivetrainSim.KitbotGearing.k10p71,
                DifferentialDrivetrainSim.KitbotWheelSize.kSixInch,
                null
            );
        }

        // Follower mode and inversion are now configured once inside Drivetrain's
        // own constructor - doing it again here was fighting drive()'s per-loop
        // .set() calls on the back motors every cycle.
        resetOdometry(new Pose2d());
    }

    @Override
    public void simulationPeriodic() {
        if (driveSim == null) {
            return;
        }

        // DifferentialDrivetrainSim.setInputs(leftVoltage, rightVoltage) - left MUST come first.
        // The original code passed (right, left), which steered the simulated robot
        // in the wrong direction relative to stick input.
        driveSim.setInputs(
            drivetrain.leftFront.getMotorOutputPercent() * 12.0,
            drivetrain.rightFront.getMotorOutputPercent() * 12.0
        );

        driveSim.update(0.02);

        leftEncoderSim.setDistance(driveSim.getLeftPositionMeters());
        rightEncoderSim.setDistance(driveSim.getRightPositionMeters());
        leftEncoderSim.setRate(driveSim.getLeftVelocityMetersPerSecond());
        rightEncoderSim.setRate(driveSim.getRightVelocityMetersPerSecond());

        gyroSim.setAngle(driveSim.getHeading().getDegrees());

        Logger.recordOutput("MyPoseArray", new Pose3d[] {poseA, poseB});
    }

    @Override
    public void periodic() {
    }

    public void Update(){
          odometry.update(
            getHeading(),
            throughBoreSubsystem.leftEncoder.getDistance(),
            throughBoreSubsystem.rightEncoder.getDistance()
        );

        Pose2d pose = odometry.getPoseMeters();
        Logger.recordOutput("Drive/Pose", pose);

        field.setRobotPose(pose);
        poseEntry.setDoubleArray(new double[] {pose.getX(), pose.getY(), pose.getRotation().getDegrees()});

        if (RobotBase.isSimulation()) {
            Logger.recordOutput("Robot/Pose3d", getPose3d());
        }
    }
    public Pose3d getPose3d() {
        return new Pose3d(
            new Translation3d(getPose().getX(), getPose().getY(), 0.0),
            new Rotation3d(0.0, 0.0, getHeading().getRadians())
        );
    }

    public Rotation2d getHeading() {
        return Rotation2d.fromDegrees(drivetrain.gyro.getAngle());
    }

    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    public void resetOdometry(Pose2d pose) {
        resetEncoders();
        drivetrain.gyro.reset();
        odometry.resetPosition(
            getHeading(),
            throughBoreSubsystem.leftEncoder.getDistance(),
            throughBoreSubsystem.rightEncoder.getDistance(),
            pose
        );
    }

    public void resetEncoders() {
        throughBoreSubsystem.leftEncoder.reset();
        throughBoreSubsystem.rightEncoder.reset();
    }

    public void rawTank(double left, double right) {
        drivetrain.leftFront.set(com.ctre.phoenix.motorcontrol.ControlMode.PercentOutput, left);
        drivetrain.rightFront.set(com.ctre.phoenix.motorcontrol.ControlMode.PercentOutput, right);

        Logger.recordOutput("Drive/LeftSetpoint", left);
        Logger.recordOutput("Drive/RightSetpoint", right);
    }
}