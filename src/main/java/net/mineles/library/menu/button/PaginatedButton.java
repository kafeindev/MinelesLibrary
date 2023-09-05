package net.mineles.library.menu.button;

import com.google.common.collect.Maps;
import net.mineles.library.components.ItemComponent;
import net.mineles.library.menu.misc.contexts.OpenContext;
import net.mineles.library.property.AttributeMap;
import net.mineles.library.utils.TriConsumer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

final class PaginatedButton<T> extends AbstractButton implements Button {
    PaginatedButton(@NotNull AttributeMap attributes,
                    @NotNull ClickHandler clickHandler,
                    @NotNull ItemStackFactory itemStackFactory) {
        super(attributes, clickHandler, itemStackFactory);
    }

    public static final class Builder {

    }

    final class PaginatedItemStackFactoryBuilder<T> extends ItemStackFactory.Builder<PaginatedItemStackFactoryBuilder<T>> {
        private Function<OpenContext, List<T>> entries;
        private BiFunction<OpenContext, T, Map<String, String>> placeholdersPerEntry;
        private TriConsumer<OpenContext, ItemComponent, T> modifier;

        private PaginatedItemStackFactoryBuilder() {
            super();
        }

        public @NotNull PaginatedItemStackFactoryBuilder<T> entries(@NotNull Function<OpenContext, List<T>> entries) {
            this.entries = entries;
            return this;
        }

        public @NotNull Function<OpenContext, List<T>> entries() {
            return this.entries;
        }

        public @NotNull PaginatedItemStackFactoryBuilder<T> placeholdersPerEntry(@NotNull BiFunction<OpenContext, T, Map<String, String>> placeholdersPerEntry) {
            this.placeholdersPerEntry = placeholdersPerEntry;
            return this;
        }

        public @NotNull BiFunction<OpenContext, T, Map<String, String>> placeholdersPerEntry() {
            return this.placeholdersPerEntry;
        }

        public @NotNull PaginatedItemStackFactoryBuilder<T> modifier(@NotNull TriConsumer<OpenContext, ItemComponent, T> modifier) {
            this.modifier = modifier;
            return this;
        }

        public @NotNull TriConsumer<OpenContext, ItemComponent, T> modifier() {
            return this.modifier;
        }

        @Override
        @NotNull ItemStackFactory build() {
            checkNotNull(this.entries, "entries");

            return (context, button) -> {
                List<T> entries = this.entries.apply(context);
                int slotCount = getSlots().length;

                int minIndex = context.getPage() * slotCount;
                if (minIndex >= entries.size()) {
                    return Maps.newHashMap();
                }

                Map<Integer, ItemStack> itemStacks = Maps.newHashMap();
                for (int i = 0; i < slotCount; i++) {
                    int position = minIndex + i;
                    if (position >= entries.size()) {
                        break;
                    }

                    T entry = entries.get(position);
                    Map<String, String> placeholders = this.placeholdersPerEntry != null
                            ? this.placeholdersPerEntry.apply(context, entry)
                            : this.placeholders.apply(context);

                    ItemComponent itemComponent = ItemComponent.from(getNode(), placeholders);
                    if (this.modifier != null) {
                        this.modifier.accept(context, itemComponent, entry);
                    }

                    itemStacks.put(getSlots()[i], itemComponent.getHandle());
                }

                return itemStacks;
            };
        }
    }
}
