package frc.robot.adl.core;

public final class ADLDecisionEngine {
    private final ActionRegistry actionRegistry;
    private final SeasonRegistrationContext season;

    public ADLDecisionEngine(ActionRegistry actionRegistry, SeasonRegistrationContext season) {
        this.actionRegistry = actionRegistry;
        this.season = season;
    }

    public ADLDecisionResult decide(ActionRequest request, RobotContextFacts context) {
        if (request == null) {
            return ADLDecisionResult.hold(null, "No action requested");
        }

        var registeredAction = actionRegistry.find(request.actionId());
        if (registeredAction.isEmpty()) {
            return ADLDecisionResult.reject(request, "Unknown action: " + request.actionId());
        }

        ActionDefinition action = registeredAction.get().definition();
        if (action.requiresVision() && !context.getBoolean("vision.usable", false)) {
            return ADLDecisionResult.hold(request, "Vision is not currently usable");
        }

        if (season.endgameRules().isEndgame(context)
                && !season.endgameRules().isAllowedInEndgame(action, request, context)) {
            return ADLDecisionResult.reject(request, "Action is not allowed during endgame");
        }

        for (ActionConstraint constraint : season.constraints()) {
            ConstraintResult result = constraint.evaluate(request, action, context);
            if (!result.allowed()) {
                return ADLDecisionResult.reject(request, result.reason());
            }
        }

        if (!registeredAction.get().handler().canHandle(request, context)) {
            return ADLDecisionResult.hold(request, "Action handler is not ready");
        }

        return ADLDecisionResult.execute(request, "Executing " + action.displayName());
    }
}
