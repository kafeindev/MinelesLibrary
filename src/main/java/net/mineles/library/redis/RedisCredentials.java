package net.mineles.library.redis;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import redis.clients.jedis.HostAndPort;

public final class RedisCredentials {
    private final @NotNull HostAndPort hostAndPort;
    private final @Nullable String username;
    private final @Nullable String password;
    private final boolean useSsl;

    public RedisCredentials(@NotNull HostAndPort hostAndPort,
                            @Nullable String username,
                            @Nullable String password,
                            boolean useSsl) {
        this.hostAndPort = hostAndPort;
        this.username = username;
        this.password = password;
        this.useSsl = useSsl;
    }

    @NotNull
    public static RedisCredentials fromNode(@NotNull ConfigurationNode node) {
        return new RedisCredentials(
                HostAndPort.from(node.node("host").getString()),
                node.node("username").getString(),
                node.node("password").getString(),
                node.node("use-ssl").getBoolean()
        );
    }

    public @NotNull HostAndPort getHostAndPort() {
        return this.hostAndPort;
    }

    public @Nullable String getUsername() {
        return this.username;
    }

    public @Nullable String getPassword() {
        return this.password;
    }

    public boolean useSsl() {
        return this.useSsl;
    }
}
