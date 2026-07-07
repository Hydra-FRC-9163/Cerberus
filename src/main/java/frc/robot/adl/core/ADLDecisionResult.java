package frc.robot.adl.core;

public record ADLDecisionResult(DecisionStatus status, ActionRequest request, String reason) {
    public static ADLDecisionResult execute(ActionRequest request, String reason) {
        return new ADLDecisionResult(DecisionStatus.EXECUTE, request, reason);
    }

    public static ADLDecisionResult hold(ActionRequest request, String reason) {
        return new ADLDecisionResult(DecisionStatus.HOLD, request, reason);
    }

    public static ADLDecisionResult reject(ActionRequest request, String reason) {
        return new ADLDecisionResult(DecisionStatus.REJECT, request, reason);
    }
}
