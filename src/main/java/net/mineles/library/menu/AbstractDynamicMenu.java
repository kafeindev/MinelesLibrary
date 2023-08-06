package net.mineles.library.menu;

import net.mineles.library.plugin.BukkitPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractDynamicMenu extends AbstractMenu implements DynamicMenu {
    protected AbstractDynamicMenu(@NotNull BukkitPlugin plugin, @NotNull String name) {
        super(plugin, name);
    }
}
