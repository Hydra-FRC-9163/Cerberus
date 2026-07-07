package frc.robot.subsystems.Drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.Constants;

public class Drivetrain extends SubsystemBase {

    private final VictorSPX leftFront = drivetrain(Constants.Drivetrain.frenteleft, Constants.Drivetrain.leftinvertido);
    private final VictorSPX leftBack = drivetrain(Constants.Drivetrain.trasleft, Constants.Drivetrain.leftinvertido);
    private final VictorSPX rightFront = drivetrain(Constants.Drivetrain.frenteright, Constants.Drivetrain.rightinvertido);
    private final VictorSPX rightBack = drivetrain(Constants.Drivetrain.trasright, Constants.Drivetrain.rightinvertido);

    public Drivetrain() {}

    private VictorSPX drivetrain(int id, boolean inverted) {
        VictorSPX motor = new VictorSPX(id);
        motor.configFactoryDefault();
        motor.setInverted(inverted);
        motor.configNeutralDeadband(Constants.Drivetrain.deadzone);
        motor.setNeutralMode(NeutralMode.Brake);
        return motor;
    }

    public void drive(double leftSpeed, double rightSpeed) {
        leftFront.set(ControlMode.PercentOutput, leftSpeed);
        rightFront.set(ControlMode.PercentOutput, rightSpeed);
        leftBack.set(ControlMode.PercentOutput, leftSpeed);
        rightBack.set(ControlMode.PercentOutput, rightSpeed);
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
}