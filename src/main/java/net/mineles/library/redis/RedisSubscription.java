package net.mineles.library.redis;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import net.mineles.library.redis.message.MessageListener;
import net.mineles.library.utils.GsonProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.mineles.library.libs.jedis.Jedis;
import net.mineles.library.libs.jedis.JedisPubSub;
import net.mineles.library.libs.jedis.exceptions.JedisConnectionException;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public final class RedisSubscription extends JedisPubSub implements Runnable {
    private final @NotNull RedisOperations operations;
    private final @NotNull String channel;
    private final @NotNull Map<String, MessageListener> listeners;

    private MessageListener mainListener;

    RedisSubscription(@NotNull RedisOperations operations,
                      @NotNull String channel) {
        this(operations, channel, Maps.newConcurrentMap());
    }

    RedisSubscription(@NotNull RedisOperations operations,
                      @NotNull String channel,
                      @NotNull MessageListener mainListener) {
        this(operations, channel, mainListener, Maps.newConcurrentMap());
    }

    RedisSubscription(@NotNull RedisOperations operations,
                      @NotNull String channel,
                      @NotNull Map<String, MessageListener> listeners) {
        this.operations = operations;
        this.channel = channel;
        this.listeners = listeners;
    }

    RedisSubscription(@NotNull RedisOperations operations,
                      @NotNull String channel,
                      @NotNull MessageListener mainListener,
                      @NotNull Map<String, MessageListener> listeners) {
        this.operations = operations;
        this.listeners = listeners;

        if (channel.split(":").length == 2) {
            this.channel = channel.split(":")[0];
            this.listeners.put(channel.split(":")[1], mainListener);
        } else {
            this.channel = channel;
            this.mainListener = mainListener;
        }
    }

    @Override
    public void run() {
        while (!this.operations.getClient().isClosed() && !Thread.interrupted() && !this.operations.getJedisPool().isClosed()) {
            try (Jedis jedis = this.operations.getJedisPool().getResource()) {
                jedis.subscribe(this, this.channel);
            } catch (JedisConnectionException e) {
                if (this.operations.getClient().isClosed()) {
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

        JsonObject parsed = GsonProvider.getGson().fromJson(message, JsonObject.class);

        if (parsed.has("key")) {
            String key = parsed.get("key").getAsString();
            MessageListener listener = getListener(key);
            if (listener == null) {
                return;
            }

            listener.onMessage(this.operations.getClient(), parsed);
        } else {
            MessageListener listener = getMainListener();
            if (listener == null) {
                return;
            }

            listener.onMessage(this.operations.getClient(), parsed);
        }
    }

    public @Nullable MessageListener getMainListener() {
        return this.mainListener;
    }

    public void setMainListener(@Nullable MessageListener mainListener) {
        this.mainListener = mainListener;
    }

    public MessageListener getListener(@NotNull String key) {
        return this.listeners.get(key);
    }

    public boolean isRegistered(@NotNull String key) {
        return this.listeners.containsKey(key);
    }

    public void registerListeners(@NotNull Map<String, MessageListener> listeners) {
        this.listeners.putAll(listeners);
    }

    public void registerListener(@NotNull String key, @NotNull MessageListener listener) {
        this.listeners.put(key, listener);
    }

    public void unregisterListener(@NotNull String key) {
        this.listeners.remove(key);
    }
}
