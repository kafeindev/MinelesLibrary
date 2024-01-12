package net.mineles.library.teleport;

import com.google.gson.JsonObject;
import net.mineles.library.redis.RedisClient;
import net.mineles.library.redis.codec.Decoder;
import org.jetbrains.annotations.NotNull;


public final class TeleportDecoder implements Decoder<Teleport> {
    @Override
    public @NotNull Teleport decode(@NotNull JsonObject payload, @NotNull RedisClient redisClient) {
        return Teleport.of(payload.get("player").getAsString(),
                payload.get("target").getAsString(),
                payload.get("type").getAsString());
    }

    @Override
    public @NotNull String encode(@NotNull Teleport payload, @NotNull RedisClient redisClient) {
        return payload.toString();
    }
}
