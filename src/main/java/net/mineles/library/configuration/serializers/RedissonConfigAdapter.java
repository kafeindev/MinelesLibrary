package net.mineles.library.configuration.serializers;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.redisson.config.Config;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public final class RedissonConfigAdapter implements TypeSerializer<Config> {
    public static final RedissonConfigAdapter INSTANCE = new RedissonConfigAdapter();

    private RedissonConfigAdapter() {}

    @Override
    public Config deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if (!node.isMap()) {
            throw new SerializationException("Expected a map");
        }

        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + node.node("host").getString("localhost") + ":" + node.node("port").getInt(6379))
                .setPassword(node.node("password").getString())
                .setDatabase(node.node("database").getInt(0));
        return config;
    }

    @Override
    public void serialize(Type type, @Nullable Config obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            throw new SerializationException("Expected a map");
        }

        node.node("host").set(obj.useSingleServer().getAddress());
        node.node("password").set(obj.useSingleServer().getPassword());
        node.node("database").set(obj.useSingleServer().getDatabase());
    }
}
