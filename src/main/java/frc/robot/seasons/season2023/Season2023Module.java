package frc.robot.seasons.season2023;

import java.util.Map;

import frc.robot.adl.core.ActionDefinition;
import frc.robot.adl.core.SeasonModule;
import frc.robot.adl.core.SeasonRegistrationContext;
import frc.robot.adl.core.ZoneDefinition;
import frc.robot.seasons.season2023.handlers.AbortHandler;
import frc.robot.subsystems.Score.angular.AngularManager;
import frc.robot.subsystems.Score.claw.ClawManager;
import frc.robot.subsystems.Score.linear.LinearManager;

public final class Season2023Module implements SeasonModule {
    public static final String ACQUIRE_PIECE = "acquire_piece";
    public static final String SCORE_PIECE = "score_piece";
    public static final String CLIMB = "climb";
    public static final String ABORT = "abort";

    private final ClawManager claw;
    private final LinearManager linear;
    private final AngularManager angular;

    public Season2023Module(
            ClawManager claw,
            LinearManager linear,
            AngularManager angular
    ) {
        this.claw = claw;
        this.linear = linear;
        this.angular = angular;
    }

    @Override
    public String seasonId() {
        return "season2023";
    }

    @Override
    public void register(SeasonRegistrationContext context) {
        registerZones(context);
        registerActions(context);
        registerConstraints(context);
    }

    private void registerZones(SeasonRegistrationContext context) {
        context.registerZone(new ZoneDefinition("pieces", "Pieces", Map.of()));
        context.registerZone(new ZoneDefinition("human", "Human", Map.of("requiresVision", true)));
        context.registerZone(new ZoneDefinition("station", "Station", Map.of("endgame", true)));
        context.registerZone(new ZoneDefinition("barrier", "Barrier", Map.of()));
    }

    private void registerConstraints(SeasonRegistrationContext context) {
        context.registerConstraint((request, action, robotContext) -> {
            boolean stressed = robotContext.getBoolean("robot.speedLimited", false)
                || robotContext.getDouble("robot.stressScore", 0.0) >= 70.0;
            if (stressed && request.priority() < 90) {
                return frc.robot.adl.core.ConstraintResult.reject("Robot stress is too high");
            }
            return frc.robot.adl.core.ConstraintResult.allow();
        });
    }

    private void registerActions(SeasonRegistrationContext context) {

        context.registerAction(
            ActionDefinition.builder(ABORT)
                .displayName("Abort")
                .category("safety")
                .defaultPriority(1000)
                .allowedInEndgame(true)
                .build(),
            new AbortHandler(claw, linear, angular)
        );
    }
}
