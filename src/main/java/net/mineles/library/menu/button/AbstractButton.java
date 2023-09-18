package net.mineles.library.menu.button;

import com.cryptomorin.xseries.XSound;
import net.mineles.library.menu.misc.ClickResult;
import net.mineles.library.menu.misc.contexts.ClickContext;
import net.mineles.library.menu.misc.contexts.OpenContext;
import net.mineles.library.node.Node;
import net.mineles.library.property.AttributeMap;
import net.mineles.library.utils.ArrayUtils;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

abstract class AbstractButton implements Button {
    private final @NotNull ButtonProperties properties;
    private final @NotNull ClickHandler clickHandler;
    private final @NotNull ItemStackFactory itemStackFactory;

    AbstractButton(@NotNull ButtonProperties properties,
                   @NotNull ClickHandler clickHandler,
                   @NotNull ItemStackFactory itemStackFactory) {
        this.properties = properties;
        this.clickHandler = clickHandler;
        this.itemStackFactory = itemStackFactory;
    }

    @Override
    public @NotNull ClickResult click(@NotNull ClickContext context) {
        return this.clickHandler.apply(context);
    }

    @Override
    public @NotNull Map<Integer, ItemStack> createItemStacks(@NotNull OpenContext context) {
        return this.itemStackFactory.createItemStacks(context, this);
    }

    @Override
    public @Nullable Node getNode() {
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
        protected final ButtonProperties.Builder properties;
        protected ClickHandler clickHandler;

        protected Builder() {
            this.properties = ButtonProperties.newBuilder();
            this.clickHandler = context -> ClickResult.CANCELLED;
        }

        @SuppressWarnings("unchecked")
        public @NotNull B self() {
            return (B) this;
        }

        public @NotNull B node(@NotNull Node node) {
            this.properties.node(node);
            return self();
        }

        public @NotNull B name(@NotNull String name) {
            this.properties.name(name);
            return self();
        }

        public @NotNull ButtonProperties.Builder properties() {
            return this.properties;
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
