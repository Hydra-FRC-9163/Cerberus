package frc.robot.adl.core;

import java.util.Objects;

public final class ActionId {
    private final String value;

    private ActionId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Action id cannot be blank");
        }
        this.value = value;
    }

    public static ActionId of(String value) {
        return new ActionId(value);
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ActionId actionId && value.equals(actionId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
