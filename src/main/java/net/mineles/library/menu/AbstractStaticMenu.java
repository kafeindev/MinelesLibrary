package net.mineles.library.menu;

import com.google.common.collect.Maps;
import net.mineles.library.components.PlayerComponent;
import net.mineles.library.menu.button.Button;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public abstract class AbstractStaticMenu extends AbstractMenu {
    private Inventory inventory;

    protected AbstractStaticMenu(@NotNull MenuProperties properties) {
        super(properties);
    }

    protected AbstractStaticMenu(@NotNull MenuProperties properties,
                                 @NotNull Set<Button> buttons) {
        super(properties, buttons);
    }

    @Override
    @NotNull Inventory createInventory(@NotNull PlayerComponent player, int page) {
        if (this.inventory == null) {
            this.inventory = super.createInventory(player, page);
        }

        return this.inventory;
    }

    @Override
    @NotNull Map<String, String> createTitlePlaceholders(@NotNull PlayerComponent player, int page) {
        return Maps.newHashMap();
    }

    @Override
    public @NotNull MenuType getType() {
        return MenuType.STATIC;
    }
}
