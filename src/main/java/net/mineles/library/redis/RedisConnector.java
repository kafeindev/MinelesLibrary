package net.mineles.library.redis;

import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

final class RedisConnector {
    RedisConnector() {}

    static @NotNull JedisPool connect(@NotNull RedisCredentials credentials) {
        if (credentials.getUsername() != null && credentials.getPassword() != null) {
            return new JedisPool(new JedisPoolConfig(),
                    credentials.getHostAndPort().getHost(),
                    credentials.getHostAndPort().getPort(),
                    Protocol.DEFAULT_TIMEOUT,
                    credentials.getUsername(),
                    credentials.getPassword(),
                    credentials.useSsl()
            );
        } else if (credentials.getPassword() != null) {
            return new JedisPool(new JedisPoolConfig(),
                    credentials.getHostAndPort().getHost(),
                    credentials.getHostAndPort().getPort(),
                    Protocol.DEFAULT_TIMEOUT,
                    credentials.getPassword(),
                    credentials.useSsl()
            );
        } else {
            return new JedisPool(new JedisPoolConfig(),
                    credentials.getHostAndPort().getHost(),
                    credentials.getHostAndPort().getPort(),
                    credentials.useSsl()
            );
        }
    }
}
