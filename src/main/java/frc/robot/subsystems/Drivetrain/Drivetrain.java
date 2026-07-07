package frc.robot.subsystems.Drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.Constants;
import frc.robot.utils.MathUtils;

public class Drivetrain extends SubsystemBase {

    private final VictorSPX leftFront = drivetrain(Constants.Drivetrain.frenteleft, Constants.Drivetrain.leftinvertido);
    private final VictorSPX leftBack = drivetrain(Constants.Drivetrain.trasleft, Constants.Drivetrain.leftinvertido);
    private final VictorSPX rightFront = drivetrain(Constants.Drivetrain.frenteright, Constants.Drivetrain.rightinvertido);
    private final VictorSPX rightBack = drivetrain(Constants.Drivetrain.trasright, Constants.Drivetrain.rightinvertido);

    private final MathUtils mathUtils = new MathUtils();

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

    public void driveComControle(Joystick joystick) {
        double eixoX1 = joystick.getRawAxis(Constants.Drivetrain.eixoX);
        double eixoY1 = -joystick.getRawAxis(Constants.Drivetrain.eixoY); 
        double eixoX2 = -joystick.getRawAxis(Constants.Drivetrain.eixoX2); 
        double eixoY2 = -joystick.getRawAxis(Constants.Drivetrain.eixoY2);
        double gatilhoEsq = joystick.getRawAxis(Constants.Drivetrain.L2);
        double gatilhoDir = joystick.getRawAxis(Constants.Drivetrain.R2);
        int pov = joystick.getPOV();

        double[] magSeno = mathUtils.calcularMagESeno(eixoX1, eixoX2, eixoY1, eixoY2);

        double velL2 = mathUtils.calcularL2(gatilhoEsq, gatilhoDir, 0.8);
        double velR2 = mathUtils.calcularR2(gatilhoEsq, gatilhoDir, 0.8);

        if (velL2 != 0) {
            drive(velL2, velL2);
            return;
        }
        if (velR2 != 0) {
            drive(-velR2, -velR2);
            return;
        }

        if (pov != -1) {
            double[] povVel = mathUtils.calcularPOV(pov, 0.6);
            drive(povVel[0], povVel[1]);
            return;
        }

        double[] analogVel = mathUtils.calcularAnalogicos(magSeno, 0.7, eixoX1, eixoY1, eixoX2, eixoY2);
        drive(analogVel[0], analogVel[1]);
    }
}