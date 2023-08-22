package net.mineles.library.menu.button;

import com.cryptomorin.xseries.XSound;
import net.mineles.library.components.ItemComponent;
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
import java.util.function.BiConsumer;

import static com.google.common.base.Preconditions.checkNotNull;

abstract class AbstractButton implements Button {
    private final @NotNull AttributeMap attributes;
    private final @NotNull ClickHandler clickHandler;
    private final @NotNull ItemStackFactory itemStackFactory;

    AbstractButton(@NotNull AttributeMap attributes,
                   @NotNull ClickHandler clickHandler,
                   @NotNull ItemStackFactory itemStackFactory) {
        this.attributes = attributes;
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
    public @NotNull AttributeMap getAttributes() {
        return this.attributes;
    }

    @Override
    public <T> @Nullable T getAttribute(@NotNull ButtonAttributes attribute) {
        return this.attributes.getValue(attribute);
    }

    @Override
    public <T> @Nullable T getAttribute(@NotNull ButtonAttributes attribute, @NotNull Class<T> type) {
        return this.attributes.getValue(attribute, type);
    }

    @Override
    public @NotNull Node getNode() {
        return checkNotNull(this.attributes.getValue(ButtonAttributes.NODE));
    }

    @Override
    public @NotNull String getName() {
        return checkNotNull(this.attributes.getValue(ButtonAttributes.NAME));
    }

    @Override
    public @NotNull ButtonType getType() {
        return checkNotNull(this.attributes.getValue(ButtonAttributes.TYPE));
    }

    @Override
    public int[] getSlots() {
        return this.attributes.getValue(ButtonAttributes.SLOTS);
    }

    @Override
    public boolean hasSlot(int slot) {
        int[] slots = getSlots();
        return ArrayUtils.contains(slots, slot);
    }

    @Override
    public @Nullable XSound getClickSound() {
        return this.attributes.getValue(ButtonAttributes.CLICK_SOUND);
    }

    abstract static class Builder<T extends Builder<T>> {
        protected final AttributeMap attributes;
        protected final DefaultButton.DefaultItemStackFactoryBuilder itemStackFactoryBuilder;
        protected ClickHandler clickHandler;

        Builder() {
            this.attributes = new AttributeMap();
            this.clickHandler = context -> ClickResult.CANCELLED;
            this.itemStackFactoryBuilder = new DefaultButton.DefaultItemStackFactoryBuilder();
        }

        @SuppressWarnings("unchecked")
        public @NotNull T self() {
            return (T) this;
        }

        public @NotNull T node(@NotNull Node node) {
            this.attributes.set(ButtonAttributes.NODE, node);
            return self();
        }

        public @NotNull T name(@NotNull String name) {
            this.attributes.set(ButtonAttributes.NAME, name);
            return self();
        }

        public @NotNull T type(@NotNull ButtonType type) {
            this.attributes.set(ButtonAttributes.TYPE, type);
            return self();
        }

        public @NotNull T whenClicked(@NotNull ClickHandler clickHandler) {
            this.clickHandler = clickHandler;
            return self();
        }

        public @NotNull T itemModifier(@NotNull BiConsumer<OpenContext, ItemComponent> modifier) {
            this.itemStackFactoryBuilder.modifier(modifier);
            return self();
        }

        public @NotNull Button build() {
            return new DefaultButton(this.attributes, this.clickHandler, this.itemStackFactoryBuilder.build());
        }
    }
}
