package net.mineles.library.redis;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import net.mineles.library.redis.message.MessageDecoder;
import net.mineles.library.redis.message.MessageListener;
import net.mineles.library.utils.GsonProvider;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.Map;

public final class RedisSubscription extends JedisPubSub implements Runnable {
    private final @NotNull RedisClient client;
    private final @NotNull String channel;

    private final @NotNull Map<String, MessageListener<?>> listeners;
    private final @NotNull Map<String, MessageDecoder<?>> decoders;

    RedisSubscription(@NotNull RedisClient client,
                      @NotNull String channel) {
        this(client, channel, Maps.newConcurrentMap(), Maps.newConcurrentMap());
    }

    RedisSubscription(@NotNull RedisClient client,
                      @NotNull String channel,
                      @NotNull Map<String, MessageListener<?>> listeners) {
        this(client, channel, listeners, Maps.newConcurrentMap());
    }

    RedisSubscription(@NotNull RedisClient client,
                      @NotNull String channel,
                      @NotNull Map<String, MessageListener<?>> listeners,
                      @NotNull Map<String, MessageDecoder<?>> decoders) {
        this.client = client;
        this.channel = channel;
        this.listeners = listeners;
        this.decoders = decoders;
    }

    @Override
    public void run() {
        while (!this.client.isClosed() && !this.client.getJedisPool().isClosed() && !Thread.interrupted()) {
            try (Jedis jedis = this.client.getJedisPool().getResource()) {
                jedis.subscribe(this, this.channel);
            } catch (JedisConnectionException e) {
                if (this.client.isClosed()) {
                    return;
                }

                try {
                    unsubscribe();
                } catch (JedisConnectionException ignored) {
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Override
    public void onMessage(String channel, String message) {
        if (!channel.equals(this.channel)) {
            return;
        }

        receive(message);
    }

    private <T> void receive(@NotNull String message) {
        JsonObject parsed = GsonProvider.getGson().fromJson(message, JsonObject.class);
        String key = parsed.get("key").getAsString();

        MessageDecoder<T> decoder = getDecoder(key);
        if (decoder == null) {
            throw new IllegalStateException("No decoder found for key " + key);
        }

        MessageListener<T> listener = getListener(key);
        if (listener == null) {
            throw new IllegalStateException("No listener found for key " + key);
        }

        listener.onMessage(decoder, this.channel, decoder.decode(parsed));
    }

    @SuppressWarnings("unchecked")
    <T> MessageDecoder<T> getDecoder(@NotNull String key) {
        return (MessageDecoder<T>) this.decoders.get(key);
    }

    void registerDecoder(@NotNull String key, @NotNull MessageDecoder<?> decoder) {
        this.decoders.put(key, decoder);
    }

    void unregisterDecoder(@NotNull String key) {
        this.decoders.remove(key);
    }

    @SuppressWarnings("unchecked")
    <T> MessageListener<T> getListener(@NotNull String key) {
        return (MessageListener<T>) this.listeners.get(key);
    }

    void registerListener(@NotNull String key, @NotNull MessageListener<?> listener) {
        this.listeners.put(key, listener);
    }

    void unregisterListener(@NotNull String key) {
        this.listeners.remove(key);
    }
}
