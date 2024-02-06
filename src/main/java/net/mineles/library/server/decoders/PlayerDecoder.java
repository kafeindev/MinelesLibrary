package net.mineles.library.server.decoders;

import com.google.gson.JsonObject;
import net.mineles.library.redis.RedisClient;
import net.mineles.library.redis.codec.Decoder;
import net.mineles.library.server.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class PlayerDecoder implements Decoder<Player> {
    @Override
    public @NotNull Player decode(@NotNull JsonObject payload, @NotNull RedisClient redisClient) {
        UUID uuid = UUID.fromString(payload.get("uuid").getAsString());
        String name = payload.get("name").getAsString();
        Player player = new Player(uuid, name);

        if (payload.has("currentProxy")) {
            player.setCurrentProxy(payload.get("currentProxy").getAsString());
        }
        if (payload.has("currentServer")) {
            player.setCurrentServer(payload.get("currentServer").getAsString());
        }
        if (payload.has("loggedIn")) {
            player.setLoggedIn(payload.get("loggedIn").getAsBoolean());
        }

        return player;
    }

    @Override
    public @NotNull String encode(@NotNull Player payload, @NotNull RedisClient redisClient) {
        JsonObject object = new JsonObject();
        object.addProperty("uuid", payload.getUUID().toString());
        object.addProperty("name", payload.getName());

        if (payload.getCurrentProxy() != null) {
            object.addProperty("currentProxy", payload.getCurrentProxy());
        }
        if (payload.getCurrentServer() != null) {
            object.addProperty("currentServer", payload.getCurrentServer());
        }
        object.addProperty("loggedIn", payload.isLoggedIn());
        return object.toString();
    }
}
