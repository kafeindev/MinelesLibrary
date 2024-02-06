package net.mineles.library.server;

import net.mineles.library.redis.RedisCache;
import net.mineles.library.server.constants.ServerKeys;

import java.util.Map;
import java.util.UUID;

public final class PlayerCache {
    private final RedisCache cache;

    public PlayerCache(RedisCache cache) {
        this.cache = cache;
    }

    public long getPlayerCount() {
        return this.cache.hlen(ServerKeys.PLAYERS);
    }

    public Map<String, Player> getPlayers() {
        return this.cache.hgetAllDecoded(ServerKeys.PLAYERS, Player.class);
    }

    public Player getPlayer(String name) {
        return this.cache.hgetDecoded(ServerKeys.PLAYERS, name, Player.class);
    }

    public Player getPlayer(UUID uuid) {
        return this.getPlayers().values().stream()
                .filter(player -> player.getUUID().equals(uuid))
                .findFirst().orElse(null);
    }

    public boolean containsPlayer(String name) {
        return this.cache.hexists(ServerKeys.PLAYERS, name);
    }

    public boolean containsPlayer(UUID uuid) {
        return this.getPlayers().values().stream()
                .anyMatch(player -> player.getUUID().equals(uuid));
    }

    public void setPlayer(String name, Player player) {
        this.cache.hsetDecoded(ServerKeys.PLAYERS, name, player, Player.class);
    }

    public void removePlayer(String name) {
        this.cache.hdel(ServerKeys.PLAYERS, name);
    }

    public void removePlayer(UUID uuid) {
        this.getPlayers().entrySet().stream()
                .filter(entry -> entry.getValue().getUUID().equals(uuid))
                .map(Map.Entry::getKey)
                .forEach(this::removePlayer);
    }

    public void clear() {
        this.cache.delete(ServerKeys.PLAYERS);
    }
}
