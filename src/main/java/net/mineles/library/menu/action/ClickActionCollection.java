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
                .registerAction("CLOSE", DefaultClickActions.CLOSE)
                .registerAction("OPEN", DefaultClickActions.OPEN)
                .registerAction("CONSOLE", DefaultClickActions.COMMAND_CONSOLE)
                .registerAction("PLAYER", DefaultClickActions.COMMAND_PLAYER)
                .registerAction("CHAT", DefaultClickActions.CHAT)
                .registerAction("MESSAGE", DefaultClickActions.CHAT)
                .registerAction("ACTIONBAR", DefaultClickActions.CHAT)
                .registerAction("ACTION_BAR", DefaultClickActions.ACTIONBAR)
                .registerAction("SERVER", DefaultClickActions.SERVER)
                .build();
    }

    private final Map<String, ClickAction> actions;

    public ClickActionCollection() {
        this(ClickActionCollection.DEFAULTS);
    }

    public ClickActionCollection(ClickActionCollection collection) {
        this(collection.getActions());
    }

    public ClickActionCollection(Map<String, ClickAction> actions) {
        this.actions = actions;
    }

    public Map<String, ClickAction> getActions() {
        return this.actions;
    }

    public Set<String> getActionNames() {
        return this.actions.keySet();
    }

    public Optional<ClickAction> findAction(@NotNull String key) {
        return Optional.ofNullable(this.actions.get(key));
    }

    public Optional<ClickAction> findAction(@NotNull RegisteredClickAction action) {
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

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private final ClickActionCollection collection;

        public Builder() {
            this.collection = new ClickActionCollection(Maps.newHashMap());
        }

        public Builder(ClickActionCollection collection) {
            this.collection = new ClickActionCollection(collection);
        }

        public Builder registerAction(@NotNull String key, @NotNull ClickAction action) {
            this.collection.registerAction(key, action);
            return this;
        }

        public Builder unregisterAction(@NotNull String key) {
            this.collection.unregisterAction(key);
            return this;
        }

        public Builder unregisterAction(@NotNull ClickAction action) {
            this.collection.unregisterAction(action);
            return this;
        }

        public Builder unregisterAll() {
            this.collection.unregisterAll();
            return this;
        }

        public ClickActionCollection build() {
            return this.collection;
        }
    }
}
