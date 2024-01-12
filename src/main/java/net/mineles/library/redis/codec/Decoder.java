package net.mineles.library.redis.codec;

import com.google.gson.JsonObject;
import net.mineles.library.redis.RedisClient;
import net.mineles.library.utils.GsonProvider;
import org.jetbrains.annotations.NotNull;

public interface Decoder<T> {
    default @NotNull T decode(@NotNull String payload, @NotNull RedisClient redisClient) {
        return decode(GsonProvider.getGson().fromJson(payload, JsonObject.class), redisClient);
    }

    @NotNull T decode(@NotNull JsonObject payload, @NotNull RedisClient redisClient);

    @NotNull String encode(@NotNull T payload, @NotNull RedisClient redisClient);
}
