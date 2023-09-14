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

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.common.collect.Sets;
import net.mineles.library.components.PlayerComponent;
import net.mineles.library.components.SenderComponent;
import net.mineles.library.listener.ListenerRegistry;
import net.mineles.library.menu.MenuManager;
import net.mineles.library.metadata.store.MetadataStore;
import net.mineles.library.plugin.scheduler.concurrent.ConcurrentTaskScheduler;
import net.mineles.library.plugin.scheduler.concurrent.forkjoin.ForkJoinPoolBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Set;
import java.util.logging.Logger;

public abstract class AbstractBukkitPlugin implements BukkitPlugin {
    private final @NotNull Plugin plugin;

    private ConcurrentTaskScheduler taskScheduler;

    private MenuManager menuManager;
    private PaperCommandManager commandManager;
    private ProtocolManager protocolManager;
    private MetadataStore metadataStore;

    protected AbstractBukkitPlugin(@NotNull Plugin plugin) {
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
        this.taskScheduler = setupTaskScheduler();

        this.protocolManager = ProtocolLibrary.getProtocolManager();

        onEnable();

        getLogger().info("Loading menus...");
        this.menuManager = new MenuManager(this);
        this.menuManager.initialize();

        getLogger().info("Starting tasks...");
        startTasks();

        getLogger().info("Registering commands...");
        this.commandManager = new PaperCommandManager(this.plugin);
        registerCommands();

        this.metadataStore = new MetadataStore();

        getLogger().info("Registering listeners...");
        registerListeners();
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
    public void registerCommands() {
        getCommandManager().getCommandContexts().registerIssuerAwareContext(SenderComponent.class, resolver -> {
            return SenderComponent.of(resolver.getSender());
        });
        getCommandManager().getCommandContexts().registerIssuerAwareContext(PlayerComponent.class, resolver -> {
            CommandSender sender = resolver.getSender();
            if (!(sender instanceof Player)) {
                SenderComponent.of(sender).sendMessage("§cYou must be a player to use this command.");
                return null;
            }

            return PlayerComponent.from(this, (Player) sender);
        });

        getCommands().forEach(command -> getCommandManager().registerCommand(command));
    }

    public abstract @NotNull Set<BaseCommand> getCommands();

    @Override
    public void registerListeners() {
        ListenerRegistry.register(this, getListeners());
    }

    @NotNull
    public Set<Class<?>> getListeners() {
        return Sets.newHashSet();
    }

    @Override
    public @NotNull Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public @NotNull Path getDataPath() {
        return this.plugin.getDataFolder().toPath().toAbsolutePath();
    }

    @Override
    public @NotNull Logger getLogger() {
        return this.plugin.getLogger();
    }

    @Override
    public @NotNull ConcurrentTaskScheduler setupTaskScheduler() {
        ConcurrentTaskScheduler taskScheduler = new ConcurrentTaskScheduler(getLogger(), getPlugin().getName());

        ForkJoinPoolBuilder builder = taskScheduler.createWorkerPoolBuilder()
                .setDaemon(true)
                .setAsyncMode(true);
        taskScheduler.setWorkerPool(builder.build());

        return taskScheduler;
    }

    @Override
    public @NotNull ConcurrentTaskScheduler getTaskScheduler() {
        return this.taskScheduler;
    }

    @Override
    public @NotNull MenuManager getMenuManager() {
        return this.menuManager;
    }

    @Override
    public @NotNull PaperCommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public @NotNull ProtocolManager getProtocolManager() {
        return this.protocolManager;
    }

    @Override
    public @NotNull MetadataStore getMetadataStore() {
        return this.metadataStore;
    }
}
