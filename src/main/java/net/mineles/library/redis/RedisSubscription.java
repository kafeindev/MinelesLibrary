package net.mineles.library.redis;

import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

public final class RedisSubscription extends JedisPubSub implements Runnable {
    private final @NotNull RedisClient client;
    private final @NotNull String channel;

    RedisSubscription(@NotNull RedisClient client,
                      @NotNull String channel) {
        this.client = client;
        this.channel = channel;
    }

    @Override
    public void run() {
        while (!isClosed()) {
            try (Jedis jedis = this.client.getJedisPool().getResource()) {
                jedis.subscribe(this, this.channel);
            } catch (JedisConnectionException e) {
                if (this.client.isClosed()) {
                    return;
                }

                try {
                    unsubscribe();
                } catch (JedisConnectionException ignored) {}

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


    }

    boolean isClosed() {
        return this.client.isClosed() || this.client.getJedisPool().isClosed() || Thread.interrupted();
    }
}
