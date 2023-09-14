package net.mineles.library.menu.button;

import com.google.common.collect.Maps;
import net.mineles.library.menu.misc.contexts.OpenContext;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

@FunctionalInterface
interface ItemStackFactory {
    @NotNull Map<Integer, ItemStack> createItemStacks(@NotNull OpenContext context, @NotNull Button button);

    abstract class Builder<T extends Builder<T>> {
        protected Function<OpenContext, Map<String, String>> placeholders;

        protected Builder() {
            placeholders = context -> Maps.newHashMap();
        }

        @SuppressWarnings("unchecked")
        private T self() {
            return (T) this;
        }

        public @Nullable Function<OpenContext, Map<String, String>> placeholders() {
            return this.placeholders;
        }

        public @NotNull T placeholders(@NotNull Function<OpenContext, Map<String, String>> placeholders) {
            this.placeholders = placeholders;
            return self();
        }

        public @NotNull T placeholders(@NotNull Map<String, String> placeholders) {
            return placeholders(context -> placeholders);
        }

        abstract @NotNull ItemStackFactory build();
    }
}
