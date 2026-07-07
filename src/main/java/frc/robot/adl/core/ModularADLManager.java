package frc.robot.adl.core;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StringPublisher;
import edu.wpi.first.wpilibj.Timer;

public final class ModularADLManager {
    private final ActionIntentSource intentSource;
    private final ADLDecisionEngine decisionEngine;
    private final ActionExecutionService executionService;
    private final RobotContextFactsProvider contextProvider;
    private final double minDecisionIntervalSeconds;
    private final StringPublisher decisionPub;
    private double lastDecisionTime = 0.0;
    private ADLDecisionResult lastDecision;

    public ModularADLManager(
            ActionIntentSource intentSource,
            RobotContextFactsProvider contextProvider,
            ADLDecisionEngine decisionEngine,
            ActionExecutionService executionService,
            double minDecisionIntervalSeconds
    ) {
        this.intentSource = intentSource;
        this.contextProvider = contextProvider;
        this.decisionEngine = decisionEngine;
        this.executionService = executionService;
        this.minDecisionIntervalSeconds = minDecisionIntervalSeconds;
        decisionPub = NetworkTableInstance.getDefault()
            .getStringTopic("/ADL/decision")
            .publish();
    }

    public void periodic() {
        ActionRequest request = intentSource.pollIntent();
        if (request == null) {
            return;
        }

        double now = Timer.getFPGATimestamp();
        if (now - lastDecisionTime < minDecisionIntervalSeconds) {
            return;
        }

        RobotContextFacts context = contextProvider.build();
        lastDecision = decisionEngine.decide(request, context);
        lastDecisionTime = now;

        if (lastDecision.status() == DecisionStatus.EXECUTE) {
            executionService.execute(request, context);
        }
        decisionPub.set(lastDecision.status() + ": " + lastDecision.reason());
    }

    public ADLDecisionResult getLastDecision() {
        return lastDecision;
    }
}
