package frc.robot.adl.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class SeasonRegistrationContext {
    private final ActionRegistry actionRegistry;
    private final Map<String, ZoneDefinition> zones = new LinkedHashMap<>();
    private final Map<String, ObjectiveDefinition> objectives = new LinkedHashMap<>();
    private final List<ActionConstraint> constraints = new ArrayList<>();
    private EndgameRules endgameRules = EndgameRules.allowRegisteredEndgameActions();

    public SeasonRegistrationContext(ActionRegistry actionRegistry) {
        this.actionRegistry = actionRegistry;
    }

    public void registerAction(ActionDefinition definition, ActionHandler handler) {
        actionRegistry.register(definition, handler);
    }

    public void registerZone(ZoneDefinition zone) {
        if (zone == null) {
            throw new IllegalArgumentException("Zone definition is required");
        }
        if (zones.containsKey(zone.id())) {
            throw new IllegalArgumentException("Duplicate zone id registered: " + zone.id());
        }
        zones.put(zone.id(), zone);
    }

    public void registerObjective(ObjectiveDefinition objective) {
        if (objective == null) {
            throw new IllegalArgumentException("Objective definition is required");
        }
        if (objectives.containsKey(objective.id())) {
            throw new IllegalArgumentException("Duplicate objective id registered: " + objective.id());
        }
        if (!actionRegistry.contains(objective.preferredActionId())) {
            throw new IllegalArgumentException(
                "Objective " + objective.id() + " references unregistered action "
                    + objective.preferredActionId()
            );
        }
        objectives.put(objective.id(), objective);
    }

    public void registerConstraint(ActionConstraint constraint) {
        if (constraint == null) {
            throw new IllegalArgumentException("Action constraint is required");
        }
        constraints.add(constraint);
    }

    public void setEndgameRules(EndgameRules endgameRules) {
        if (endgameRules == null) {
            throw new IllegalArgumentException("Endgame rules are required");
        }
        this.endgameRules = endgameRules;
    }

    public ActionRegistry actionRegistry() { return actionRegistry; }
    public Map<String, ZoneDefinition> zones() { return Collections.unmodifiableMap(zones); }
    public Map<String, ObjectiveDefinition> objectives() { return Collections.unmodifiableMap(objectives); }
    public List<ActionConstraint> constraints() { return Collections.unmodifiableList(constraints); }
    public EndgameRules endgameRules() { return endgameRules; }
}
