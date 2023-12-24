package net.mineles.library.menu.button;

import com.google.common.collect.Maps;
import net.mineles.library.components.ItemComponent;
import net.mineles.library.menu.action.RegisteredClickAction;
import net.mineles.library.menu.misc.contexts.OpenContext;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

final class DefaultButton extends AbstractButton {
    DefaultButton(ButtonProperties properties, ItemStackFactory itemStackFactory, ClickHandler clickHandler) {
        super(properties, itemStackFactory, clickHandler);
    }

    DefaultButton(ButtonProperties properties, ItemStackFactory itemStackFactory, ClickHandler clickHandler, Set<RegisteredClickAction> clickActions) {
        super(properties, itemStackFactory, clickHandler, clickActions);
    }

    public static DefaultButton.Builder newBuilder() {
        return new Builder();
    }

    @Override
    public ButtonType getType() {
        return ButtonType.DEFAULT;
    }

    public static final class Builder extends AbstractButton.Builder<DefaultButton, Builder> {
        private final DefaultItemStackFactoryBuilder itemStackFactoryBuilder;

        Builder() {
            super();
            this.itemStackFactoryBuilder = new DefaultItemStackFactoryBuilder();
        }

        public DefaultButton.Builder itemFactory(@NotNull Function<OpenContext, ItemComponent> factory) {
            this.itemStackFactoryBuilder.itemFactory(factory);
            return this;
        }

        public DefaultButton.Builder itemModifier(@NotNull BiConsumer<OpenContext, ItemComponent> modifier) {
            this.itemStackFactoryBuilder.itemModifier(modifier);
            return this;
        }

        public DefaultButton.Builder itemPlaceholders(@NotNull Function<OpenContext, Map<String, String>> placeholders) {
            this.itemStackFactoryBuilder.placeholders(placeholders);
            return this;
        }

        public DefaultButton.Builder itemPlaceholders(@NotNull Map<String, String> placeholders) {
            return itemPlaceholders(context -> placeholders);
        }

        @Override
        public DefaultButton build() {
            return new DefaultButton(properties().build(), this.itemStackFactoryBuilder.build(), clickHandler(), clickActions());
        }
    }

    static final class DefaultItemStackFactoryBuilder extends ItemStackFactory.Builder<DefaultItemStackFactoryBuilder> {
        private Function<OpenContext, ItemComponent> itemFactory;
        private BiConsumer<OpenContext, ItemComponent> itemModifier;

        DefaultItemStackFactoryBuilder() {
            super();
        }

        public @Nullable Function<OpenContext, ItemComponent> itemFactory() {
            return this.itemFactory;
        }

        public DefaultItemStackFactoryBuilder itemFactory(@NotNull Function<OpenContext, ItemComponent> factory) {
            this.itemFactory = factory;
            return this;
        }

        public @Nullable BiConsumer<OpenContext, ItemComponent> itemModifier() {
            return this.itemModifier;
        }

        public DefaultItemStackFactoryBuilder itemModifier(@NotNull BiConsumer<OpenContext, ItemComponent> modifier) {
            this.itemModifier = modifier;
            return this;
        }

        @Override
        @NotNull ItemStackFactory build() {
            return (context, button) -> {
                checkArgument(this.itemFactory != null || button.getNode() != null,
                        "Either itemFactory or node must be set");

                Map<String, String> placeholders = this.placeholders.apply(context);

                ItemComponent itemComponent = this.itemFactory != null
                        ? this.itemFactory.apply(context)
                        : ItemComponent.from(button.getNode(), placeholders, context.getPlayer().getHandle());
                if (this.itemModifier != null) {
                    this.itemModifier.accept(context, itemComponent);
                }

                Map<Integer, ItemStack> itemStacks = Maps.newHashMap();
                for (int slot : button.getSlots()) {
                    itemStacks.put(slot, itemComponent.getHandle());
                }

                return itemStacks;
            };
        }
    }
}
