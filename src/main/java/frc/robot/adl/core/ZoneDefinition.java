package frc.robot.adl.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public record ZoneDefinition(String id, String displayName, Map<String, Object> properties) {
    public ZoneDefinition {
        properties = Collections.unmodifiableMap(new HashMap<>(properties));
    }
}
