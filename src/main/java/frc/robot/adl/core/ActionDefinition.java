package frc.robot.adl.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class ActionDefinition {
    private final ActionId id;
    private final String displayName;
    private final String category;
    private final int defaultPriority;
    private final boolean interruptible;
    private final boolean requiresVision;
    private final boolean allowedInEndgame;
    private final Set<String> requiredCapabilities;

    private ActionDefinition(Builder builder) {
        id = builder.id;
        displayName = builder.displayName;
        category = builder.category;
        defaultPriority = builder.defaultPriority;
        interruptible = builder.interruptible;
        requiresVision = builder.requiresVision;
        allowedInEndgame = builder.allowedInEndgame;
        requiredCapabilities = Collections.unmodifiableSet(new HashSet<>(builder.requiredCapabilities));
    }

    public ActionId id() { return id; }
    public String displayName() { return displayName; }
    public String category() { return category; }
    public int defaultPriority() { return defaultPriority; }
    public boolean interruptible() { return interruptible; }
    public boolean requiresVision() { return requiresVision; }
    public boolean allowedInEndgame() { return allowedInEndgame; }
    public Set<String> requiredCapabilities() { return requiredCapabilities; }

    public static Builder builder(String id) {
        return new Builder(ActionId.of(id));
    }

    public static final class Builder {
        private final ActionId id;
        private String displayName;
        private String category = "general";
        private int defaultPriority = 0;
        private boolean interruptible = true;
        private boolean requiresVision = false;
        private boolean allowedInEndgame = false;
        private final Set<String> requiredCapabilities = new HashSet<>();

        private Builder(ActionId id) {
            this.id = id;
            this.displayName = id.value();
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder defaultPriority(int defaultPriority) {
            this.defaultPriority = defaultPriority;
            return this;
        }

        public Builder interruptible(boolean interruptible) {
            this.interruptible = interruptible;
            return this;
        }

        public Builder requiresVision(boolean requiresVision) {
            this.requiresVision = requiresVision;
            return this;
        }

        public Builder allowedInEndgame(boolean allowedInEndgame) {
            this.allowedInEndgame = allowedInEndgame;
            return this;
        }

        public Builder requiresCapability(String capability) {
            requiredCapabilities.add(capability);
            return this;
        }

        public ActionDefinition build() {
            return new ActionDefinition(this);
        }
    }
}
