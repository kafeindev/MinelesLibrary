package net.mineles.library.redis;

import com.google.gson.JsonObject;
import net.mineles.library.utils.GsonProvider;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;

final class RedisPublisher {
    private final @NotNull RedisClient client;
    private final @NotNull String channel;

    RedisPublisher(@NotNull RedisClient client,
                   @NotNull String channel) {
        this.client = client;
        this.channel = channel;
    }

    void publish(@NotNull String key, @NotNull String message) {
        JsonObject json = GsonProvider.getGson().fromJson(message, JsonObject.class);
        if (json == null) {
            throw new IllegalArgumentException("Message is not a valid JSON object");
        }

        if (json.get("key") == null) {
            json.addProperty("key", key);
        }

        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            jedis.publish(this.channel, json.toString());
        }
    }
}
