package net.mineles.library.server;

import net.mineles.library.connection.HostAndPort;

import java.util.Set;
import java.util.UUID;

public class Server {
    private final String id;
    private final String name;
    private final String image;
    private final long createTime;

    public Server(String id, String name, String image) {
        this(id, name, image, System.currentTimeMillis());
    }

    public Server(String id, String name, String image, long createTime) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public long getCreateTime() {
        return createTime;
    }

    public boolean isStarted() {
        return this instanceof RegisteredServer;
    }

    public RegisteredServer asRegisteredServer(HostAndPort hostAndPort) {
        return new RegisteredServer(this, hostAndPort);
    }

    public RegisteredServer asRegisteredServer(HostAndPort hostAndPort, Set<UUID> players, Set<UUID> entries) {
        return new RegisteredServer(this, hostAndPort, players, entries);
    }
}
