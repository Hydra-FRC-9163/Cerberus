package frc.robot.adl.core;

public interface ActionConstraint {
    ConstraintResult evaluate(ActionRequest request, ActionDefinition action, RobotContextFacts context);
}
