package net.mineles.library.plugin;

import com.velocitypowered.api.proxy.ProxyServer;
import net.mineles.library.configuration.ConfigManager;
import net.mineles.library.connection.HostAndPort;
import net.mineles.library.metadata.store.MetadataStore;
import org.apache.hc.core5.net.Host;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.nio.file.Path;

public abstract class AbstractVelocityPlugin extends AbstractMinelesPlugin
        implements VelocityPlugin {
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

        super.enable();

        onEnable();

        getLogger().info("Starting tasks...");
        startTasks();

        getLogger().info("Registering commands...");
        registerCommands();

        getLogger().info("Registering listeners...");
        registerListeners();

        this.metadataStore = new MetadataStore();
    }

    public abstract void onEnable();

    @Override
    public void disable() {
        super.disable();

        onDisable();
    }

    public abstract void onDisable();

    @Override
    public void stopServer() {
        getLogger().info("Stopping server...");
        this.proxyServer.shutdown();
    }

    protected abstract void registerListeners();

    protected abstract void registerCommands();

    @Override
    public void dispatchCommand(String command) {
        this.proxyServer.getCommandManager().executeAsync(this.proxyServer.getConsoleCommandSource(), command);
    }

    @Override
    public void log(String message) {
        getLogger().info(message);
    }

    @Override
    public String getPluginName() {
        return "MinelesVelocityPlugin";
    }

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
    public HostAndPort getServerAddress() {
        return HostAndPort.fromSocketAddress(this.proxyServer.getBoundAddress());
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
