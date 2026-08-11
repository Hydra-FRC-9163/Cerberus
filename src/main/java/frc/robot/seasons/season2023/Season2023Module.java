package frc.robot.seasons.season2023;

import java.util.Map;

import frc.robot.adl.core.ActionDefinition;
import frc.robot.adl.core.ActionId;
import frc.robot.adl.core.ObjectiveDefinition;
import frc.robot.adl.core.SeasonModule;
import frc.robot.adl.core.SeasonRegistrationContext;
import frc.robot.adl.core.ZoneDefinition;
import frc.robot.seasons.season2023.handlers.AbortHandler;
import frc.robot.seasons.season2023.handlers.AcquirePieceHandler;
import frc.robot.seasons.season2023.handlers.BalanceHandler;
import frc.robot.seasons.season2023.handlers.ScorePieceHandler;
import frc.robot.subsystems.Drivetrain.ChargeStationBalancer;
import frc.robot.subsystems.Drivetrain.Drivetrain;
import frc.robot.subsystems.Score.angular.AngularManager;
import frc.robot.subsystems.Score.claw.ClawManager;

public final class Season2023Module implements SeasonModule {
    public static final String ACQUIRE_PIECE = "acquire_piece";
    public static final String SCORE_PIECE = "score_piece";
    // Charged Up 2023 nao tem climb - renomeado de CLIMB para BALANCE
    // (charge station). Mantem o mesmo valor de string usado pelo dashboard
    // apos a Fase 1 ("/ADL/state" publica "BALANCING", intent "BALANCE").
    public static final String BALANCE = "balance";
    public static final String ABORT = "abort";

    private final ClawManager claw;
    private final AngularManager angular;
    private final ChargeStationBalancer chargeStationBalancer;
    private final Drivetrain drivetrain;

    public Season2023Module(
            ClawManager claw,
            AngularManager angular,
            ChargeStationBalancer chargeStationBalancer,
            Drivetrain drivetrain
    ) {
        this.claw = claw;
        this.angular = angular;
        this.chargeStationBalancer = chargeStationBalancer;
        this.drivetrain = drivetrain;
    }

    @Override
    public String seasonId() {
        return "season2023";
    }

    @Override
    public void register(SeasonRegistrationContext context) {
        registerZones(context);
        registerActions(context);
        registerObjectives(context);
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
                .interruptible(false)
                .allowedInEndgame(true)
                .build(),
            new AbortHandler(claw, angular, chargeStationBalancer, drivetrain)
        );

        context.registerAction(
            ActionDefinition.builder(ACQUIRE_PIECE)
                .displayName("Acquire Piece")
                .category("intake")
                .defaultPriority(5)
                .interruptible(true)
                .allowedInEndgame(false)
                .build(),
            new AcquirePieceHandler(claw, angular)
        );

        context.registerAction(
            ActionDefinition.builder(SCORE_PIECE)
                .displayName("Score Piece")
                .category("score")
                .defaultPriority(8)
                .interruptible(true)
                .allowedInEndgame(false)
                .build(),
            new ScorePieceHandler(claw, angular)
        );

        context.registerAction(
            ActionDefinition.builder(BALANCE)
                .displayName("Balance Charge Station")
                .category("endgame")
                .defaultPriority(10)
                .interruptible(true)
                .allowedInEndgame(true)
                .requiresCapability("drivetrain")
                .build(),
            new BalanceHandler(chargeStationBalancer, drivetrain)
        );
    }

    private void registerObjectives(SeasonRegistrationContext context) {
        context.registerObjective(new ObjectiveDefinition(
            "collect_game_piece", "Collect Game Piece", ActionId.of(ACQUIRE_PIECE), 5
        ));
        context.registerObjective(new ObjectiveDefinition(
            "score_game_piece", "Score Game Piece", ActionId.of(SCORE_PIECE), 8
        ));
        context.registerObjective(new ObjectiveDefinition(
            "balance_charge_station", "Balance Charge Station", ActionId.of(BALANCE), 10
        ));
        context.registerObjective(new ObjectiveDefinition(
            "abort_current_action", "Abort Current Action", ActionId.of(ABORT), 1000
        ));
    }
}
