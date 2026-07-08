package frc.robot.subsystems.Drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.RobotBase;
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

    public Drivetrain() {
        leftFront  = drivetrain(Constants.Drivetrain.frenteleft, Constants.Drivetrain.leftinvertido);
        leftBack   = drivetrain(Constants.Drivetrain.trasleft, Constants.Drivetrain.leftinvertido);
        rightFront = drivetrain(Constants.Drivetrain.frenteright, Constants.Drivetrain.rightinvertido);
        rightBack  = drivetrain(Constants.Drivetrain.trasright, Constants.Drivetrain.rightinvertido);

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
        leftFront.set(ControlMode.PercentOutput, leftSpeed);
        rightFront.set(ControlMode.PercentOutput, rightSpeed);
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

    public double getTotalRobotCurrent() {
        return pdh.getTotalCurrent();
    }

    public double getEstimatedDrivetrainCurrent() {
        if (!RobotBase.isSimulation()) {
            return getTotalRobotCurrent();
        }

        double leftLoad = Math.abs(getLeftMotorOutput());
        double rightLoad = Math.abs(getRightMotorOutput());
        return (leftLoad + rightLoad) * 40.0;
    }

    public double getEstimatedChassisSpeedMps() {
        double averageOutput = (Math.abs(getLeftMotorOutput()) + Math.abs(getRightMotorOutput())) / 2.0;
        return averageOutput * 3.6;
    }
    @Override
    public void periodic() {
        if (drivetrainSim != null) {
            drivetrainSim.Update();
        }
    }
}
