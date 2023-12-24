package net.mineles.library.plugin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BungeeCommandManager;
import com.google.common.collect.ImmutableSet;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.mineles.library.configuration.ConfigManager;
import net.mineles.library.listener.ListenerRegistry;
import net.mineles.library.metadata.store.MetadataStore;
import net.mineles.library.plugin.scheduler.concurrent.ConcurrentTaskScheduler;
import net.mineles.library.plugin.scheduler.concurrent.forkjoin.ForkJoinPoolBuilder;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Set;
import java.util.logging.Logger;

public abstract class AbstractBungeePlugin implements BungeePlugin {
    private final Plugin plugin;

    private ConcurrentTaskScheduler taskScheduler;

    private ConfigManager configManager;
    private BungeeCommandManager commandManager;
    private MetadataStore metadataStore;

    protected AbstractBungeePlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        onLoad();
    }

    public abstract void onLoad();

    @Override
    public void enable() {
        getLogger().info("Setting up task scheduler...");
        setupTaskScheduler();

        getLogger().info("Loading configs...");
        this.configManager = new ConfigManager(getDataPath());
        loadConfigs();

        onEnable();

        getLogger().info("Starting tasks...");
        startTasks();

        getLogger().info("Registering commands...");
        this.commandManager = new BungeeCommandManager(this.plugin);
        registerCommands();

        getLogger().info("Registering listeners...");
        ListenerRegistry.register(this, new ImmutableSet.Builder<Class<?>>()
                .addAll(getListeners())
                .build());

        this.metadataStore = new MetadataStore();
    }

    public abstract void onEnable();

    @Override
    public void disable() {
        this.taskScheduler.shutdownExecutor();

        onDisable();

        this.taskScheduler.shutdownWorkerPool();
    }

    public abstract void onDisable();

    protected void registerCommands() {
        getCommands().forEach(this.commandManager::registerCommand);
    }

    protected abstract @NotNull Set<BaseCommand> getCommands();

    protected abstract @NotNull Set<Class<?>> getListeners();

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public Path getDataPath() {
        return this.plugin.getDataFolder().toPath().toAbsolutePath();
    }

    @Override
    public Logger getLogger() {
        return this.plugin.getLogger();
    }

    @Override
    public ProxyServer getServer() {
        return this.plugin.getProxy();
    }

    private void setupTaskScheduler() {
        this.taskScheduler = new ConcurrentTaskScheduler(getLogger(), getPlugin().getDescription().getName());

        ForkJoinPoolBuilder builder = this.taskScheduler.createWorkerPoolBuilder()
                .setDaemon(true)
                .setAsyncMode(true);
        this.taskScheduler.setWorkerPool(builder.build());
    }

    @Override
    public ConcurrentTaskScheduler getTaskScheduler() {
        return this.taskScheduler;
    }

    @Override
    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    @Override
    public BungeeCommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public MetadataStore getMetadataStore() {
        return this.metadataStore;
    }
}
