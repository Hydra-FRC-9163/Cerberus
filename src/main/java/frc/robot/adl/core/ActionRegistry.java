package frc.robot.adl.core;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class ActionRegistry {
    private final Map<ActionId, RegisteredAction> actions = new LinkedHashMap<>();

    public void register(ActionDefinition definition, ActionHandler handler) {
        if (definition == null || handler == null) {
            throw new IllegalArgumentException("Action definition and handler are required");
        }
        if (actions.containsKey(definition.id())) {
            throw new IllegalArgumentException("Duplicate action id registered: " + definition.id());
        }
        actions.put(definition.id(), new RegisteredAction(definition, handler));
    }

    public Optional<RegisteredAction> find(ActionId actionId) {
        return Optional.ofNullable(actions.get(actionId));
    }

    public boolean contains(ActionId actionId) {
        return actions.containsKey(actionId);
    }

    public Collection<RegisteredAction> all() {
        return actions.values();
    }
}
