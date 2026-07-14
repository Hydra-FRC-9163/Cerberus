package frc.robot.seasons.season2023.handlers;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.adl.core.ActionHandler;
import frc.robot.adl.core.ActionRequest;
import frc.robot.adl.core.RobotContextFacts;
import frc.robot.subsystems.Score.angular.AngularManager;
import frc.robot.subsystems.Score.linear.LinearManager;

public final class ScorePieceHandler implements ActionHandler {

    private final LinearManager linear;
    private final AngularManager angular;

    public ScorePieceHandler(LinearManager linear, AngularManager angular) {
        this.linear = linear;
        this.angular = angular;
    }

    @Override
    public Command createCommand(ActionRequest request, RobotContextFacts context) {
        // Retorna um comando de segurança padrão para garantir que os braços parem
        // de subir/descer ao executar a ação de pontuar.
        return Commands.sequence(
            Commands.runOnce(() -> linear.LinearStop(), linear),
            Commands.runOnce(() -> angular.AngularStop(), angular)
        );
    }
}