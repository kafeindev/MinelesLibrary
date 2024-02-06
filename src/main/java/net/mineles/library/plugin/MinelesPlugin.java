package net.mineles.library.plugin;

import net.mineles.library.connection.HostAndPort;
import net.mineles.library.redis.RedisClient;
import net.mineles.library.server.ServerManager;

public interface MinelesPlugin {
    void load();

    void enable();

    void disable();

    void stopServer();

    void dispatchCommand(String command);

    void log(String message);

    HostAndPort getServerAddress();

    RedisClient getRedisClient();

    ServerManager getServerManager();
}
