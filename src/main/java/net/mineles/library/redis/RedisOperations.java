package net.mineles.library.redis;

import com.google.common.collect.Maps;
import net.mineles.library.redis.codec.Decoder;
import net.mineles.library.redis.message.Message;
import net.mineles.library.redis.message.MessageListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.JedisPool;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public final class RedisOperations {
    private final @NotNull RedisClient client;
    private final @NotNull Map<String, RedisSubscription> subscriptions;

    public RedisOperations(@NotNull RedisClient client) {
        this(client, Maps.newConcurrentMap());
    }

    public RedisOperations(@NotNull RedisClient client,
                           @NotNull Map<String, RedisSubscription> subscriptions) {
        this.client = client;
        this.subscriptions = subscriptions;
    }

    public <T> void publish(@NotNull String channel,
                            @NotNull Message message) {
        publish(channel, message.getKey(), message.getPayload());
    }

    public <T> void publish(@NotNull String channel,
                            @NotNull String key,
                            @NotNull T message) {
        RedisSubscription subscription = this.subscriptions.get(channel);
        checkNotNull(subscription, "Cannot publish message to channel " + channel + " as it is not subscribed to");

        Decoder<T> decoder = this.client.getDecoder(message.getClass());
        checkNotNull(decoder, "Decoder for " + message.getClass().getName() + " is not registered");

        publish(channel, key, decoder.encode(message));
    }

    public void publish(@NotNull String channel,
                        @NotNull String key,
                        @NotNull String message) {
        RedisPublisher publisher = new RedisPublisher(this, channel);
        publisher.publish(key, message);
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
                          @NotNull Map<String, MessageListener> listeners) {
        RedisSubscription subscription = new RedisSubscription(this, channel, listeners);
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

    public void registerListener(@NotNull String channel,
                                 @NotNull String key,
                                 @NotNull MessageListener listener) {
        RedisSubscription subscription = this.subscriptions.get(channel);
        checkNotNull(subscription, "Cannot register listener for channel " + channel + " as it is not subscribed to");

        subscription.registerListener(key, listener);
    }

    public void unregisterListener(@NotNull String channel,
                                   @NotNull String key) {
        RedisSubscription subscription = this.subscriptions.get(channel);
        checkNotNull(subscription, "Cannot unregister listener for channel " + channel + " as it is not subscribed to");

        subscription.unregisterListener(key);
    }

    @NotNull RedisClient getClient() {
        return this.client;
    }

    @NotNull JedisPool getJedisPool() {
        return this.client.getJedisPool();
    }
}
