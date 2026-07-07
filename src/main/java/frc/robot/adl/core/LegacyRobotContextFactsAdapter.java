package frc.robot.adl.core;

import frc.robot.adl.RobotContext;

public final class LegacyRobotContextFactsAdapter {
    private LegacyRobotContextFactsAdapter() {}

    public static RobotContextFacts from(RobotContext context) {
        return RobotContextFacts.builder()
            .put("robot.batteryVoltage", context.batteryVoltage)
            .put("robot.stressScore", context.stressScore)
            .put("robot.stressLevel", context.stressLevel)
            .put("robot.speedLimited", context.speedLimited)
            .put("mechanisms.hasGamePiece", context.hasGamePiece)
            .put("mechanisms.intakeActive", context.intakeActive)
            .put("mechanisms.shooterReady", context.shooterReady)
            .put("mechanisms.climbAvailable", context.climbAvailable)
            .put("vision.hasTarget", context.visionHasTarget)
            .put("vision.aligned", context.visionAligned)
            .put("vision.confidence", context.visionConfidence)
            .put("vision.usable", context.canUseVision())
            .put("drive.moving", context.robotMoving)
            .put("field.currentZone", context.currentZone.name().toLowerCase())
            .put("game.endgame", context.endgame)
            .build();
    }
}
