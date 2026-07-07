package frc.robot.adl.core;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class ObjectiveManager {
    private final Map<String, ObjectiveDefinition> objectives;

    public ObjectiveManager(Map<String, ObjectiveDefinition> objectives) {
        this.objectives = Map.copyOf(objectives);
    }

    public Optional<ObjectiveDefinition> find(String objectiveId) {
        return Optional.ofNullable(objectives.get(objectiveId));
    }

    public List<ObjectiveDefinition> orderedByPriority() {
        return objectives.values().stream()
            .sorted(Comparator.comparingInt(ObjectiveDefinition::priority).reversed())
            .toList();
    }

    public Optional<ObjectiveDefinition> highestPriority() {
        return orderedByPriority().stream().findFirst();
    }
}
