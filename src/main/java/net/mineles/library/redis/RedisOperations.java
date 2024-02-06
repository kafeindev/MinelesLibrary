package net.mineles.library.redis;

import com.google.common.collect.Maps;
import net.mineles.library.redis.codec.Decoder;
import net.mineles.library.redis.message.Message;
import net.mineles.library.redis.message.MessageListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.mineles.library.libs.jedis.JedisPool;

import java.util.Map;
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

    public void publish(@NotNull String channel,
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
        if (channel.split(":").length == 2) {
            key = channel.split(":")[1];
            channel = channel.split(":")[0];
        }

        Decoder<T> decoder = this.client.getDecoder(message.getClass());
        checkNotNull(decoder, "Decoder for " + message.getClass().getName() + " is not registered");

        publish(channel, key, decoder.encode(message, this.client));
    }

    public void publish(@NotNull String channel,
                        @NotNull String message) {
        publish(channel, null, message);
    }

    public void publish(@NotNull String channel,
                        @Nullable String key,
                        @NotNull String message) {
        if (channel.split(":").length == 2) {
            key = channel.split(":")[1];
            channel = channel.split(":")[0];
        }

        RedisPublisher publisher = new RedisPublisher(this, channel);
        publisher.publish(key, message);
    }

    public Map<String, RedisSubscription> getSubscriptions() {
        return this.subscriptions;
    }

    public boolean isSubscribed(@NotNull String channel) {
        if (channel.split(":").length == 2) {
            channel = channel.split(":")[0];
        }

        return this.subscriptions.containsKey(channel);
    }

    public RedisSubscription subscribe(@NotNull ExecutorService executorService,
                                       @NotNull String channel) {
        if (channel.split(":").length == 2) {
            channel = channel.split(":")[0];
        }

        if (isSubscribed(channel)) {
            return this.subscriptions.get(channel);
        }

        RedisSubscription subscription = new RedisSubscription(this, channel);
        executorService.submit(subscription);

        this.subscriptions.put(channel, subscription);
        return subscription;
    }

    public RedisSubscription subscribe(@NotNull ExecutorService executorService,
                                       @NotNull String channel,
                                       @NotNull MessageListener mainListener) {
        if (channel.split(":").length == 2) {
            Map<String, MessageListener> listeners = Maps.newConcurrentMap();
            listeners.put(channel.split(":")[1], mainListener);

            return subscribe(executorService, channel.split(":")[0], listeners);
        }

        if (isSubscribed(channel)) {
            RedisSubscription subscription = this.subscriptions.get(channel);
            subscription.setMainListener(mainListener);
            return subscription;
        }

        RedisSubscription subscription = new RedisSubscription(this, channel, mainListener);
        executorService.submit(subscription);

        this.subscriptions.put(channel, subscription);
        return subscription;
    }

    public RedisSubscription subscribe(@NotNull ExecutorService executorService,
                                       @NotNull String channel,
                                       @NotNull Map<String, MessageListener> listeners) {
        if (channel.split(":").length == 2) {
            channel = channel.split(":")[0];
        }

        if (isSubscribed(channel)) {
            RedisSubscription subscription = this.subscriptions.get(channel);
            subscription.registerListeners(listeners);
            return subscription;
        }

        RedisSubscription subscription = new RedisSubscription(this, channel, listeners);
        executorService.submit(subscription);

        this.subscriptions.put(channel, subscription);
        return subscription;
    }

    public RedisSubscription subscribe(@NotNull ExecutorService executorService,
                                       @NotNull String channel,
                                       @NotNull MessageListener mainListener,
                                       @NotNull Map<String, MessageListener> listeners) {
        if (channel.split(":").length == 2) {
            Map<String, MessageListener> newListeners = Maps.newConcurrentMap();
            newListeners.put(channel.split(":")[1], mainListener);
            newListeners.putAll(listeners);

            return subscribe(executorService, channel.split(":")[0], newListeners);
        }

        if (isSubscribed(channel)) {
            RedisSubscription subscription = this.subscriptions.get(channel);
            subscription.setMainListener(mainListener);
            subscription.registerListeners(listeners);
            return subscription;
        }

        RedisSubscription subscription = new RedisSubscription(this, channel, mainListener, listeners);
        executorService.submit(subscription);

        this.subscriptions.put(channel, subscription);
        return subscription;
    }

    public void unsubscribe(@NotNull String channel) {
        if (channel.split(":").length == 2) {
            channel = channel.split(":")[0];
        }

        this.subscriptions.remove(channel).unsubscribe();
    }

    public void unsubscribeAll() {
        this.subscriptions.values().forEach(RedisSubscription::unsubscribe);
        this.subscriptions.clear();
    }

    public void registerListener(@NotNull String channel,
                                 @NotNull MessageListener listener) {
        if (channel.split(":").length == 2) {
            registerListener(channel.split(":")[0], channel.split(":")[1], listener);
            return;
        }

        RedisSubscription subscription = this.subscriptions.get(channel);
        checkNotNull(subscription, "Cannot register listener for channel " + channel + " as it is not subscribed to");

        subscription.setMainListener(listener);
    }

    public void registerListener(@NotNull String channel,
                                 @NotNull String key,
                                 @NotNull MessageListener listener) {
        if (channel.split(":").length == 2) {
            key = channel.split(":")[1];
            channel = channel.split(":")[0];
        }

        RedisSubscription subscription = this.subscriptions.get(channel);
        checkNotNull(subscription, "Cannot register listener for channel " + channel + " as it is not subscribed to");

        subscription.registerListener(key, listener);
    }

    public void unregisterListener(@NotNull String channel) {
        if (channel.split(":").length == 2) {
            channel = channel.split(":")[0];
        }

        RedisSubscription subscription = this.subscriptions.get(channel);
        checkNotNull(subscription, "Cannot unregister listener for channel " + channel + " as it is not subscribed to");

        subscription.setMainListener(null);
    }

    public void unregisterListener(@NotNull String channel,
                                   @NotNull String key) {
        if (channel.split(":").length == 2) {
            key = channel.split(":")[1];
            channel = channel.split(":")[0];
        }

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
