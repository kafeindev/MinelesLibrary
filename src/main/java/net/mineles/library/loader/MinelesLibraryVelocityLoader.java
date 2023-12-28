package net.mineles.library.loader;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

@Plugin(
        id = "MinelesLibrary",
        name = "MinelesLibrary",
        version = "1.0.0",
        authors = "Mineles",
        description = "Library for Mineles Network."
)
public final class MinelesLibraryVelocityLoader {
    private final ProxyServer server;

    @Inject
    public MinelesLibraryVelocityLoader(ProxyServer server) {
        this.server = server;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

    }
}
