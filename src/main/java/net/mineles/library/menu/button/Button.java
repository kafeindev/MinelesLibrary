package net.mineles.library.menu.button;

import com.cryptomorin.xseries.XSound;
import net.mineles.library.menu.action.RegisteredClickAction;
import net.mineles.library.menu.misc.ClickResult;
import net.mineles.library.menu.misc.contexts.ClickContext;
import net.mineles.library.menu.misc.contexts.OpenContext;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.Map;
import java.util.Set;

public interface Button {
    static Button fromNode(@NotNull ConfigurationNode node) {
        return DefaultButton.newBuilder()
                .node(node)
                .propertiesFromNode()
                .clickActionsFromNode()
                .build();
    }

    static DefaultButton.Builder newBuilder() {
        return DefaultButton.newBuilder();
    }

    static <T> PaginatedButton.Builder<T> newPaginatedBuilder() {
        return PaginatedButton.newBuilder();
    }

    Map<Integer, ItemStack> createItemStacks(@NotNull OpenContext context);

    ClickResult click(@NotNull ClickContext context);

    void setClickHandler(@NotNull ClickHandler clickHandler);

    Set<RegisteredClickAction> getClickActions();

    void putClickActions(@NotNull Set<RegisteredClickAction> actions);

    void putClickAction(@NotNull RegisteredClickAction action);

    void removeClickAction(@NotNull RegisteredClickAction action);

    void clearClickActions();

    ButtonProperties getProperties();

    @Nullable ConfigurationNode getNode();

    String getName();

    ButtonType getType();

    int[] getSlots();

    boolean hasSlot(int slot);

    @Nullable XSound getClickSound();
}
