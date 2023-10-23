package net.mineles.library.teleport;

import com.google.gson.JsonObject;
import net.mineles.library.redis.codec.Decoder;
import org.jetbrains.annotations.NotNull;


public final class TeleportDecoder implements Decoder<Teleport> {
    @Override
    public @NotNull Teleport decode(@NotNull JsonObject payload) {
        return Teleport.of(payload.get("player").getAsString(),
                payload.get("target").getAsString(),
                payload.get("type").getAsString());
    }

    @Override
    public @NotNull String encode(@NotNull Teleport payload) {
        return payload.toString();
    }
}
