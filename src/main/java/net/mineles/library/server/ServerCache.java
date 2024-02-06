package net.mineles.library.server;

import net.mineles.library.connection.HostAndPort;
import net.mineles.library.redis.RedisCache;
import net.mineles.library.server.constants.ServerKeys;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ServerCache {
    private final RedisCache cache;

    public ServerCache(RedisCache cache) {
        this.cache = cache;
    }

    public long getServerCount() {
        return this.cache.hlen(ServerKeys.SERVERS);
    }

    public Map<String, Server> getServers() {
        return this.cache.hgetAllDecoded(ServerKeys.SERVERS, Server.class);
    }

    public Map<String, RegisteredServer> getRegisteredServers() {
        return getServers().values().stream()
                .filter(server -> server instanceof RegisteredServer)
                .map(server -> (RegisteredServer) server)
                .collect(Collectors.toMap(RegisteredServer::getName, Function.identity()));
    }

    public Server getServer(String name) {
        return this.cache.hgetDecoded(ServerKeys.SERVERS, name, Server.class);
    }

    public RegisteredServer getRegisteredServer(String name) {
        return getRegisteredServers().get(name);
    }

    public RegisteredServer getRegisteredServer(HostAndPort hostAndPort) {
        return getRegisteredServers().values().stream()
                .filter(server -> server.getHostAndPort().equals(hostAndPort))
                .findFirst()
                .orElse(null);
    }

    public boolean existsServer(String name) {
        return this.cache.hexists(ServerKeys.SERVERS, name);
    }

    public void setServer(String name, Server server) {
        this.cache.hsetDecoded(ServerKeys.SERVERS, name, server, Server.class);
    }

    public void delServer(String name) {
        this.cache.hdel(ServerKeys.SERVERS, name);
    }

    public void clear() {
        this.cache.delete(ServerKeys.SERVERS);
    }
}
