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

    Plugin getPlugin();

    Path getDataPath();

    Logger getLogger();

    ProxyServer getServer();

    TaskScheduler getTaskScheduler();

    ConfigManager getConfigManager();

    BungeeCommandManager getCommandManager();

    MetadataStore getMetadataStore();

    default <T extends BungeePlugin> T getAs(@NotNull Class<T> clazz) {
        return clazz.cast(this);
    }
}
