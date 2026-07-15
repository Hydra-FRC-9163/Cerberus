package frc.robot.utils.simulation;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.simulation.ADXRS450_GyroSim;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import edu.wpi.first.wpilibj.simulation.EncoderSim;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Drivetrain.Drivetrain;
import frc.robot.subsystems.Sensors.ThroughBore.ThroughBoreHardware;

public class DrivetrainSim {

    private final ThroughBoreHardware throughBoreSubsystem;
    private final Drivetrain drivetrain;
    private final DifferentialDriveOdometry odometry;

    private final Field2d field = new Field2d();
    private final StructPublisher<Pose2d> pose2dPublisher;
    private final StructPublisher<Pose3d> pose3dPublisher;

    private EncoderSim leftEncoderSim;
    private EncoderSim rightEncoderSim;
    private ADXRS450_GyroSim gyroSim;
    private DifferentialDrivetrainSim driveSim;

    public DrivetrainSim(ThroughBoreHardware throughBoreSubsystem, Drivetrain drivetrain) {
        this.throughBoreSubsystem = throughBoreSubsystem;
        this.drivetrain = drivetrain;

        odometry = new DifferentialDriveOdometry(
            getHeading(),
            throughBoreSubsystem.leftEncoder.getDistance(),
            throughBoreSubsystem.rightEncoder.getDistance()
        );


        SmartDashboard.putData("Field", field);
        pose2dPublisher = NetworkTableInstance.getDefault()
            .getStructTopic("/Drive/Pose2d", Pose2d.struct)
            .publish();
        pose3dPublisher = NetworkTableInstance.getDefault()
            .getStructTopic("/Drive/Pose3d", Pose3d.struct)
            .publish();

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

        resetOdometry(new Pose2d());
    }

    public void update() {
        if (driveSim != null) {
            stepPhysics();
        }

        odometry.update(
            getHeading(),
            throughBoreSubsystem.leftEncoder.getDistance(),
            throughBoreSubsystem.rightEncoder.getDistance()
        );

        Pose2d pose = odometry.getPoseMeters();
        Logger.recordOutput("Drive/Pose", pose);
        field.setRobotPose(pose);
        pose2dPublisher.set(pose);

        if (RobotBase.isSimulation()) {
            Pose3d pose3d = getPose3d(pose);
            Logger.recordOutput("Robot/Pose3d", pose3d);
            pose3dPublisher.set(pose3d);
        }
    }

    private void stepPhysics() {
        driveSim.setInputs(
            drivetrain.getLeftCommandedOutput() * 12.0,
            drivetrain.getRightCommandedOutput() * 12.0
        );
        driveSim.update(0.02);

        leftEncoderSim.setDistance(driveSim.getLeftPositionMeters());
        rightEncoderSim.setDistance(driveSim.getRightPositionMeters());
        leftEncoderSim.setRate(driveSim.getLeftVelocityMetersPerSecond());
        rightEncoderSim.setRate(driveSim.getRightVelocityMetersPerSecond());

        gyroSim.setAngle(driveSim.getHeading().getDegrees());
    }

    private Pose3d getPose3d(Pose2d pose) {
        return new Pose3d(
            new Translation3d(pose.getX(), pose.getY(), 0.0),
            new Rotation3d(0.0, 0.0, pose.getRotation().getRadians())
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
        drivetrain.drive(left, right);
        Logger.recordOutput("Drive/LeftSetpoint", left);
        Logger.recordOutput("Drive/RightSetpoint", right);
    }
}