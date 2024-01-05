package net.mineles.library.redis;

import com.google.common.collect.Maps;
import net.mineles.library.redis.codec.Decoder;
import net.mineles.library.redis.message.Message;
import net.mineles.library.redis.message.MessageListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.google.common.base.Preconditions.checkNotNull;

public final class RedisOperations {
    private final RedisClient client;
    private final Map<String, RedisSubscription> subscriptions;

    public RedisOperations(RedisClient client) {
        this(client, Maps.newConcurrentMap());
    }

    public RedisOperations(RedisClient client, Map<String, RedisSubscription> subscriptions) {
        this.client = client;
        this.subscriptions = subscriptions;
    }

    public <T> void publish(@NotNull String channel,
                            @NotNull Message message) {
        publish(channel, message.getKey(), message.getPayload());
    }

    public <T> void publish(@NotNull String channel,
                            @NotNull T message) {
        publish(channel, null, message);
    }

    public <T> void publish(@NotNull String channel,
                            @Nullable String key,
                            @NotNull T message) {
        Decoder<T> decoder = this.client.getDecoder(message.getClass());
        checkNotNull(decoder, "Decoder for " + message.getClass().getName() + " is not registered");

        publish(channel, key, decoder.encode(message));
    }

    public <T> void publish(@NotNull String channel,
                            @NotNull String message) {
        publish(channel, null, message);
    }

    public void publish(@NotNull String channel,
                        @Nullable String key,
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

    public RedisSubscription subscribe(@NotNull ExecutorService executorService,
                                       @NotNull String channel) {
        RedisSubscription subscription = new RedisSubscription(this, channel);
        CompletableFuture.runAsync(subscription, executorService);

        this.subscriptions.put(channel, subscription);
        return subscription;
    }

    public RedisSubscription subscribe(@NotNull ExecutorService executorService,
                                       @NotNull String channel,
                                       @NotNull MessageListener mainListener) {
        RedisSubscription subscription = new RedisSubscription(this, channel, mainListener);
        CompletableFuture.runAsync(subscription, executorService);

        this.subscriptions.put(channel, subscription);
        return subscription;
    }

    public RedisSubscription subscribe(@NotNull ExecutorService executorService,
                                       @NotNull String channel,
                                       @NotNull Map<String, MessageListener> listeners) {
        RedisSubscription subscription = new RedisSubscription(this, channel, listeners);
        CompletableFuture.runAsync(subscription, executorService);

        this.subscriptions.put(channel, subscription);
        return subscription;
    }

    public RedisSubscription subscribe(@NotNull ExecutorService executorService,
                                       @NotNull String channel,
                                       @NotNull MessageListener mainListener,
                                       @NotNull Map<String, MessageListener> listeners) {
        RedisSubscription subscription = new RedisSubscription(this, channel, mainListener, listeners);
        CompletableFuture.runAsync(subscription, executorService);

        this.subscriptions.put(channel, subscription);
        return subscription;
    }

    public void unsubscribe(@NotNull String channel) {
        this.subscriptions.remove(channel).unsubscribe();
    }

    public void unsubscribeAll() {
        this.subscriptions.values().forEach(RedisSubscription::unsubscribe);
        this.subscriptions.clear();
    }

    public void registerListener(@NotNull String channel,
                                 @NotNull MessageListener listener) {
        RedisSubscription subscription = this.subscriptions.get(channel);
        checkNotNull(subscription, "Cannot register listener for channel " + channel + " as it is not subscribed to");

        subscription.setMainListener(listener);
    }

    public void registerListener(@NotNull String channel,
                                 @NotNull String key,
                                 @NotNull MessageListener listener) {
        RedisSubscription subscription = this.subscriptions.get(channel);
        checkNotNull(subscription, "Cannot register listener for channel " + channel + " as it is not subscribed to");

        subscription.registerListener(key, listener);
    }

    public void unregisterListener(@NotNull String channel) {
        RedisSubscription subscription = this.subscriptions.get(channel);
        checkNotNull(subscription, "Cannot unregister listener for channel " + channel + " as it is not subscribed to");

        subscription.setMainListener(null);
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
