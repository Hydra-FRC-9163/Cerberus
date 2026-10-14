package frc.robot.commands;

import edu.wpi.first.wpilibj.PS5Controller;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import frc.robot.utils.MathUtils;
import frc.robot.subsystems.Drivetrain.*;

public class DefaultDriveCommand extends Command {

    private final Drivetrain drivetrain;
    private final PS5Controller controller;

    private int POV;
    private boolean botaoA, botaoB, botaoX, R1, L1, differentialMode;
    private double speed, velEsq, velDir, L2, R2, eixoX1, eixoX2, eixoY1, eixoY2;


    private final MathUtils math = new MathUtils();

    public DefaultDriveCommand(Drivetrain drive, PS5Controller controller2) {
        this.drivetrain = drive;
        this.controller = controller2;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        speed = 0.5;
        L2 = R2 = velEsq = velDir = 0;
        differentialMode = false;
    }

    @Override
    public void execute() {
        lerControles();
        speedControl();
        atualizarSmartDashboard();

        if (useSimDashboardDrive()) {
            drivetrain.drive(velEsq, velDir);
            return;
        }
    
        if (differentialMode) { 
            drivetrain.diffDrive(eixoY1, eixoX2, speed);
        } else {
            double[] magSeno = math.calcularMagESeno(
                eixoX1, -eixoX2, 
                eixoY1, eixoY2  
            );
            double[] velocidades = math.calcularAnalogicos(magSeno, speed, eixoX1, eixoY1, -eixoX2, eixoY2);
            velEsq = velocidades[0];
            velDir = velocidades[1];

            drivetrain.drive(velEsq, velDir);
        }
    
        SmartDashboard.putBoolean("Drive/CommandRunning", true);
    }

    @Override
    public void end(boolean interrompido) {
        drivetrain.stop();
        SmartDashboard.putBoolean("Drive/CommandRunning", false);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    private void lerControles() {
        botaoA = controller.getTriangleButton();
        botaoB = controller.getCrossButton();
        botaoX = controller.getSquareButton();

        eixoX1 = controller.getLeftX();
        eixoY1 = -controller.getLeftY();
        eixoX2 = controller.getRightX();
        eixoY2 = -controller.getRightY();

        R1 = controller.getR1Button();
        L1 = controller.getL1Button();
        L2 = controller.getL2Axis();
        R2 = controller.getR2Axis();

        POV = controller.getPOV();
    }

    private void speedControl() {
        if (botaoA) speed = 0.25;
        else if (botaoB) speed = 0.5;
        else if (botaoX) speed = 1;
    }

    public void toggleDriveMode() {
        differentialMode = !differentialMode;
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

    private boolean useSimDashboardDrive() {
        if (!RobotBase.isSimulation()) {
            return false;
        }

        double simForward = SmartDashboard.getNumber("SimDrive/Forward", 0.0);
        double simTurn = SmartDashboard.getNumber("SimDrive/Turn", 0.0);
        double simSpeed = SmartDashboard.getNumber("SimDrive/Speed", speed);

        if (Math.abs(simForward) < 0.02 && Math.abs(simTurn) < 0.02) {
            return false;
        }

        velEsq = clamp((simForward + simTurn) * simSpeed);
        velDir = clamp((simForward - simTurn) * simSpeed);
        SmartDashboard.putBoolean("Drive/CommandRunning", true);
        SmartDashboard.putNumber("Velocidade Esquerda", velEsq);
        SmartDashboard.putNumber("Velocidade Direita", velDir);
        return true;
    }

    private double clamp(double value) {
        return Math.max(-1.0, Math.min(1.0, value));
    }
}
