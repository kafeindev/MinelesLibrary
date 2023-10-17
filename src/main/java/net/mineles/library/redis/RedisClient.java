package net.mineles.library.redis;

import net.mineles.library.redis.codec.Decoder;
import net.mineles.library.redis.codec.DecoderCollection;
import net.mineles.library.redis.message.Message;
import net.mineles.library.redis.message.MessageListener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.JedisPool;

import java.util.Map;

public final class RedisClient {
    private final @NotNull RedisCredentials credentials;
    private final @NotNull RedisCache cache;
    private final @NotNull RedisOperations operations;
    private final @NotNull DecoderCollection decoders;

    private JedisPool jedisPool;
    private boolean closed;

    public RedisClient(@NotNull RedisCredentials credentials) {
        this.credentials = credentials;
        this.cache = new RedisCache(this);
        this.operations = new RedisOperations(this);
        this.decoders = new DecoderCollection();
    }

    public @NotNull RedisCredentials getCredentials() {
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

    public @NotNull JedisPool getJedisPool() {
        return this.jedisPool;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public @NotNull RedisCache getCache() {
        return this.cache;
    }

    public @NotNull RedisOperations getOperations() {
        return this.operations;
    }

    public <T> void publish(@NotNull String channel,
                            @NotNull Message message) {
        this.operations.publish(channel, message);
    }

    public <T> void publish(@NotNull String channel,
                            @NotNull String key,
                            @NotNull T message) {
        this.operations.publish(channel, key, message);
    }

    public void publish(@NotNull String channel,
                        @NotNull String key,
                        @NotNull String message) {
        this.operations.publish(channel, key, message);
    }

    public void subscribe(@NotNull Plugin plugin,
                          @NotNull String channel) {
        this.operations.subscribe(plugin, channel);
    }

    public void subscribe(@NotNull Plugin plugin,
                          @NotNull String channel,
                          @NotNull Map<String, MessageListener> listeners) {
        this.operations.subscribe(plugin, channel, listeners);
    }

    public void unsubscribe(@NotNull String channel) {
        this.operations.unsubscribe(channel);
    }

    public boolean isSubscribed(@NotNull String channel) {
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

    public @NotNull DecoderCollection getDecoders() {
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
