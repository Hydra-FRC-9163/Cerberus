package frc.robot.subsystems.Drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.Constants;

public class Drivetrain extends SubsystemBase {

    public final WPI_VictorSPX leftFront = new WPI_VictorSPX(Constants.Drivetrain.frenteleft);
    public final WPI_VictorSPX leftBack = new WPI_VictorSPX(Constants.Drivetrain.trasleft);
    public final WPI_VictorSPX rightFront = new WPI_VictorSPX(Constants.Drivetrain.frenteright);
    public final WPI_VictorSPX rightBack = new WPI_VictorSPX(Constants.Drivetrain.trasright);

    public final ADXRS450_Gyro gyro = new ADXRS450_Gyro();

    private final PowerDistribution pdh = new PowerDistribution();

    public Drivetrain() {
        leftBack.follow(leftFront);
    rightBack.follow(rightFront);

    leftFront.setInverted(false);
    rightFront.setInverted(true);

    leftBack.setInverted(InvertType.FollowMaster);
    rightBack.setInverted(InvertType.FollowMaster);

    leftFront.setNeutralMode(NeutralMode.Brake);
    leftBack.setNeutralMode(NeutralMode.Brake);
    rightFront.setNeutralMode(NeutralMode.Brake);
    rightBack.setNeutralMode(NeutralMode.Brake);
    }



    public void drive(double leftSpeed, double rightSpeed) {
        leftFront.set(ControlMode.PercentOutput, leftSpeed);
        rightFront.set(ControlMode.PercentOutput, rightSpeed);

           System.out.println("Recebi: " + leftSpeed + " " + rightSpeed);

    leftFront.set(ControlMode.PercentOutput, leftSpeed);
    rightFront.set(ControlMode.PercentOutput, rightSpeed);

    System.out.println("Motor L = " + leftFront.getMotorOutputPercent());
    System.out.println("Motor R = " + rightFront.getMotorOutputPercent());
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

    public void reset(){
        gyro.reset();
    }

    public double getTotalRobotCurrent() {
    return pdh.getTotalCurrent();
    }
}