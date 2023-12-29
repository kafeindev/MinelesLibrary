package net.mineles.library.loader;

import com.google.inject.Inject;
import com.velocitypowered.api.proxy.ProxyServer;

public final class MinelesLibraryVelocityLoader {
    private final ProxyServer server;

    @Inject
    public MinelesLibraryVelocityLoader(ProxyServer server) {
        this.server = server;
    }
}
