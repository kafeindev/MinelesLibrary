package net.mineles.library.plugin;

import com.google.gson.JsonObject;
import net.mineles.library.redis.RedisClient;
import net.mineles.library.redis.RedisCredentials;
import net.mineles.library.server.listeners.DispatchCommandListener;
import net.mineles.library.server.ServerManager;
import net.mineles.library.server.constants.ServerKeys;

import java.util.UUID;

public abstract class AbstractMinelesPlugin implements MinelesPlugin {
    private RedisClient redisClient;
    private ServerManager serverManager;

    private boolean enabled = false;
    private boolean disabled = false;

    @Override
    public void enable() {
        if (this.enabled) {
            throw new IllegalStateException("Plugin is already enabled.");
        }

        this.enabled = true;

        log("Connecting to Redis...");
        String executorName = getPluginName() + "-" + UUID.randomUUID().toString().substring(0, 5);
        this.redisClient = new RedisClient(getRedisCredentials(), executorName);
        this.redisClient.connect();
        this.redisClient.subscribe(ServerKeys.SERVERS_DISPATCH_COMMAND_REQUEST, new DispatchCommandListener(this));
        log("Connected to Redis.");

        this.serverManager = new ServerManager(this.redisClient);

        log("Publishing server start...");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("host", getServerAddress().toString());
        this.redisClient.publish(ServerKeys.APP_SERVER_START, jsonObject.toString());
        log("Published server start.");
    }

    @Override
    public void disable() {
        if (this.disabled) {
            throw new IllegalStateException("Plugin is already disabled.");
        }

        this.disabled = true;
        this.redisClient.disconnect();
    }

    public abstract String getPluginName();

    @Override
    public RedisClient getRedisClient() {
        return this.redisClient;
    }

    protected abstract RedisCredentials getRedisCredentials();

    @Override
    public ServerManager getServerManager() {
        return this.serverManager;
    }
}
