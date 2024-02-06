package net.mineles.library.redis;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.mineles.library.redis.codec.Decoder;
import org.jetbrains.annotations.NotNull;
import net.mineles.library.libs.jedis.Jedis;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public final class RedisCache {
    private final RedisClient client;

    public RedisCache(RedisClient client) {
        this.client = client;
    }

    public boolean exists(@NotNull String key) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            return jedis.exists(key);
        }
    }

    public String get(@NotNull String key) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            return jedis.get(key);
        }
    }

    public <T> T getDecoded(@NotNull String key,
                            @NotNull Class<T> type) {
        Decoder<T> decoder = this.client.getDecoders().getDecoder(type);
        checkNotNull(decoder, "Decoder for key " + key + " is not registered");

        return getDecoded(key, decoder);
    }

    public <T> T getDecoded(@NotNull String key,
                            @NotNull Decoder<T> decoder) {
        String value = get(key);
        if (value == null) {
            return null;
        }

        return decoder.decode(value, this.client);
    }

    public Set<String> keys(@NotNull String pattern) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            return jedis.keys(pattern);
        }
    }

    public boolean sismember(@NotNull String key,
                             @NotNull String member) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            return jedis.sismember(key, member);
        }
    }

    public Set<String> smembers(@NotNull String key) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            return jedis.smembers(key);
        }
    }

    public <T> Set<T> smembersDecoded(@NotNull String key,
                                      @NotNull Class<T> type) {
        Decoder<T> decoder = this.client.getDecoders().getDecoder(type);
        checkNotNull(decoder, "Decoder for key " + key + " is not registered");

        return smembersDecoded(key, decoder);
    }

    public <T> Set<T> smembersDecoded(@NotNull String key,
                                      @NotNull Decoder<T> decoder) {
        Set<String> members = smembers(key);
        Set<T> decoded = Sets.newHashSet();
        for (String member : members) {
            decoded.add(decoder.decode(member, this.client));
        }

        return decoded;
    }

    public long scard(@NotNull String key) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            return jedis.scard(key);
        }
    }

    public List<String> lrange(@NotNull String key,
                               long start,
                               long end) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            return jedis.lrange(key, start, end);
        }
    }

    public <T> List<T> lrangeDecoded(@NotNull String key,
                                     long start,
                                     long end,
                                     @NotNull Class<T> type) {
        Decoder<T> decoder = this.client.getDecoders().getDecoder(type);
        checkNotNull(decoder, "Decoder for key " + key + " is not registered");

        return lrangeDecoded(key, start, end, decoder);
    }

    public <T> List<T> lrangeDecoded(@NotNull String key,
                                     long start,
                                     long end,
                                     @NotNull Decoder<T> decoder) {
        List<String> members = lrange(key, start, end);
        List<T> decoded = Lists.newArrayList();
        for (String member : members) {
            decoded.add(decoder.decode(member, this.client));
        }

        return decoded;
    }

    public String lindex(@NotNull String key,
                         long index) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            return jedis.lindex(key, index);
        }
    }

    public <T> T lindexDecoded(@NotNull String key,
                               long index,
                               @NotNull Class<T> type) {
        Decoder<T> decoder = this.client.getDecoders().getDecoder(type);
        checkNotNull(decoder, "Decoder for key " + key + " is not registered");

        return lindexDecoded(key, index, decoder);
    }

    public <T> T lindexDecoded(@NotNull String key,
                               long index,
                               @NotNull Decoder<T> decoder) {
        String member = lindex(key, index);
        if (member == null) {
            return null;
        }

        return decoder.decode(member, this.client);
    }

    public long llen(@NotNull String key) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            return jedis.llen(key);
        }
    }

    public long hlen(@NotNull String key) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            return jedis.hlen(key);
        }
    }

    public Set<String> hkeys(@NotNull String key) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            return jedis.hkeys(key);
        }
    }

    public Map<String, String> hgetAll(@NotNull String key) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            return jedis.hgetAll(key);
        }
    }

    public <T> Map<String, T> hgetAllDecoded(@NotNull String key,
                                             @NotNull Class<T> type) {
        Decoder<T> decoder = this.client.getDecoders().getDecoder(type);
        checkNotNull(decoder, "Decoder for key " + key + " is not registered");

        return hgetAllDecoded(key, decoder);
    }

    public <T> Map<String, T> hgetAllDecoded(@NotNull String key,
                                             @NotNull Decoder<T> decoder) {
        Map<String, String> members = hgetAll(key);
        Map<String, T> decoded = Maps.newHashMap();
        for (Map.Entry<String, String> entry : members.entrySet()) {
            decoded.put(entry.getKey(), decoder.decode(entry.getValue(), this.client));
        }

        return decoded;
    }

    public String hget(@NotNull String key,
                       @NotNull String field) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            return jedis.hget(key, field);
        }
    }

    public <T> T hgetDecoded(@NotNull String key,
                             @NotNull String field,
                             @NotNull Class<T> type) {
        Decoder<T> decoder = this.client.getDecoders().getDecoder(type);
        checkNotNull(decoder, "Decoder for key " + key + " is not registered");

        return hgetDecoded(key, field, decoder);
    }

    public <T> T hgetDecoded(@NotNull String key,
                             @NotNull String field,
                             @NotNull Decoder<T> decoder) {
        String member = hget(key, field);
        if (member == null) {
            return null;
        }

        return decoder.decode(member, this.client);
    }

    public boolean hexists(@NotNull String key,
                           @NotNull String field) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            return jedis.hexists(key, field);
        }
    }

    public void set(@NotNull String key,
                    @NotNull String value) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            jedis.set(key, value);
        }
    }

    public void set(@NotNull String key,
                    @NotNull String value,
                    @NotNull Duration duration) {
        set(key, value, duration.toMillis());
    }

    public void set(@NotNull String key,
                    @NotNull String value,
                    long duration) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            jedis.psetex(key, duration, value);
        }
    }

    public <T> void setDecoded(@NotNull String key,
                               @NotNull T value,
                               @NotNull Class<T> type) {
        Decoder<T> decoder = this.client.getDecoders().getDecoder(type);
        checkNotNull(decoder, "Decoder for key " + key + " is not registered");

        setDecoded(key, value, decoder);
    }

    public <T> void setDecoded(@NotNull String key,
                               @NotNull T value,
                               @NotNull Decoder<T> decoder) {
        set(key, decoder.encode(value, this.client));
    }

    public <T> void setDecoded(@NotNull String key,
                               @NotNull T value,
                               @NotNull Decoder<T> decoder,
                               @NotNull Duration duration) {
        setDecoded(key, value, decoder, duration.toMillis());
    }

    public <T> void setDecoded(@NotNull String key,
                               @NotNull T value,
                               @NotNull Decoder<T> decoder,
                               long duration) {
        set(key, decoder.encode(value, this.client), duration);
    }

    public void sadd(@NotNull String key,
                     @NotNull String... members) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            jedis.sadd(key, members);
        }
    }

    public <T> void saddDecoded(@NotNull String key,
                                @NotNull Class<T> type,
                                @NotNull T... members) {
        Decoder<T> decoder = this.client.getDecoders().getDecoder(type);
        checkNotNull(decoder, "Decoder for key " + key + " is not registered");

        saddDecoded(key, decoder, members);
    }

    public <T> void saddDecoded(@NotNull String key,
                                @NotNull Decoder<T> decoder,
                                @NotNull T... members) {
        String[] encoded = new String[members.length];
        for (int i = 0; i < members.length; i++) {
            encoded[i] = decoder.encode(members[i], this.client);
        }

        sadd(key, encoded);
    }

    public void lpush(@NotNull String key,
                      @NotNull String... members) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            jedis.lpush(key, members);
        }
    }

    public <T> void lpushDecoded(@NotNull String key,
                                 @NotNull Class<T> type,
                                 @NotNull T... members) {
        Decoder<T> decoder = this.client.getDecoders().getDecoder(type);
        checkNotNull(decoder, "Decoder for key " + key + " is not registered");

        lpushDecoded(key, decoder, members);
    }

    public <T> void lpushDecoded(@NotNull String key,
                                 @NotNull Decoder<T> decoder,
                                 @NotNull T... members) {
        String[] encoded = new String[members.length];
        for (int i = 0; i < members.length; i++) {
            encoded[i] = decoder.encode(members[i], this.client);
        }

        lpush(key, encoded);
    }

    public void hset(@NotNull String key,
                     @NotNull String field,
                     @NotNull String value) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            jedis.hset(key, field, value);
        }
    }

    public void hset(@NotNull String key,
                     @NotNull String field,
                     @NotNull String value,
                     @NotNull Duration duration) {
        hset(key, field, value, duration.toMillis());
    }

    public void hset(@NotNull String key,
                     @NotNull String field,
                     @NotNull String value,
                     long duration) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            jedis.hset(key, field, value);
            jedis.pexpire(key, duration);
        }
    }

    public <T> void hsetDecoded(@NotNull String key,
                                @NotNull String field,
                                @NotNull T value,
                                @NotNull Class<T> type) {
        Decoder<T> decoder = this.client.getDecoders().getDecoder(type);
        checkNotNull(decoder, "Decoder for key " + key + " is not registered");

        hsetDecoded(key, field, value, decoder);
    }

    public <T> void hsetDecoded(@NotNull String key,
                                @NotNull String field,
                                @NotNull T value,
                                @NotNull Decoder<T> decoder) {
        hset(key, field, decoder.encode(value, this.client));
    }

    public <T> void hsetDecoded(@NotNull String key,
                                @NotNull String field,
                                @NotNull T value,
                                @NotNull Decoder<T> decoder,
                                @NotNull Duration duration) {
        hsetDecoded(key, field, value, decoder, duration.toMillis());
    }

    public <T> void hsetDecoded(@NotNull String key,
                                @NotNull String field,
                                @NotNull T value,
                                @NotNull Decoder<T> decoder,
                                long duration) {
        hset(key, field, decoder.encode(value, this.client), duration);
    }

    public void expire(@NotNull String key,
                       @NotNull Duration duration) {
        expire(key, duration.toMillis());
    }

    public void expire(@NotNull String key,
                       long duration) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            jedis.pexpire(key, duration);
        }
    }

    public void delete(@NotNull String key) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            jedis.del(key);
        }
    }

    public void delete(@NotNull String... keys) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            jedis.del(keys);
        }
    }

    public void srem(@NotNull String key,
                     @NotNull String... members) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            jedis.srem(key, members);
        }
    }

    public void lrem(@NotNull String key,
                     long count,
                     @NotNull String member) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            jedis.lrem(key, count, member);
        }
    }

    public void hdel(@NotNull String key,
                     @NotNull String... fields) {
        try (Jedis jedis = this.client.getJedisPool().getResource()) {
            jedis.hdel(key, fields);
        }
    }
}
