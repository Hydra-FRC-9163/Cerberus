package frc.robot.seasons.season2023.handlers;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.adl.core.ActionHandler;
import frc.robot.adl.core.ActionRequest;
import frc.robot.adl.core.RobotContextFacts;
import frc.robot.subsystems.Score.angular.AngularManager;
import frc.robot.subsystems.Score.claw.ClawManager;
import frc.robot.subsystems.Score.claw.GamePieceType;
import frc.robot.subsystems.Score.linear.LinearManager;

public final class AcquirePieceHandler implements ActionHandler {

    private final ClawManager claw;
    @SuppressWarnings("unused")
    private final LinearManager linear;
    @SuppressWarnings("unused")
    private final AngularManager angular;

    public AcquirePieceHandler(
            ClawManager claw,
            LinearManager linear,
            AngularManager angular
    ) {
        this.claw = claw;
        this.linear = linear;
        this.angular = angular;
    }

    @Override
    public Command createCommand(ActionRequest request, RobotContextFacts context) {
        GamePieceType pieceType = parsePieceType(request);

        return Commands.runOnce(() -> {
            claw.selectIntakeType(pieceType);
            claw.Intake();
        }, claw);
    }

    private GamePieceType parsePieceType(ActionRequest request) {
        Object raw = request.parameters().get("pieceType");
        if (raw == null) {
            return claw.getSelectedIntakeType();
        }
        try {
            return GamePieceType.valueOf(raw.toString().toUpperCase());
        } catch (IllegalArgumentException e) {
            return claw.getSelectedIntakeType();
        }
    }
}