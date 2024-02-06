package net.mineles.library.server;

import com.google.gson.JsonObject;
import net.mineles.library.docker.client.DockerTemplate;
import net.mineles.library.redis.RedisCache;
import net.mineles.library.redis.RedisClient;
import net.mineles.library.server.constants.ServerKeys;
import net.mineles.library.server.decoders.PlayerDecoder;
import net.mineles.library.server.decoders.ServerDecoder;
import net.mineles.library.server.query.Filter;
import net.mineles.library.server.query.Filters;
import net.mineles.library.server.query.Sort;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public final class ServerManager {
    private final ServerController serverController;
    private final RedisClient redisClient;
    private final ServerCache serverCache;
    private final PlayerCache playerCache;

    public ServerManager(RedisClient redisClient) {
        this(redisClient, null);
    }

    public ServerManager(RedisClient redisClient, @Nullable DockerTemplate dockerTemplate) {
        this.redisClient = redisClient;
        this.serverController = new ServerController(this, dockerTemplate);

        RedisCache cache = redisClient.getCache();
        this.serverCache = new ServerCache(cache);
        this.playerCache = new PlayerCache(cache);

        redisClient.registerDecoder(Player.class, new PlayerDecoder());
        redisClient.registerDecoder(Server.class, new ServerDecoder());
    }

    public void redirectPlayer(String playerName, String image, Sort... sorts) {
        Filter[] filters = new Filter[]{
                Filters.online(),
                Filters.image(image)
        };
        redirectPlayer(playerName, filters, sorts);
    }

    public void redirectPlayer(String playerName, Filter[] filters, Sort... sorts) {
        List<? extends Server> servers = sortServers(filters, sorts);
        if (servers.isEmpty()) {
            return;
        }

        Server server = servers.get(0);

        JsonObject object = new JsonObject();
        object.addProperty("player", playerName);
        object.addProperty("name", server.getName());
        this.redisClient.publish(ServerKeys.PLAYERS_SWITCH_SERVER_REQUEST, object.toString());
    }

    public void redirectPlayerForCache(String playerName, UUID playerUniqueId, String proxyName, String serverName) {
        Player player = this.playerCache.getPlayer(playerName);
        if (player == null) {
            player = new Player(playerUniqueId, playerName);
        }

        if (player.getCurrentProxy() == null) {
            player.setCurrentProxy(proxyName);
        }

        if (player.getCurrentServer() != null) {
            RegisteredServer registeredServer = this.serverCache.getRegisteredServer(player.getCurrentServer());
            if (registeredServer != null) {
                registeredServer.getPlayers().remove(player.getUUID());
                this.serverCache.setServer(player.getCurrentServer(), registeredServer);
            }
        }

        RegisteredServer registeredServer = this.serverCache.getRegisteredServer(serverName);
        if (registeredServer != null) {
            registeredServer.getPlayers().add(playerUniqueId);
            this.serverCache.setServer(serverName, registeredServer);
            player.setCurrentServer(serverName);
        }

        this.playerCache.setPlayer(playerName, player);
    }

    public void removePlayerFromCache(String playerName) {
        Player player = this.playerCache.getPlayer(playerName);
        if (player == null) {
            return;
        }

        this.playerCache.removePlayer(playerName);

        if (player.getCurrentProxy() != null) {
            removePlayerFromServerCache(player.getCurrentProxy(), player.getUUID());
        }
        if (player.getCurrentServer() != null) {
            removePlayerFromServerCache(player.getCurrentServer(), player.getUUID());
        }
    }

    private void removePlayerFromServerCache(String serverName, UUID uniqueId) {
        RegisteredServer server = this.serverCache.getRegisteredServer(serverName);
        if (server == null) {
            return;
        }

        server.getPlayers().remove(uniqueId);
        this.serverCache.setServer(serverName, server);
    }

    public void sendDispatchCommand(String serverName, String command) {
        RegisteredServer server = getServerCache().getRegisteredServer(serverName);
        if (server == null) {
            return;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("host", server.getHostAndPort().toString());
        jsonObject.addProperty("command", command);
        this.redisClient.publish(ServerKeys.SERVERS_DISPATCH_COMMAND_REQUEST, jsonObject.toString());
    }

    public List<? extends Server> sortServers(Filter[] filters, Sort... sorts) {
        Stream<RegisteredServer> serverStream = this.serverCache.getServers().values().stream()
                .filter(server -> Filter.test(filters, server))
                .filter(server -> Filters.online().test(server))
                .map(RegisteredServer.class::cast);
        return Sort.sort(sorts, serverStream).toList();
    }

    public RedisClient getRedisClient() {
        return this.redisClient;
    }

    public ServerController getServerController() {
        return this.serverController;
    }

    public ServerCache getServerCache() {
        return this.serverCache;
    }

    public PlayerCache getPlayerCache() {
        return this.playerCache;
    }
}
