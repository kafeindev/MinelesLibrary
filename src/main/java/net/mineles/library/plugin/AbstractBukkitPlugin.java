/*
 * MIT License
 *
 * Copyright (c) 2023 Kafein
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.mineles.library.plugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.common.collect.ImmutableSet;
import net.mineles.library.command.Command;
import net.mineles.library.command.CommandManager;
import net.mineles.library.configuration.ConfigManager;
import net.mineles.library.listener.InventoryListener;
import net.mineles.library.listener.ListenerRegistry;
import net.mineles.library.menu.Menu;
import net.mineles.library.menu.MenuManager;
import net.mineles.library.metadata.store.MetadataStore;
import net.mineles.library.plugin.scheduler.concurrent.ConcurrentTaskScheduler;
import net.mineles.library.plugin.scheduler.concurrent.forkjoin.ForkJoinPoolBuilder;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Set;
import java.util.logging.Logger;

public abstract class AbstractBukkitPlugin implements BukkitPlugin {
    private final Plugin plugin;

    private ConcurrentTaskScheduler taskScheduler;

    private ConfigManager configManager;
    private MenuManager menuManager;
    private CommandManager commandManager;
    private ProtocolManager protocolManager;
    private MetadataStore metadataStore;

    protected AbstractBukkitPlugin(Plugin plugin) {
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

        getLogger().info("Loading menus...");
        this.menuManager = new MenuManager();
        loadMenus();

        this.protocolManager = ProtocolLibrary.getProtocolManager();

        onEnable();

        getLogger().info("Starting tasks...");
        startTasks();

        getLogger().info("Registering commands...");
        this.commandManager = new CommandManager(this.plugin);
        registerCommands();

        getLogger().info("Registering listeners...");
        ListenerRegistry.register(this, new ImmutableSet.Builder<Class<?>>()
                .add(InventoryListener.class)
                .addAll(getListeners())
                .build());

        getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, "BungeeCord");

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

    @Override
    public void loadMenus() {
        this.menuManager.stopViewing();
        this.menuManager.clear();
        this.menuManager.register(getMenus());
    }

    protected void registerCommands() {
        getCommands().forEach(this.commandManager::registerCommand);
    }

    protected abstract @NotNull Set<Command> getCommands();

    protected abstract @NotNull Set<Menu> getMenus();

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
    public Server getServer() {
        return this.plugin.getServer();
    }

    private void setupTaskScheduler() {
        this.taskScheduler = new ConcurrentTaskScheduler(getLogger(), getPlugin().getName());

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
    public MenuManager getMenuManager() {
        return this.menuManager;
    }

    @Override
    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public ProtocolManager getProtocolManager() {
        return this.protocolManager;
    }

    @Override
    public MetadataStore getMetadataStore() {
        return this.metadataStore;
    }
}
