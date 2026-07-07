package frc.robot.adl.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StringSubscriber;

public final class NetworkTablesActionIntentSource implements ActionIntentSource {
    private final StringSubscriber actionSub;
    private final StringSubscriber zoneSub;
    private final StringSubscriber parametersSub;
    private final StringSubscriber legacyIntentSub;
    private final Map<String, LegacyIntentMapping> legacyMappings;
    private String lastAction = "";
    private String lastLegacyIntent = "";

    public NetworkTablesActionIntentSource() {
        this(Map.of());
    }

    public NetworkTablesActionIntentSource(Map<String, LegacyIntentMapping> legacyMappings) {
        var nt = NetworkTableInstance.getDefault();
        actionSub = nt.getStringTopic("/ADL/intent/actionId").subscribe("");
        zoneSub = nt.getStringTopic("/ADL/intent/zoneId").subscribe("unknown");
        parametersSub = nt.getStringTopic("/ADL/intent/parameters").subscribe("");
        legacyIntentSub = nt.getStringTopic("/ADL/intent").subscribe("");
        this.legacyMappings = Collections.unmodifiableMap(new HashMap<>(legacyMappings));
    }

    @Override
    public ActionRequest pollIntent() {
        String actionId = actionSub.get();
        if (!actionId.isBlank() && !actionId.equals(lastAction)) {
            lastAction = actionId;
            return buildRequest(actionId, zoneSub.get(), 0, false);
        }

        String legacyIntent = legacyIntentSub.get();
        if (legacyIntent.isBlank() || legacyIntent.equals(lastLegacyIntent)) {
            return null;
        }
        lastLegacyIntent = legacyIntent;

        LegacyIntentMapping mapping = legacyMappings.get(legacyIntent);
        if (mapping == null) {
            return null;
        }

        return buildRequest(
            mapping.actionId(),
            mapping.zoneId(),
            mapping.priority(),
            mapping.preemptCurrent()
        );
    }

    private ActionRequest buildRequest(
            String actionId,
            String zoneId,
            int priority,
            boolean preemptCurrent
    ) {
        ActionRequest.Builder builder = ActionRequest.builder(actionId)
            .targetZone(zoneId)
            .priority(priority)
            .preemptCurrent(preemptCurrent);

        for (String parameter : parametersSub.get().split(";")) {
            String[] parts = parameter.split("=", 2);
            if (parts.length == 2 && !parts[0].isBlank()) {
                builder.parameter(parts[0].trim(), parts[1].trim());
            }
        }

        return builder.build();
    }

    public record LegacyIntentMapping(
            String actionId,
            String zoneId,
            int priority,
            boolean preemptCurrent
    ) {}
}
