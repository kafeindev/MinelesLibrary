package net.mineles.library.plugin;

import com.velocitypowered.api.proxy.ProxyServer;
import net.mineles.library.configuration.ConfigManager;
import net.mineles.library.metadata.store.MetadataStore;
import org.slf4j.Logger;

import java.nio.file.Path;

public abstract class AbstractVelocityPlugin implements VelocityPlugin {
    private final ProxyServer proxyServer;
    private final Logger logger;
    private final Path path;

    private ConfigManager configManager;
    private MetadataStore metadataStore;

    protected AbstractVelocityPlugin(ProxyServer proxyServer, Logger logger, Path path) {
        this.proxyServer = proxyServer;
        this.logger = logger;
        this.path = path;
    }

    @Override
    public void load() {
        onLoad();
    }

    public abstract void onLoad();

    @Override
    public void enable() {
        getLogger().info("Loading configs...");
        this.configManager = new ConfigManager(getDataPath());
        loadConfigs();

        onEnable();

        getLogger().info("Starting tasks...");
        startTasks();

        getLogger().info("Registering commands...");
        registerCommands();

        this.metadataStore = new MetadataStore();
    }

    public abstract void onEnable();

    @Override
    public void disable() {
        onDisable();
    }

    public abstract void onDisable();

    protected abstract void registerCommands();

    @Override
    public Path getDataPath() {
        return this.path;
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }

    @Override
    public ProxyServer getServer() {
        return this.proxyServer;
    }

    @Override
    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    @Override
    public MetadataStore getMetadataStore() {
        return this.metadataStore;
    }
}
