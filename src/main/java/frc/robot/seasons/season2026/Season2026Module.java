// package frc.robot.seasons.season2026;

// import java.util.Map;

// import frc.robot.adl.core.ActionDefinition;
// import frc.robot.adl.core.ActionId;
// import frc.robot.adl.core.ObjectiveDefinition;
// import frc.robot.adl.core.SeasonModule;
// import frc.robot.adl.core.SeasonRegistrationContext;
// import frc.robot.adl.core.ZoneDefinition;
// import frc.robot.seasons.season2026.handlers.AbortHandler;
// import frc.robot.seasons.season2026.handlers.AcquirePieceHandler;
// import frc.robot.seasons.season2026.handlers.ClimbHandler;
// import frc.robot.seasons.season2026.handlers.ScorePieceHandler;
// import frc.robot.subsystems.Score.Angular.IntakeAngleManager;
// import frc.robot.subsystems.Score.Climb.ClimberManager;
// import frc.robot.subsystems.Score.PreShooter.PreShooterManager;
// import frc.robot.subsystems.Score.Rollers.IntakeRollerManager;
// import frc.robot.subsystems.Score.Shooter.ShooterManager;
// import frc.robot.subsystems.Score.Spindexer.SpindexerManager;

// public final class Season2026Module implements SeasonModule {
//     public static final String ACQUIRE_PIECE = "acquire_piece";
//     public static final String SCORE_PIECE = "score_piece";
//     public static final String CLIMB = "climb";
//     public static final String ABORT = "abort";

//     private final IntakeAngleManager intakeAngle;
//     private final IntakeRollerManager intakeRoller;
//     private final ClimberManager climber;
//     private final PreShooterManager preShooter;
//     private final ShooterManager shooter;
//     private final SpindexerManager spindexer;

//     public Season2026Module(
//             IntakeAngleManager intakeAngle,
//             IntakeRollerManager intakeRoller,
//             ClimberManager climber,
//             PreShooterManager preShooter,
//             ShooterManager shooter,
//             SpindexerManager spindexer
//     ) {
//         this.intakeAngle = intakeAngle;
//         this.intakeRoller = intakeRoller;
//         this.climber = climber;
//         this.preShooter = preShooter;
//         this.shooter = shooter;
//         this.spindexer = spindexer;
//     }

//     @Override
//     public String seasonId() {
//         return "season2026";
//     }

//     @Override
//     public void register(SeasonRegistrationContext context) {
//         registerZones(context);
//         registerActions(context);
//         registerObjectives(context);
//         registerConstraints(context);
//     }

//     private void registerZones(SeasonRegistrationContext context) {
//         context.registerZone(new ZoneDefinition("depot", "Depot", Map.of()));
//         context.registerZone(new ZoneDefinition("hub", "Hub", Map.of("requiresVision", true)));
//         context.registerZone(new ZoneDefinition("tower", "Tower", Map.of("endgame", true)));
//         context.registerZone(new ZoneDefinition("bump", "Bump", Map.of()));
//     }

//     private void registerObjectives(SeasonRegistrationContext context) {
//         context.registerObjective(new ObjectiveDefinition(
//             "score_primary",
//             "Score primary game piece",
//             ActionId.of(SCORE_PIECE),
//             100
//         ));
//         context.registerObjective(new ObjectiveDefinition(
//             "prepare_endgame",
//             "Prepare endgame climb",
//             ActionId.of(CLIMB),
//             90
//         ));
//     }

//     private void registerConstraints(SeasonRegistrationContext context) {
//         context.registerConstraint((request, action, robotContext) -> {
//             boolean stressed = robotContext.getBoolean("robot.speedLimited", false)
//                 || robotContext.getDouble("robot.stressScore", 0.0) >= 70.0;
//             if (stressed && request.priority() < 90) {
//                 return frc.robot.adl.core.ConstraintResult.reject("Robot stress is too high");
//             }
//             return frc.robot.adl.core.ConstraintResult.allow();
//         });
//     }

//     private void registerActions(SeasonRegistrationContext context) {
//         context.registerAction(
//             ActionDefinition.builder(ACQUIRE_PIECE)
//                 .displayName("Acquire piece")
//                 .category("intake")
//                 .defaultPriority(60)
//                 .requiresCapability("intake")
//                 .build(),
//             new AcquirePieceHandler(intakeRoller, preShooter, shooter)
//         );

//         context.registerAction(
//             ActionDefinition.builder(SCORE_PIECE)
//                 .displayName("Score piece")
//                 .category("score")
//                 .defaultPriority(80)
//                 .requiresVision(true)
//                 .requiresCapability("shooter")
//                 .requiresCapability("indexer")
//                 .build(),
//             new ScorePieceHandler(intakeRoller, preShooter, shooter, spindexer)
//         );

//         context.registerAction(
//             ActionDefinition.builder(CLIMB)
//                 .displayName("Climb")
//                 .category("endgame")
//                 .defaultPriority(100)
//                 .interruptible(false)
//                 .allowedInEndgame(true)
//                 .requiresCapability("climber")
//                 .build(),
//             new ClimbHandler(intakeRoller, climber, preShooter, shooter)
//         );

//         context.registerAction(
//             ActionDefinition.builder(ABORT)
//                 .displayName("Abort")
//                 .category("safety")
//                 .defaultPriority(1000)
//                 .allowedInEndgame(true)
//                 .build(),
//             new AbortHandler(intakeAngle, intakeRoller, climber, preShooter, shooter)
//         );
//     }
// }
