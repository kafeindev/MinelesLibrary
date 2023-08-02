package net.mineles.library.menu;

import net.mineles.library.manager.AbstractManager;
import net.mineles.library.plugin.BukkitPlugin;
import org.jetbrains.annotations.NotNull;

public final class MenuManager extends AbstractManager<String, Menu> {
    @NotNull
    private final BukkitPlugin plugin;

    public MenuManager(@NotNull BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize() {

    }
}
