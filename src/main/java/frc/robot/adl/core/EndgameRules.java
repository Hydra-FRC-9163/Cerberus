package frc.robot.adl.core;

public interface EndgameRules {
    boolean isEndgame(RobotContextFacts context);

    boolean isAllowedInEndgame(ActionDefinition action, ActionRequest request, RobotContextFacts context);

    static EndgameRules allowRegisteredEndgameActions() {
        return new EndgameRules() {
            @Override
            public boolean isEndgame(RobotContextFacts context) {
                return context.getBoolean("game.endgame", false);
            }

            @Override
            public boolean isAllowedInEndgame(
                    ActionDefinition action,
                    ActionRequest request,
                    RobotContextFacts context
            ) {
                return action.allowedInEndgame();
            }
        };
    }
}
