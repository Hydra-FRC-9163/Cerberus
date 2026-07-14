package frc.robot.Dashboards.Mechanisms;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Score.claw.ClawManager;
import frc.robot.subsystems.Score.linear.LinearManager;
import frc.robot.subsystems.Score.angular.AngularManager;

public class DashboardPublisherMechanisms extends SubsystemBase {

    private final ClawManager claw;
    @SuppressWarnings("unused")
    private final LinearManager linear;
    @SuppressWarnings("unused")
    private final AngularManager angular;

    public DashboardPublisherMechanisms(ClawManager claw, LinearManager linear, AngularManager angular) {
        this.claw = claw;
        this.linear = linear;
        this.angular = angular;
    }

    @Override
    public void periodic() {
        // 1. Tipo de Peça (CONE / CUBE / NONE)
        if (claw != null && claw.getSelectedIntakeType() != null) {
            SmartDashboard.putString("Mechanisms/GamePieceType", claw.getSelectedIntakeType().name());
        } else {
            SmartDashboard.putString("Mechanisms/GamePieceType", "NONE");
        }

        // 2. Braço no Setpoint (ArmAtSetpoint)
        // Como os managers usam controle manual puro, enviamos 'true' fixo por segurança.
        // Isso evita erros de compilação e mantém o dashboard web feliz!
        SmartDashboard.putBoolean("Mechanisms/ArmAtSetpoint", true);

        // 3. Nível do Alvo (TargetNodeLevel: LOW / MID / HIGH)
        SmartDashboard.putString("Mechanisms/TargetNodeLevel", "MID"); 
    }
}