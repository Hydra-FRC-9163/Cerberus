package frc.robot.adl.core;

import edu.wpi.first.networktables.BooleanSubscriber;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StringSubscriber;

public final class NetworkTablesRobotContextFactsProvider implements RobotContextFactsProvider {

    private final DoubleSubscriber batteryVoltageSub;
    private final DoubleSubscriber stressScoreSub;
    private final StringSubscriber stressLevelSub;
    private final BooleanSubscriber speedLimitedSub;

    private final BooleanSubscriber armAtSetpointSub;
    private final StringSubscriber gamePieceTypeSub;

    private final BooleanSubscriber visionHasTargetSub;
    private final BooleanSubscriber visionAlignedSub;
    private final DoubleSubscriber visionConfidenceSub;

    private final BooleanSubscriber robotMovingSub;
    private final StringSubscriber currentZoneSub;
    private final BooleanSubscriber endgameSub;

    private final DoubleSubscriber chargeStationTiltSub;
    private final BooleanSubscriber chargeStationBalancedSub;
    private final BooleanSubscriber chargeStationBalanceModeActiveSub;

    public NetworkTablesRobotContextFactsProvider() {
        var nt = NetworkTableInstance.getDefault();

        batteryVoltageSub = nt.getDoubleTopic("/Robot/BatteryVoltage").subscribe(12.0);
        stressScoreSub     = nt.getDoubleTopic("/RobotStress/stressScore").subscribe(0.0);
        stressLevelSub     = nt.getStringTopic("/RobotStress/stressLevel").subscribe("LOW");
        speedLimitedSub    = nt.getBooleanTopic("/Robot/SpeedLimited").subscribe(false);

        armAtSetpointSub  = nt.getBooleanTopic("/Mechanisms/ArmAtSetpoint").subscribe(false);
        gamePieceTypeSub  = nt.getStringTopic("/Mechanisms/GamePieceType").subscribe("NONE");

        visionHasTargetSub  = nt.getBooleanTopic("/Vision/HasTarget").subscribe(false);
        visionAlignedSub    = nt.getBooleanTopic("/Vision/Aligned").subscribe(false);
        visionConfidenceSub = nt.getDoubleTopic("/Vision/Confidence").subscribe(0.0);

        robotMovingSub  = nt.getBooleanTopic("/Drive/Moving").subscribe(false);
        currentZoneSub  = nt.getStringTopic("/Robot/CurrentZone").subscribe("unknown");
        endgameSub      = nt.getBooleanTopic("/Game/Endgame").subscribe(false);

        chargeStationTiltSub             = nt.getDoubleTopic("/ChargeStation/TiltDeg").subscribe(0.0);
        chargeStationBalancedSub         = nt.getBooleanTopic("/ChargeStation/Balanced").subscribe(false);
        chargeStationBalanceModeActiveSub = nt.getBooleanTopic("/ChargeStation/BalanceModeActive").subscribe(false);
    }

    @Override
    public RobotContextFacts build() {
        boolean visionUsable = visionHasTargetSub.get() && visionConfidenceSub.get() > 0.6;
        String pieceType = gamePieceTypeSub.get();

        return RobotContextFacts.builder()
            .put("robot.batteryVoltage", batteryVoltageSub.get())
            .put("robot.stressScore", stressScoreSub.get())
            .put("robot.stressLevel", stressLevelSub.get())
            .put("robot.speedLimited", speedLimitedSub.get())

            .put("mechanisms.armAtSetpoint", armAtSetpointSub.get())
            .put("mechanisms.gamePieceType", pieceType)
            .put("mechanisms.hasGamePiece", !"NONE".equals(pieceType))

            .put("vision.hasTarget", visionHasTargetSub.get())
            .put("vision.aligned", visionAlignedSub.get())
            .put("vision.confidence", visionConfidenceSub.get())
            .put("vision.usable", visionUsable)

            .put("drive.moving", robotMovingSub.get())
            .put("field.currentZone", currentZoneSub.get())
            .put("game.endgame", endgameSub.get())

            .put("chargeStation.tiltDeg", chargeStationTiltSub.get())
            .put("chargeStation.balanced", chargeStationBalancedSub.get())
            .put("chargeStation.balanceModeActive", chargeStationBalanceModeActiveSub.get())
            .build();
    }
}