package net.mineles.library.menu.button;

import com.cryptomorin.xseries.XSound;
import com.google.common.collect.Sets;
import net.mineles.library.menu.action.RegisteredClickAction;
import net.mineles.library.menu.misc.ClickResult;
import net.mineles.library.menu.misc.contexts.ClickContext;
import net.mineles.library.menu.misc.contexts.OpenContext;
import net.mineles.library.utils.ArrayUtils;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

abstract class AbstractButton implements Button {
    private final @NotNull ButtonProperties properties;
    private final @NotNull ItemStackFactory itemStackFactory;
    private final @NotNull Set<RegisteredClickAction> clickActions;
    private ClickHandler clickHandler;

    AbstractButton(@NotNull ButtonProperties properties,
                   @NotNull ItemStackFactory itemStackFactory,
                   @NotNull ClickHandler clickHandler) {
        this(properties, itemStackFactory, clickHandler, Sets.newHashSet());
    }

    AbstractButton(@NotNull ButtonProperties properties,
                   @NotNull ItemStackFactory itemStackFactory,
                   @NotNull ClickHandler clickHandler,
                   @NotNull Set<RegisteredClickAction>clickActions) {
        this.properties = properties;
        this.clickHandler = clickHandler;
        this.clickActions = clickActions;
        this.itemStackFactory = itemStackFactory;
    }

    @Override
    public @NotNull Map<Integer, ItemStack> createItemStacks(@NotNull OpenContext context) {
        return this.itemStackFactory.createItemStacks(context, this);
    }

    @Override
    public @NotNull ClickResult click(@NotNull ClickContext context) {
        return this.clickHandler.apply(context);
    }

    @Override
    public void setClickHandler(@NotNull ClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public @NotNull Set<RegisteredClickAction> getClickActions() {
        return this.clickActions;
    }

    @Override
    public void putClickActions(@NotNull Set<RegisteredClickAction> actions) {
        this.clickActions.addAll(actions);
    }

    @Override
    public void putClickAction(@NotNull RegisteredClickAction action) {
        this.clickActions.add(action);
    }

    @Override
    public void removeClickAction(@NotNull RegisteredClickAction action) {
        this.clickActions.remove(action);
    }

    @Override
    public void clearClickActions() {
        this.clickActions.clear();
    }

    @Override
    public @NotNull ButtonProperties getProperties() {
        return this.properties;
    }

    @Override
    public @Nullable ConfigurationNode getNode() {
        return this.properties.getNode();
    }

    @Override
    public @NotNull String getName() {
        return this.properties.getName();
    }

    @Override
    public int[] getSlots() {
        return this.properties.getSlots();
    }

    @Override
    public boolean hasSlot(int slot) {
        int[] slots = getSlots();
        return ArrayUtils.contains(slots, slot);
    }

    @Override
    public @Nullable XSound getClickSound() {
        return this.properties.getClickSound();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Button)) return false;

        Button button = (Button) obj;
        return getName().equals(button.getName());
    }

    abstract static class Builder<T, B extends Builder<T, B>> {
        protected ButtonProperties.Builder properties;
        protected Set<RegisteredClickAction> clickActions;
        protected ClickHandler clickHandler;

        protected Builder() {
            this.properties = ButtonProperties.newBuilder();
            this.clickActions = Sets.newHashSet();
            this.clickHandler = context -> ClickResult.CANCELLED;
        }

        @SuppressWarnings("unchecked")
        public @NotNull B self() {
            return (B) this;
        }

        public @NotNull ButtonProperties.Builder properties() {
            return this.properties;
        }

        public @NotNull B properties(@NotNull ButtonProperties properties) {
            this.properties.copy(properties);
            return self();
        }

        public @NotNull B propertiesFromNode() {
            if (this.properties.node() == null) {
                throw new IllegalStateException("Node is null");
            }

            return propertiesFromNode(this.properties.node());
        }

        public @NotNull B propertiesFromNode(@NotNull ConfigurationNode node) {
            this.properties.copy(ButtonProperties.fromNode(node));
            return self();
        }

        public @NotNull B node(@NotNull ConfigurationNode node) {
            this.properties.node(node);
            return self();
        }

        public @NotNull B name(@NotNull String name) {
            this.properties.name(name);
            return self();
        }

        public @NotNull B slots(int... slots) {
            this.properties.slots(slots);
            return self();
        }

        public @NotNull B clickSound(@NotNull XSound clickSound) {
            this.properties.clickSound(clickSound);
            return self();
        }

        public @NotNull Set<RegisteredClickAction> clickActions() {
            return this.clickActions;
        }

        public @NotNull B clickAction(@NotNull Iterable<RegisteredClickAction> actions) {
            this.clickActions.addAll(Sets.newHashSet(actions));
            return self();
        }

        public @NotNull B clickAction(@NotNull RegisteredClickAction... action) {
            this.clickActions.addAll(Arrays.asList(action));
            return self();
        }

        public @NotNull B clickActionsFromNode() {
            if (this.properties.node() == null) {
                throw new IllegalStateException("Node is null");
            }

            return clickActionsFromNode(this.properties.node());
        }

        public @NotNull B clickActionsFromNode(@NotNull ConfigurationNode node) {
            ConfigurationNode actionsNode = node.node("actions");
            if (!actionsNode.empty()) {
                try {
                    List<String> actions = actionsNode.getList(String.class);
                    actions.forEach(keyValue -> {
                        RegisteredClickAction action = RegisteredClickAction.of(keyValue);
                        this.clickActions.add(action);
                    });
                } catch (SerializationException e) {
                    throw new RuntimeException("Failed to deserialize actions", e);
                }
            }

            return self();
        }

        public @NotNull ClickHandler clickHandler() {
            return this.clickHandler;
        }

        public @NotNull B whenClicked(@NotNull ClickHandler clickHandler) {
            this.clickHandler = clickHandler;
            return self();
        }

        public abstract @NotNull T build();
    }
}
