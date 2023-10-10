package net.mineles.library.configuration.serializers;

import org.checkerframework.checker.nullness.qual.Nullable;
import net.mineles.library.redis.RedisCredentials;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public final class RedisCredentialsAdapter implements TypeSerializer<RedisCredentials> {
    public static final RedisCredentialsAdapter INSTANCE = new RedisCredentialsAdapter();

    private RedisCredentialsAdapter() {}

    @Override
    public RedisCredentials deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if (!node.isMap()) {
            throw new SerializationException("Expected a map");
        }

        return RedisCredentials.fromNode(node);
    }

    @Override
    public void serialize(Type type, @Nullable RedisCredentials obj, ConfigurationNode node) throws SerializationException {
        throw new SerializationException("Cannot serialize RedisCredentials");
    }
}
