package net.mineles.library.plugin;

import co.aikar.commands.BungeeCommandManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.mineles.library.configuration.ConfigManager;
import net.mineles.library.metadata.store.MetadataStore;
import net.mineles.library.plugin.scheduler.task.TaskScheduler;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.logging.Logger;

public interface BungeePlugin {
    void load();

    void enable();

    void disable();

    void startTasks();

    void loadConfigs();

    @NotNull Plugin getPlugin();

    @NotNull Path getDataPath();

    @NotNull Logger getLogger();

    @NotNull ProxyServer getServer();

    @NotNull TaskScheduler getTaskScheduler();

    @NotNull ConfigManager getConfigManager();

    @NotNull BungeeCommandManager getCommandManager();

    @NotNull MetadataStore getMetadataStore();

    default @NotNull <T extends BungeePlugin> T getAs(@NotNull Class<T> clazz) {
        return clazz.cast(this);
    }
}
