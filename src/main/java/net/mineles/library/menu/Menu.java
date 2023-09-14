package net.mineles.library.menu;

import com.cryptomorin.xseries.XSound;
import net.mineles.library.components.PlayerComponent;
import net.mineles.library.menu.button.Button;
import net.mineles.library.menu.misc.ClickResult;
import net.mineles.library.menu.misc.contexts.ClickContext;
import net.mineles.library.node.Node;
import net.mineles.library.property.Attribute;
import net.mineles.library.property.AttributeMap;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

public interface Menu {
    static @NotNull MenuBuilder newBuilder() {
        return new MenuBuilder();
    }

    default boolean open(@NotNull PlayerComponent player) {
        return open(player, 0);
    }

    boolean open(@NotNull PlayerComponent player, int page);

    boolean close(@NotNull PlayerComponent player);

    boolean refresh(@NotNull PlayerComponent player);

    boolean refreshButton(@NotNull PlayerComponent player, int slot);

    boolean refreshButton(@NotNull PlayerComponent player, @NotNull String buttonName);

    @NotNull ClickResult click(@NotNull ClickContext context);

    @NotNull MenuProperties getProperties();

    @Nullable Node getNode();

    @NotNull String getName();

    @NotNull MenuType getType();

    @Nullable String getParent();

    @NotNull InventoryProperties getInventoryProperties();

    @NotNull String getTitle();

    @NotNull InventoryType getInventoryType();

    int getSize();

    @NotNull Set<Button> getButtons();

    @NotNull Optional<Button> findButton(@NotNull String name);

    @NotNull Optional<Button> findButton(int slot);

    void putButtons(@NotNull Button... buttons);

    void putButtons(@NotNull Iterable<Button> buttons);

    void putButton(@NotNull Button button);

    void removeButtons(@NotNull Button... buttons);

    void removeButtons(@NotNull Iterable<Button> buttons);

    void removeButton(@NotNull Button button);

    void removeButton(@NotNull String name);

    @Nullable XSound getOpenSound();

    @Nullable XSound getCloseSound();
}
