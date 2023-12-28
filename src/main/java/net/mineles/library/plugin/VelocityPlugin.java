package net.mineles.library.plugin;

import com.velocitypowered.api.proxy.ProxyServer;
import net.mineles.library.configuration.ConfigManager;
import net.mineles.library.metadata.store.MetadataStore;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;

public interface VelocityPlugin {
    void load();

    void enable();

    void disable();

    void startTasks();

    void loadConfigs();

    Path getDataPath();

    Logger getLogger();

    ProxyServer getServer();

    ConfigManager getConfigManager();

    MetadataStore getMetadataStore();

    default <T extends VelocityPlugin> T getAs(@NotNull Class<T> clazz) {
        return clazz.cast(this);
    }
}
