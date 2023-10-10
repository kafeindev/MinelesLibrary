package net.mineles.library.redis;

import com.google.common.collect.Maps;
import net.mineles.library.plugin.BukkitPlugin;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.JedisPool;

import java.util.Map;

public final class RedisClient {
    private final @NotNull BukkitPlugin plugin;
    private final @NotNull RedisCredentials credentials;
    private final @NotNull Map<String, RedisSubscription> subscriptions;

    private JedisPool jedisPool;
    private boolean closed;

    public RedisClient(@NotNull BukkitPlugin plugin,
                       @NotNull RedisCredentials credentials) {
        this.plugin = plugin;
        this.credentials = credentials;
        this.subscriptions = Maps.newConcurrentMap();
    }

    public void connect() {
        this.jedisPool = RedisConnector.connect(this.credentials);
    }

    public void disconnect() {
        this.closed = true;
        unsubscribeAll();
        this.jedisPool.close();
    }

    public void subscribe(@NotNull String channel) {
        RedisSubscription subscription = new RedisSubscription(this, channel);
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin.getPlugin(), subscription);

        this.subscriptions.put(channel, new RedisSubscription(this, channel));
    }

    public void unsubscribe(@NotNull String channel) {
        this.subscriptions.remove(channel).unsubscribe();
    }

    public void unsubscribeAll() {
        this.subscriptions.values().forEach(RedisSubscription::unsubscribe);
        this.subscriptions.clear();
    }

    public @NotNull JedisPool getJedisPool() {
        return this.jedisPool;
    }

    public boolean isClosed() {
        return this.closed;
    }
}
