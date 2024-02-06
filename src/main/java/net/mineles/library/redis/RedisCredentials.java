package net.mineles.library.redis;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.mineles.library.libs.configurate.ConfigurationNode;
import net.mineles.library.libs.jedis.HostAndPort;

import static net.mineles.library.configuration.ConfigKey.getValueFromEnv;

public record RedisCredentials(@NotNull HostAndPort hostAndPort,
                               @Nullable String username,
                               @NotNull String password,
                               boolean useSsl) {

    public static RedisCredentials fromNode(@NotNull ConfigurationNode node) {
        return new RedisCredentials(
                HostAndPort.from(node.node("host").getString()),
                getValueFromEnv(node.node("username"), node.node("username").getString()),
                getValueFromEnv(node.node("password"), node.node("password").getString()),
                node.node("use-ssl").getBoolean(false)
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
