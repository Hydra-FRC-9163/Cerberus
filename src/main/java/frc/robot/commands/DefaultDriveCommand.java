package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.utils.Constants;
import frc.robot.utils.MathUtils;
import frc.robot.subsystem.Drivetrain;
import frc.robot.subsystem.SensorE3;
import frc.robot.subsystem.Arm;

public class DefaultDriveCommand extends Command {

    private final Drivetrain drivetrain;
    private final SensorE3 infrav;
    private final Joystick controle;
    private final Arm arm;

    private int POV;
    private boolean botaoA, botaoB, botaoX, R1, L1;
    private double speed, velEsq, velDir, L2, R2, eixoX1, eixoX2, eixoY1, eixoY2;


    private final MathUtils math = new MathUtils();

    public DefaultDriveCommand(Drivetrain drive, SensorE3 infrav, Joystick controle, Arm arm) {
        this.drivetrain = drive;
        this.infrav = infrav;
        this.controle = controle;
        this.arm = arm;
        addRequirements(drivetrain, infrav, arm);
    }

    @Override
    public void initialize() {
        speed = 0.5;
        L2 = R2 = velEsq = velDir = 0;
    }

    @Override
    public void execute() {
        lerControles();
        speedControl();
        moveArm();
        atualizarSmartDashboard();
    
        if (infrav.isObstacleDetected()) {
        drivetrain.stop();
        return;
        }
    
        if (POV != -1) {
            double[] velocidades = math.calcularPOV(POV, speed);
            velEsq = velocidades[0];
            velDir = velocidades[1];
        }
        else if (math.calcularL2(L2, R2, speed) != 0) {
            double v = math.calcularL2(L2, R2, speed);
            velEsq = v;
            velDir = v;
        }
        else if (math.calcularR2(L2, R2, speed) != 0) {
            double v = math.calcularR2(L2, R2, speed);
            velEsq = -v;
            velDir = -v;
        }
        else {
            double[] magSeno = math.calcularMagESeno(
                eixoX1, -eixoX2, 
                eixoY1, eixoY2  
            );
            double[] velocidades = math.calcularAnalogicos(magSeno, speed, eixoX1, eixoY1, -eixoX2, eixoY2);
            velEsq = velocidades[0];
            velDir = velocidades[1];
        }
    
        drivetrain.drive(velEsq, velDir);
    }

    @Override
    public void end(boolean interrompido) {
        drivetrain.stop();
        arm.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    private void lerControles() {
        botaoA = controle.getRawButton(Constants.Drivetrain.botaoA);
        botaoB = controle.getRawButton(Constants.Drivetrain.botaoB);
        botaoX = controle.getRawButton(Constants.Drivetrain.botaoX);

        eixoX1 = controle.getRawAxis(Constants.Drivetrain.eixoX);
        eixoY1 = -controle.getRawAxis(Constants.Drivetrain.eixoY);
        eixoX2 = controle.getRawAxis(Constants.Drivetrain.eixoX2);
        eixoY2 = -controle.getRawAxis(Constants.Drivetrain.eixoY2);

        R1 = controle.getRawButton(Constants.Drivetrain.R1);
        L1 = controle.getRawButton(Constants.Drivetrain.L1);
        L2 = controle.getRawAxis(Constants.Drivetrain.L2);
        R2 = controle.getRawAxis(Constants.Drivetrain.R2);

        POV = controle.getPOV();
    }

    private void speedControl() {
        if (botaoA) speed = 0.25;
        else if (botaoB) speed = 0.5;
        else if (botaoX) speed = 1;
    }

    private void moveArm() {
        if (R1) {
            arm.moveArm(0.2);
        } else if (L1) {
            arm.moveArm(-0.2);
        } else {
            arm.stop();
        }
    }

    private void atualizarSmartDashboard() {
        SmartDashboard.putNumber("Velocidade", speed);
        SmartDashboard.putNumber("POV", POV);
        SmartDashboard.putBoolean("R1", R1);
        SmartDashboard.putBoolean("L1", L1);
        SmartDashboard.putNumber("L2", L2);
        SmartDashboard.putNumber("R2", R2);
        SmartDashboard.putBoolean("Botão A", botaoA);
        SmartDashboard.putBoolean("Botão B", botaoB);
        SmartDashboard.putBoolean("Botão X", botaoX);
        SmartDashboard.putNumber("Eixo X", eixoX1);
        SmartDashboard.putNumber("Eixo Y", eixoY1);
        SmartDashboard.putNumber("Eixo X2", eixoX2);
        SmartDashboard.putNumber("Eixo Y2", eixoY2);
        SmartDashboard.putNumber("Velocidade Esquerda", velEsq);
        SmartDashboard.putNumber("Velocidade Direita", velDir);
    }
}