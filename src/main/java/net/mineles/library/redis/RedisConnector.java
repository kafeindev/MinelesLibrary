package net.mineles.library.redis;

import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

final class RedisConnector {
    RedisConnector() {}

    static @NotNull JedisPool connect(@NotNull RedisCredentials credentials) {
        return switch (credentials.getType()) {
            case WITH_USERNAME_AND_PASSWORD -> new JedisPool(new JedisPoolConfig(),
                    credentials.getHostAndPort().getHost(),
                    credentials.getHostAndPort().getPort(),
                    Protocol.DEFAULT_TIMEOUT,
                    credentials.getUsername(),
                    credentials.getPassword(),
                    credentials.useSsl());
            case WITH_PASSWORD -> new JedisPool(new JedisPoolConfig(),
                    credentials.getHostAndPort().getHost(),
                    credentials.getHostAndPort().getPort(),
                    Protocol.DEFAULT_TIMEOUT,
                    credentials.getPassword(),
                    credentials.useSsl());
        };
    }
}
