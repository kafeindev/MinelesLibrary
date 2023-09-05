package net.mineles.library.menu.button;

import com.google.common.collect.Maps;
import net.mineles.library.components.ItemComponent;
import net.mineles.library.menu.misc.contexts.OpenContext;
import net.mineles.library.property.AttributeMap;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.BiConsumer;

final class DefaultButton extends AbstractButton {
    DefaultButton(@NotNull AttributeMap attributes,
                  @NotNull ClickHandler clickHandler,
                  @NotNull ItemStackFactory itemStackFactory) {
        super(attributes, clickHandler, itemStackFactory);
    }

    @NotNull
    public static DefaultButton.Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder extends AbstractButton.Builder<DefaultButton, Builder> {
        private final @NotNull DefaultItemStackFactoryBuilder itemStackFactoryBuilder;

        Builder() {
            super();
            this.itemStackFactoryBuilder = new DefaultItemStackFactoryBuilder();
        }

        public @NotNull DefaultButton.Builder itemModifier(@NotNull BiConsumer<OpenContext, ItemComponent> modifier) {
            this.itemStackFactoryBuilder.modifier(modifier);
            return this;
        }

        @Override
        public @NotNull DefaultButton build() {
            return new DefaultButton(attributes(), clickHandler(), this.itemStackFactoryBuilder.build());
        }
    }

    static final class DefaultItemStackFactoryBuilder extends ItemStackFactory.Builder<DefaultItemStackFactoryBuilder> {
        private BiConsumer<OpenContext, ItemComponent> modifier;

        DefaultItemStackFactoryBuilder() {
            super();
        }

        public @NotNull DefaultItemStackFactoryBuilder modifier(@NotNull BiConsumer<OpenContext, ItemComponent> modifier) {
            this.modifier = modifier;
            return this;
        }

        public @Nullable BiConsumer<OpenContext, ItemComponent> modifier() {
            return this.modifier;
        }

        @Override
        @NotNull ItemStackFactory build() {
            return (context, button) -> {
                Map<String, String> placeholders = this.placeholders.apply(context);

                Map<Integer, ItemStack> itemStacks = Maps.newHashMap();
                for (int slot : button.getSlots()) {
                    ItemComponent itemComponent = ItemComponent.from(button.getNode(), placeholders);
                    if (this.modifier != null) {
                        this.modifier.accept(context, itemComponent);
                    }

                    itemStacks.put(slot, itemComponent.getHandle());
                }

                return itemStacks;
            };
        }
    }
}
