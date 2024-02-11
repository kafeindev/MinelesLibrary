package net.mineles.library.serializer;

import com.google.common.collect.Maps;

import java.util.Map;

public final class SerializerCollection<T> {
    private final Map<Class<?>, Serializer<?, T>> serializers;

    public SerializerCollection() {
        this.serializers = Maps.newHashMap();
    }

    public SerializerCollection(Map<Class<?>, Serializer<?, T>> serializers) {
        this.serializers = serializers;
    }

    public Map<Class<?>, Serializer<?, T>> getSerializers() {
        return serializers;
    }

    @SuppressWarnings("unchecked")
    public <S> Serializer<S, T> get(Class<S> type) {
        return (Serializer<S, T>) serializers.get(type);
    }

    public <S> void register(Class<S> type, Serializer<S, T> serializer) {
        serializers.put(type, serializer);
    }

    public <S> void unregister(Class<S> type) {
        serializers.remove(type);
    }

    public <S> boolean isRegistered(Class<S> type) {
        return serializers.containsKey(type);
    }

    public <S> boolean isRegistered(Serializer<S, T> serializer) {
        return serializers.containsValue(serializer);
    }
}
