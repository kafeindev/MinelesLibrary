package net.mineles.library.redis;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import redis.clients.jedis.HostAndPort;

public record RedisCredentials(@NotNull HostAndPort hostAndPort,
                               @Nullable String username,
                               @NotNull String password,
                               boolean useSsl) {

    public static RedisCredentials fromNode(@NotNull ConfigurationNode node) {
        return new RedisCredentials(
                HostAndPort.from(node.node("host").getString()),
                node.node("username").getString(),
                node.node("password").getString(),
                node.node("use-ssl").getBoolean()
        );
    }

    public static RedisCredentials empty() {
        return new RedisCredentials(
                HostAndPort.from("localhost:6379"),
                null,
                "",
                false
        );
    }
}
