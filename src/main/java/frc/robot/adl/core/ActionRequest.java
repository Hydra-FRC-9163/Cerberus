package frc.robot.adl.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.Timer;

public final class ActionRequest {
    private final ActionId actionId;
    private final String targetZoneId;
    private final int priority;
    private final boolean preemptCurrent;
    private final double timestamp;
    private final Map<String, Object> parameters;

    private ActionRequest(Builder builder) {
        actionId = builder.actionId;
        targetZoneId = builder.targetZoneId;
        priority = builder.priority;
        preemptCurrent = builder.preemptCurrent;
        timestamp = Timer.getFPGATimestamp();
        parameters = Collections.unmodifiableMap(new HashMap<>(builder.parameters));
    }

    public ActionId actionId() { return actionId; }
    public String targetZoneId() { return targetZoneId; }
    public int priority() { return priority; }
    public boolean preemptCurrent() { return preemptCurrent; }
    public double timestamp() { return timestamp; }
    public Map<String, Object> parameters() { return parameters; }

    public static Builder builder(String actionId) {
        return new Builder(ActionId.of(actionId));
    }

    public static final class Builder {
        private final ActionId actionId;
        private String targetZoneId = "unknown";
        private int priority = 0;
        private boolean preemptCurrent = false;
        private final Map<String, Object> parameters = new HashMap<>();

        private Builder(ActionId actionId) {
            this.actionId = actionId;
        }

        public Builder targetZone(String targetZoneId) {
            this.targetZoneId = targetZoneId;
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder preemptCurrent(boolean preemptCurrent) {
            this.preemptCurrent = preemptCurrent;
            return this;
        }

        public Builder parameter(String key, Object value) {
            parameters.put(key, value);
            return this;
        }

        public ActionRequest build() {
            return new ActionRequest(this);
        }
    }
}
