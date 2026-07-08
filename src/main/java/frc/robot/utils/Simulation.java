
package frc.robot.utils;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.simulation.ADXRS450_GyroSim;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import edu.wpi.first.wpilibj.simulation.EncoderSim;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Drivetrain.Drivetrain;
import frc.robot.subsystems.Sensors.ThroughBoreSubsystem;
import swervelib.simulation.ironmaple.simulation.SimulatedArena;

public class Simulation extends SubsystemBase {
       private ThroughBoreSubsystem throughBoreSubsystem;
       private Drivetrain drivetrain;
      private final double diametroRoda = 0.06; // 6 cm
        private final DifferentialDriveOdometry odometry;

         private final Field2d field = new Field2d();
         private final NetworkTableEntry poseEntry;
         public EncoderSim leftEncoderSim;
         public EncoderSim rightEncoderSim;
         public ADXRS450_GyroSim gyroSim;
         public DifferentialDrivetrainSim driveSim;
         private double _debugX = 0.0;
         private double _lastTime = 0.0;
         private double simYaw = 0.0;


             private final Pose3d poseA = new Pose3d();
             private final Pose3d poseB = new Pose3d();

  public Simulation(ThroughBoreSubsystem throughBoreSubsystem, Drivetrain drivetrain) {
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
          

             // Inicializa a simulação
              
        }
    
        reqDrive();   
    }
    @Override
    public void simulationPeriodic() {
      if (driveSim != null) {
        driveSim.setInputs(drivetrain.rightFront.get() * 12.0, drivetrain.leftFront.get() * 12.0);
    
        driveSim.update(0.02);
    
        leftEncoderSim.setDistance(driveSim.getLeftPositionMeters());
        rightEncoderSim.setDistance(driveSim.getRightPositionMeters());
        leftEncoderSim.setRate(driveSim.getLeftVelocityMetersPerSecond());
        rightEncoderSim.setRate(driveSim.getRightVelocityMetersPerSecond());
    
        gyroSim.setAngle(driveSim.getHeading().getDegrees());

        Logger.recordOutput("MyPose", poseA);
        Logger.recordOutput("MyPoseArray", poseA, poseB);
        Logger.recordOutput("MyPoseArray", new Pose3d[] {poseA, poseB});
        
      }
    }

    @Override
    public void periodic() {
      odometry.update( getHeading(),throughBoreSubsystem.leftEncoder.getDistance(),throughBoreSubsystem.rightEncoder.getDistance());
      Pose2d pose = odometry.getPoseMeters();
      Logger.recordOutput("Drive/Pose", new Pose2d(pose.getX(), pose.getY(), pose.getRotation()));
      
      
      field.setRobotPose(pose);
      poseEntry.setDoubleArray(new double[] {pose.getX(), pose.getY(), pose.getRotation().getDegrees()});
      if (pose.getX() != _debugX) {
        _debugX = pose.getX();
        double now = Timer.getFPGATimestamp();
        double rate = 1.0 / (now - _lastTime);
        _lastTime = now;

        Pose3d pose3d = new Pose3d(
          new Translation3d(pose.getX(), pose.getY(), 0.0), // z = 0
          new Rotation3d(0.0, 0.0, pose.getRotation().getRadians()) // só yaw
      );

      if (RobotBase.isSimulation()) {
        Logger.recordOutput("Robot/Pose3d", pose3d);

     }
    }
  }
    public Pose3d getPose3d(){
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
      odometry.resetPosition(getHeading(), throughBoreSubsystem.leftEncoder.getDistance(), throughBoreSubsystem.rightEncoder.getDistance(), pose);
    }

    public void resetEncoders() {
      throughBoreSubsystem.leftEncoder.reset();
      throughBoreSubsystem.rightEncoder.reset();
    }

    public void reqDrive() {
      resetOdometry(getPose());
      drivetrain.leftBack.follow(drivetrain.leftFront);
      drivetrain.rightBack.follow(drivetrain.rightFront);

      drivetrain.rightFront.setInverted(true);
      drivetrain.leftFront.setInverted(false);
      drivetrain.leftBack.setInverted(InvertType.FollowMaster);
      drivetrain.rightBack.setInverted(InvertType.FollowMaster);

      drivetrain.rightFront.setNeutralMode(NeutralMode.Brake);
      drivetrain.leftFront.setNeutralMode(NeutralMode.Brake);
      drivetrain.rightBack.setNeutralMode(NeutralMode.Brake);
      drivetrain.leftBack.setNeutralMode(NeutralMode.Brake);

      double distancePerPulse = (Math.PI * diametroRoda) / 2048;
      throughBoreSubsystem.leftEncoder.setDistancePerPulse(distancePerPulse);
      throughBoreSubsystem.rightEncoder.setDistancePerPulse(distancePerPulse);
    }

    public void rawTank(double left, double right) {
      drivetrain.leftFront.set(left);
      drivetrain.rightFront.set(right);

      Logger.recordOutput("Drive/LeftSetpoint", left);
      Logger.recordOutput("Drive/RightSetpoint", right);
    }


  }

