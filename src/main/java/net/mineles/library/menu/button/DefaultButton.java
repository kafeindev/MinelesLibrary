package net.mineles.library.menu.button;

import com.cryptomorin.xseries.XSound;
import com.google.common.collect.Maps;
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

final class DefaultButton extends AbstractButton {
    DefaultButton(@NotNull AttributeMap attributes,
                  @NotNull ClickHandler clickHandler,
                  @NotNull ItemStackFactory itemStackFactory) {
        super(attributes, clickHandler, itemStackFactory);
    }

    @NotNull
    public static DefaultBuilder newBuilder() {
        return new DefaultBuilder();
    }

    public static final class DefaultBuilder extends AbstractButton.Builder<DefaultBuilder> {
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
