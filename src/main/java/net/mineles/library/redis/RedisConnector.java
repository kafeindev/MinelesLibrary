package net.mineles.library.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

final class RedisConnector {

    RedisConnector() {}

    static @NotNull JedisPool connect(@NotNull RedisCredentials credentials) {
        return connect(credentials, new JedisPoolConfig());
    }

    static @NotNull JedisPool connect(@NotNull RedisCredentials credentials,
                                      @NotNull GenericObjectPoolConfig<Jedis> config) {
        return new JedisPool(config,
                credentials.hostAndPort().getHost(),
                credentials.hostAndPort().getPort(),
                Protocol.DEFAULT_TIMEOUT,
                credentials.password(),
                credentials.useSsl()
        );
    }
}
