package frc.robot.adl.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class RobotContextFacts {
    private final Map<String, Object> facts;

    private RobotContextFacts(Map<String, Object> facts) {
        this.facts = Collections.unmodifiableMap(new HashMap<>(facts));
    }

    public boolean getBoolean(String key, boolean fallback) {
        Object value = facts.get(key);
        return value instanceof Boolean booleanValue ? booleanValue : fallback;
    }

    public double getDouble(String key, double fallback) {
        Object value = facts.get(key);
        return value instanceof Number number ? number.doubleValue() : fallback;
    }

    public String getString(String key, String fallback) {
        Object value = facts.get(key);
        return value instanceof String string ? string : fallback;
    }

    public Map<String, Object> asMap() {
        return facts;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Map<String, Object> facts = new HashMap<>();

        public Builder put(String key, Object value) {
            facts.put(key, value);
            return this;
        }

        public RobotContextFacts build() {
            return new RobotContextFacts(facts);
        }
    }
}
