package net.mineles.library.server.listeners;

import com.google.gson.JsonObject;
import net.mineles.library.connection.HostAndPort;
import net.mineles.library.plugin.MinelesPlugin;
import net.mineles.library.redis.RedisClient;
import net.mineles.library.redis.message.MessageListener;
import org.jetbrains.annotations.NotNull;

public final class DispatchCommandListener implements MessageListener {
    private final MinelesPlugin plugin;

    public DispatchCommandListener(MinelesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessage(@NotNull RedisClient client, @NotNull JsonObject message) {
        HostAndPort hostAndPort = HostAndPort.of(message.get("host").getAsString());
        if (!hostAndPort.equals(plugin.getServerAddress())) {
            return;
        }

        String command = message.get("command").getAsString();
        this.plugin.dispatchCommand(command);
    }
}
