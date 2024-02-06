package net.mineles.library.server.decoders;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.mineles.library.connection.HostAndPort;
import net.mineles.library.redis.RedisClient;
import net.mineles.library.redis.codec.Decoder;
import net.mineles.library.server.RegisteredServer;
import net.mineles.library.server.Server;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

public final class ServerDecoder implements Decoder<Server> {
    @Override
    public @NotNull Server decode(@NotNull JsonObject payload, @NotNull RedisClient redisClient) {
        String id = payload.get("id").getAsString();
        String name = payload.get("name").getAsString();
        String image = payload.get("image").getAsString();

        Server server = new Server(id, name, image);
        if (!payload.has("host")) {
            return server;
        }

        String host = payload.get("host").getAsString();

        Set<UUID> players = Sets.newHashSet();
        if (payload.has("players")) {
            JsonArray playersArray = payload.get("players").getAsJsonArray();
            playersArray.forEach(player -> players.add(UUID.fromString(player.getAsString())));
        }

        Set<UUID> entries = Sets.newHashSet();
        if (payload.has("entries")) {
            JsonArray entriesArray = payload.get("entries").getAsJsonArray();
            entriesArray.forEach(entry -> entries.add(UUID.fromString(entry.getAsString())));
        }

        return server.asRegisteredServer(HostAndPort.of(host), players, entries);
    }

    @Override
    public @NotNull String encode(@NotNull Server payload, @NotNull RedisClient redisClient) {
        JsonObject object = new JsonObject();
        object.addProperty("id", payload.getId());
        object.addProperty("name", payload.getName());
        object.addProperty("image", payload.getImage());

        if (payload instanceof RegisteredServer) {
            RegisteredServer registeredServer = (RegisteredServer) payload;
            object.addProperty("host", registeredServer.getHostAndPort().toString());

            JsonArray players = new JsonArray();
            registeredServer.getPlayers().forEach(player -> players.add(player.toString()));
            object.add("players", players);

            JsonArray entries = new JsonArray();
            registeredServer.getEntries().forEach(entry -> entries.add(entry.toString()));
            object.add("entries", entries);
        }

        return object.toString();
    }
}
