package net.mineles.library.menu.button;

import com.google.common.collect.Maps;
import net.mineles.library.components.ItemComponent;
import net.mineles.library.menu.MenuException;
import net.mineles.library.menu.misc.contexts.OpenContext;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

final class DefaultButton extends AbstractButton {
    DefaultButton(@NotNull ButtonProperties properties,
                  @NotNull ClickHandler clickHandler,
                  @NotNull ItemStackFactory itemStackFactory) {
        super(properties, clickHandler, itemStackFactory);
    }

    @NotNull
    public static DefaultButton.Builder newBuilder() {
        return new Builder();
    }

    @Override
    public @NotNull ButtonType getType() {
        return ButtonType.DEFAULT;
    }

    public static final class Builder extends AbstractButton.Builder<DefaultButton, Builder> {
        private final @NotNull DefaultItemStackFactoryBuilder itemStackFactoryBuilder;

        Builder() {
            super();
            this.itemStackFactoryBuilder = new DefaultItemStackFactoryBuilder();
        }

        public @NotNull DefaultButton.Builder itemModifier(@NotNull BiFunction<OpenContext, ItemComponent, ItemComponent> modifier) {
            this.itemStackFactoryBuilder.itemModifier(modifier);
            return this;
        }

        public @NotNull DefaultButton.Builder placeholders(@NotNull Function<OpenContext, Map<String, String>> placeholders) {
            this.itemStackFactoryBuilder.placeholders(placeholders);
            return this;
        }

        public @NotNull DefaultButton.Builder placeholders(@NotNull Map<String, String> placeholders) {
            return placeholders(context -> placeholders);
        }

        @Override
        public @NotNull DefaultButton build() {
            return new DefaultButton(properties().build(), clickHandler(), this.itemStackFactoryBuilder.build());
        }
    }

    static final class DefaultItemStackFactoryBuilder extends ItemStackFactory.Builder<DefaultItemStackFactoryBuilder> {
        private Function<OpenContext, ItemComponent> itemFactory;
        private BiFunction<OpenContext, ItemComponent, ItemComponent> itemModifier;

        DefaultItemStackFactoryBuilder() {
            super();
        }

        public @Nullable Function<OpenContext, ItemComponent> itemFactory() {
            return this.itemFactory;
        }

        public @NotNull DefaultItemStackFactoryBuilder itemFactory(@NotNull Function<OpenContext, ItemComponent> factory) {
            this.itemFactory = factory;
            return this;
        }

        public @Nullable BiFunction<OpenContext, ItemComponent, ItemComponent> itemModifier() {
            return this.itemModifier;
        }

        public @NotNull DefaultItemStackFactoryBuilder itemModifier(@NotNull BiFunction<OpenContext, ItemComponent, ItemComponent> modifier) {
            this.itemModifier = modifier;
            return this;
        }

        @Override
        @NotNull ItemStackFactory build() {
            return (context, button) -> {
                if (button.getNode() == null && this.itemFactory == null) {
                    throw new MenuException("");
                }

                Map<String, String> placeholders = this.placeholders.apply(context);

                Map<Integer, ItemStack> itemStacks = Maps.newHashMap();
                for (int slot : button.getSlots()) {
                    ItemComponent itemComponent = this.itemFactory == null
                            ? this.itemFactory.apply(context)
                            : ItemComponent.from(button.getNode(), placeholders);
                    if (this.itemModifier != null) {
                        itemComponent = this.itemModifier.apply(context, itemComponent);
                    }

                    itemStacks.put(slot, itemComponent.getHandle());
                }

                return itemStacks;
            };
        }
    }
}
