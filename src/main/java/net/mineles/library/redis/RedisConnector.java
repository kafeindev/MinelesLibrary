package net.mineles.library.redis;

import net.mineles.library.libs.commons.pool2.impl.GenericObjectPoolConfig;
import org.jetbrains.annotations.NotNull;
import net.mineles.library.libs.jedis.Jedis;
import net.mineles.library.libs.jedis.JedisPool;
import net.mineles.library.libs.jedis.JedisPoolConfig;
import net.mineles.library.libs.jedis.Protocol;

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
