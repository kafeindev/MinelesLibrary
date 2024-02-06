package net.mineles.library.loader;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.mineles.library.dependency.DependencyManager;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;

public final class MinelesLibraryVelocityLoader {
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataPath;

    @Inject
    public MinelesLibraryVelocityLoader(ProxyServer server, Logger logger, @DataDirectory Path dataPath) {
        this.server = server;
        this.logger = logger;
        this.dataPath = dataPath;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("Loading Dependencies...");
        PluginManager pluginManager = server.getPluginManager();

        DependencyManager dependencyManager = new DependencyManager(this.dataPath);
        File[] dependencyFiles = dependencyManager.loadAllFromRepositoryAsFiles().join();
        for (File dependencyFile : dependencyFiles) {
            pluginManager.addToClasspath(this, dependencyFile.toPath());
        }
        logger.info("Loaded Dependencies.");
    }
}
