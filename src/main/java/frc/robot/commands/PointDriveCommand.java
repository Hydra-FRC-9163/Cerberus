package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.utils.Constants;
import frc.robot.subsystems.Score.claw.ClawManager;
import frc.robot.subsystems.Score.angular.AngularManager;
import frc.robot.subsystems.Sensors.ThroughBore.ThroughBoreManager;
import frc.robot.subsystems.Sensors.Limelight.LimelightManager;
import edu.wpi.first.wpilibj.PS5Controller;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PointDriveCommand extends Command {

    private final AngularManager angular;
    private final ClawManager claw;
    private final ThroughBoreManager throughBore;
    private final PS5Controller PS5;
    private final LimelightManager limelight;

    private boolean isMovingToTarget = false;
    private double targetPosition = 0;
    private static final double posInit = 0;  
    private static final double posFinal = 0;
    private static final double toleranciamin = 0.2;  
    private static final double toleranciamax = 0.95;
    private static final double maxoutput = 0.7;
    private static final double manualMove = 0.5;
    private static final int circleButton = 0;

    public PointDriveCommand(AngularManager angular, ClawManager claw, ThroughBoreManager throughBore, PS5Controller PS5, LimelightManager limelight) {
        this.angular = angular;
        this.claw = claw;
        this.throughBore = throughBore;
        this.PS5 = PS5;
        this.limelight = limelight;
        addRequirements(angular, claw);
    }

    @Override
    public void initialize() {
        SmartDashboard.putString("Angular/Status", "Pronto!");
        SmartDashboard.putString("Claw/Status", "Pronto!");
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        angular.AngularStop();
        claw.stopClawMotor();
    }
}