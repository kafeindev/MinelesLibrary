package net.mineles.library.menu.action;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class ClickActionCollection {
    private static final ClickActionCollection DEFAULTS;

    static {
        DEFAULTS = ClickActionCollection.newBuilder()
                .registerAction("CLOSE", ClickActionDefaults.CLOSE)
                .registerAction("OPEN", ClickActionDefaults.OPEN)
                .registerAction("CONSOLE", ClickActionDefaults.COMMAND_CONSOLE)
                .registerAction("PLAYER", ClickActionDefaults.COMMAND_PLAYER)
                .registerAction("SERVER", ClickActionDefaults.SERVER)
                .build();
    }

    private final @NotNull Map<String, ClickAction> actions;

    public ClickActionCollection() {
        this(ClickActionCollection.DEFAULTS);
    }

    public ClickActionCollection(@NotNull ClickActionCollection collection) {
        this(collection.getActions());
    }

    public ClickActionCollection(@NotNull Map<String, ClickAction> actions) {
        this.actions = actions;
    }

    public @NotNull Map<String, ClickAction> getActions() {
        return this.actions;
    }

    public @NotNull Set<String> getActionNames() {
        return this.actions.keySet();
    }

    public @NotNull Optional<ClickAction> findAction(@NotNull String key) {
        return Optional.ofNullable(this.actions.get(key));
    }

    public @NotNull Optional<ClickAction> findAction(@NotNull RegisteredClickAction action) {
        return this.findAction(action.getKey());
    }

    public void registerAction(@NotNull String key, @NotNull ClickAction action) {
        this.actions.put(key, action);
    }

    public void unregisterAction(@NotNull String key) {
        this.actions.remove(key);
    }

    public void unregisterAction(@NotNull ClickAction action) {
        this.actions.values().remove(action);
    }

    public void unregisterAll() {
        this.actions.clear();
    }

    public boolean isRegistered(@NotNull String key) {
        return this.actions.containsKey(key);
    }

    public boolean isRegistered(@NotNull ClickAction action) {
        return this.actions.containsValue(action);
    }

    public boolean isEmpty() {
        return this.actions.isEmpty();
    }

    public int size() {
        return this.actions.size();
    }

    public @NotNull Builder toBuilder() {
        return new Builder(this);
    }

    @NotNull
    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private final @NotNull ClickActionCollection collection;

        public Builder() {
            this.collection = new ClickActionCollection(Maps.newHashMap());
        }

        public Builder(@NotNull ClickActionCollection collection) {
            this.collection = new ClickActionCollection(collection);
        }

        public @NotNull Builder registerAction(@NotNull String key, @NotNull ClickAction action) {
            this.collection.registerAction(key, action);
            return this;
        }

        public @NotNull Builder unregisterAction(@NotNull String key) {
            this.collection.unregisterAction(key);
            return this;
        }

        public @NotNull Builder unregisterAction(@NotNull ClickAction action) {
            this.collection.unregisterAction(action);
            return this;
        }

        public @NotNull Builder unregisterAll() {
            this.collection.unregisterAll();
            return this;
        }

        public @NotNull ClickActionCollection build() {
            return this.collection;
        }
    }
}
