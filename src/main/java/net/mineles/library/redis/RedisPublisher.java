package net.mineles.library.redis;

import com.google.gson.JsonObject;
import net.mineles.library.utils.GsonProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.mineles.library.libs.jedis.Jedis;

import static com.google.common.base.Preconditions.checkNotNull;

final class RedisPublisher {
    private final RedisOperations pubSub;
    private final String channel;

    RedisPublisher(RedisOperations pubSub, String channel) {
        this.pubSub = pubSub;
        this.channel = channel;
    }

    void publish(@Nullable String key, @NotNull String message) {
        JsonObject json = GsonProvider.getGson().fromJson(message, JsonObject.class);
        checkNotNull(json, "Message is not a valid JSON object");

        if (key != null && json.get("key") == null) {
            json.addProperty("key", key);
        }

        try (Jedis jedis = this.pubSub.getJedisPool().getResource()) {
            jedis.publish(this.channel, json.toString());
        }
    }
}
