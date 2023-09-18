package net.mineles.library.menu.button;

import com.cryptomorin.xseries.XSound;
import net.mineles.library.menu.misc.ClickResult;
import net.mineles.library.menu.misc.contexts.ClickContext;
import net.mineles.library.menu.misc.contexts.OpenContext;
import net.mineles.library.node.Node;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface Button {
    @NotNull
    static Button fromNode(@NotNull Node node) {
        return DefaultButton.newBuilder()
                .name(node.toStringKey())
                .node(node)
                .build();
    }

    @NotNull
    static DefaultButton.Builder newBuilder() {
        return DefaultButton.newBuilder();
    }

    @NotNull
    static <T> PaginatedButton.Builder<T> newPaginatedBuilder() {
        return PaginatedButton.newBuilder();
    }

    @NotNull ClickResult click(@NotNull ClickContext context);

    @NotNull Map<Integer, ItemStack> createItemStacks(@NotNull OpenContext context);

    @Nullable Node getNode();

    @NotNull String getName();

    @NotNull ButtonType getType();

    int[] getSlots();

    boolean hasSlot(int slot);

    @Nullable XSound getClickSound();
}
