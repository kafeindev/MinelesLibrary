package net.mineles.library.redis;

import com.google.common.collect.Maps;
import net.mineles.library.plugin.BukkitPlugin;
import net.mineles.library.redis.message.Message;
import net.mineles.library.redis.message.MessageDecoder;
import net.mineles.library.redis.message.MessageListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.JedisPool;

import java.util.Map;

public final class RedisClient {
    private final @NotNull RedisCredentials credentials;
    private final @NotNull Map<String, RedisSubscription> subscriptions;

    private JedisPool jedisPool;
    private boolean closed;

    public RedisClient(@NotNull RedisCredentials credentials) {
        this.credentials = credentials;
        this.subscriptions = Maps.newConcurrentMap();
    }

    public @NotNull RedisCredentials getCredentials() {
        return this.credentials;
    }

    public void connect() {
        this.jedisPool = RedisConnector.connect(this.credentials);
    }

    public void disconnect() {
        this.closed = true;
        unsubscribeAll();
        this.jedisPool.close();
    }

    public @NotNull JedisPool getJedisPool() {
        return this.jedisPool;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public Map<String, RedisSubscription> getSubscriptions() {
        return this.subscriptions;
    }

    public boolean isSubscribed(@NotNull String channel) {
        return this.subscriptions.containsKey(channel);
    }

    public void subscribe(@NotNull Plugin plugin,
                          @NotNull String channel) {
        RedisSubscription subscription = new RedisSubscription(this, channel);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, subscription);

        this.subscriptions.put(channel, subscription);
    }

    public void subscribe(@NotNull Plugin plugin,
                          @NotNull String channel,
                          @NotNull Map<String, MessageListener<?>> listeners) {
        RedisSubscription subscription = new RedisSubscription(this, channel, listeners);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, subscription);

        this.subscriptions.put(channel, subscription);
    }

    public void subscribe(@NotNull Plugin plugin,
                          @NotNull String channel,
                          @NotNull Map<String, MessageListener<?>> listeners,
                          @NotNull Map<String, MessageDecoder<?>> decoders) {
        RedisSubscription subscription = new RedisSubscription(this, channel, listeners, decoders);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, subscription);

        this.subscriptions.put(channel, subscription);
    }

    public void unsubscribe(@NotNull String channel) {
        this.subscriptions.remove(channel).unsubscribe();
    }

    public void unsubscribeAll() {
        this.subscriptions.values().forEach(RedisSubscription::unsubscribe);
        this.subscriptions.clear();
    }

    public <T> void send(@NotNull String channel,
                         @NotNull Message message) {
        send(channel, message.getKey(), message.getPayload());
    }

    public <T> void send(@NotNull String channel,
                         @NotNull String key,
                         @NotNull T message) {
        RedisSubscription subscription = this.subscriptions.get(channel);
        if (subscription == null) {
            throw new IllegalStateException("Cannot send message to channel " + channel + " as it is not subscribed to");
        }

        MessageDecoder<T> decoder = subscription.getDecoder(key);
        if (decoder == null) {
            throw new IllegalStateException("No decoder found for key " + key);
        }

        send(channel, key, decoder.encode(message));
    }

    public void send(@NotNull String channel,
                     @NotNull String key,
                     @NotNull String message) {
        RedisPublisher publisher = new RedisPublisher(this, channel);
        publisher.publish(key, message);
    }

    public void registerDecoder(@NotNull String channel,
                                @NotNull String key,
                                @NotNull MessageDecoder<?> decoder) {
        RedisSubscription subscription = this.subscriptions.get(channel);
        if (subscription == null) {
            throw new IllegalStateException("Cannot register decoder for channel " + channel + " as it is not subscribed to");
        }

        subscription.registerDecoder(key, decoder);
    }

    public void unregisterDecoder(@NotNull String channel,
                                  @NotNull String key) {
        RedisSubscription subscription = this.subscriptions.get(channel);
        if (subscription == null) {
            throw new IllegalStateException("Cannot unregister decoder for channel " + channel + " as it is not subscribed to");
        }

        subscription.unregisterDecoder(key);
    }

    public void registerListener(@NotNull String channel,
                                 @NotNull String key,
                                 @NotNull MessageListener<?> listener) {
        RedisSubscription subscription = this.subscriptions.get(channel);
        if (subscription == null) {
            throw new IllegalStateException("Cannot register listener for channel " + channel + " as it is not subscribed to");
        }

        subscription.registerListener(key, listener);
    }

    public void unregisterListener(@NotNull String channel,
                                   @NotNull String key) {
        RedisSubscription subscription = this.subscriptions.get(channel);
        if (subscription == null) {
            throw new IllegalStateException("Cannot unregister listener for channel " + channel + " as it is not subscribed to");
        }

        subscription.unregisterListener(key);
    }
}
