package net.mineles.library.redis;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.mineles.library.redis.codec.Decoder;
import net.mineles.library.redis.codec.DecoderCollection;
import net.mineles.library.redis.message.Message;
import net.mineles.library.redis.message.MessageListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.mineles.library.libs.jedis.JedisPool;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class RedisClient {
    private final RedisCredentials credentials;
    private final RedisCache cache;
    private final RedisOperations operations;
    private final DecoderCollection decoders;
    private final String executorName;

    private JedisPool jedisPool;
    private boolean closed;

    public RedisClient(RedisCredentials credentials, String executorName) {
        this.credentials = credentials;
        this.cache = new RedisCache(this);
        this.operations = new RedisOperations(this);
        this.decoders = new DecoderCollection();
        this.executorName = executorName;
    }

    public RedisCredentials getCredentials() {
        return this.credentials;
    }

    public void connect() {
        this.jedisPool = RedisConnector.connect(this.credentials);
    }

    public void disconnect() {
        this.closed = true;
        this.operations.unsubscribeAll();
        this.jedisPool.close();
    }

    public JedisPool getJedisPool() {
        return this.jedisPool;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public RedisCache getCache() {
        return this.cache;
    }

    public RedisOperations getOperations() {
        return this.operations;
    }

    public void publish(@NotNull String channel,
                        @NotNull Message message) {
        if (channel.split(":").length == 2) {
            message.setKey(channel.split(":")[1]);
            channel = channel.split(":")[0];
        }

        this.operations.publish(channel, message);
    }

    public <T> void publish(@NotNull String channel,
                            @NotNull T message) {
        if (channel.split(":").length == 2) {
            this.operations.publish(channel.split(":")[0], channel.split(":")[1], message);
        } else {
            this.operations.publish(channel, null, message);
        }
    }

    public <T> void publish(@NotNull String channel,
                            @Nullable String key,
                            @NotNull T message) {
        this.operations.publish(channel, key, message);
    }

    public void publish(@NotNull String channel,
                        @NotNull String message) {
        if (channel.split(":").length == 2) {
            this.operations.publish(channel.split(":")[0], channel.split(":")[1], message);
        } else {
            this.operations.publish(channel, null, message);
        }
    }

    public void publish(@NotNull String channel,
                        @Nullable String key,
                        @NotNull String message) {
        this.operations.publish(channel, key, message);
    }

    public RedisSubscription subscribe(@NotNull String channel) {
        ExecutorService executorService = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder()
                .setNameFormat(this.executorName + "-redis-%d")
                .setDaemon(true)
                .build());

        if (channel.split(":").length == 2) {
            channel = channel.split(":")[0];
        }
        return this.operations.subscribe(executorService, channel);
    }

    public RedisSubscription subscribe(@NotNull String channel, @NotNull MessageListener messageListener) {
        ExecutorService executorService = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder()
                .setNameFormat(this.executorName + "-redis-%d")
                .setDaemon(true)
                .build());

        if (channel.split(":").length == 2) {
            Map<String, MessageListener> listeners = Maps.newConcurrentMap();
            listeners.put(channel.split(":")[1], messageListener);

            return subscribe(channel.split(":")[0], listeners);
        } else {
            RedisSubscription subscription = this.operations.subscribe(executorService, channel);
            subscription.setMainListener(messageListener);

            return subscription;
        }
    }

    public RedisSubscription subscribe(@NotNull String channel,
                                       @NotNull Map<String, MessageListener> listeners) {
        ExecutorService executorService = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder()
                .setNameFormat(this.executorName + "-redis-%d")
                .setDaemon(true)
                .build());

        if (channel.split(":").length == 2) {
            channel = channel.split(":")[0];
        }
        return this.operations.subscribe(executorService, channel, listeners);
    }

    public void unsubscribe(@NotNull String channel) {
        if (channel.split(":").length == 2) {
            channel = channel.split(":")[0];
        }
        this.operations.unsubscribe(channel);
    }

    public boolean isSubscribed(@NotNull String channel) {
        if (channel.split(":").length == 2) {
            channel = channel.split(":")[0];
        }
        return this.operations.isSubscribed(channel);
    }

    public void registerListener(@NotNull String channel,
                                 @NotNull String key,
                                 @NotNull MessageListener listener) {
        this.operations.registerListener(channel, key, listener);
    }

    public void unregisterListener(@NotNull String channel,
                                   @NotNull String key) {
        this.operations.unregisterListener(channel, key);
    }

    public DecoderCollection getDecoders() {
        return this.decoders;
    }

    public <T> void registerDecoder(@NotNull Class<T> type, @NotNull Decoder<T> decoder) {
        this.decoders.registerDecoder(type, decoder);
    }

    public <T> void unregisterDecoder(@NotNull Class<?> type) {
        this.decoders.unregisterDecoder(type);
    }

    public <T> Decoder<T> getDecoder(@NotNull Class<?> type) {
        return this.decoders.getDecoder(type);
    }
}
