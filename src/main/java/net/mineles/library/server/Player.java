package net.mineles.library.server;

import java.util.UUID;

public final class Player {
    private final UUID uuid;
    private final String name;

    private String currentProxy;
    private String currentServer;
    private boolean loggedIn;

    public Player(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public Player(UUID uuid, String name, String currentProxy, String currentServer, boolean loggedIn) {
        this.uuid = uuid;
        this.name = name;
        this.currentProxy = currentProxy;
        this.currentServer = currentServer;
        this.loggedIn = loggedIn;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getCurrentServer() {
        return currentServer;
    }

    public void setCurrentServer(String currentServer) {
        this.currentServer = currentServer;
    }

    public String getCurrentProxy() {
        return currentProxy;
    }

    public void setCurrentProxy(String currentProxy) {
        this.currentProxy = currentProxy;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
