package frc.robot.adl;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StringSubscriber;

public class HumanIntentSource {

    private final StringSubscriber intentSub;
    private String lastCommand = "";

    public HumanIntentSource() {
        intentSub = NetworkTableInstance.getDefault()
            .getStringTopic("/ADL/intent")
            .subscribe("");
    }

    public HumanIntent pollIntent() {
        String cmd = intentSub.get();
        if (cmd.isEmpty() || cmd.equals(lastCommand)) return null;
        lastCommand = cmd;

        switch (cmd) {
            case "ACQUIRE_PIECE": return HumanIntent.acquirePiece(HumanIntent.GameZone.OUTPOST, true);
            case "SCORE_PIECE":         return HumanIntent.scorePiece(HumanIntent.GameZone.GRIDS, true);
            case "MOVE_DEPOT":    return HumanIntent.moveTo(HumanIntent.GameZone.PIECES);
            case "MOVE_STATION":     return HumanIntent.moveTo(HumanIntent.GameZone.STATION);
            case "BALANCE":         return HumanIntent.balance();
            case "ABORT":         return HumanIntent.abort();
            default:              return null;
        }
    }
}