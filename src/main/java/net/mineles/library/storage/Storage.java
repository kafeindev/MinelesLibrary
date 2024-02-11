package net.mineles.library.storage;

import net.mineles.library.serializer.Serializer;
import net.mineles.library.serializer.SerializerCollection;
import org.jetbrains.annotations.Nullable;

public interface Storage<R extends Repository, S> {
    void initialize();

    void shutdown();

    RepositoryMap<R> getRepositoryMap();

    @Nullable R getRepository(String name);

    void registerRepository(R repository);

    void unregisterRepository(R repository);

    SerializerCollection<S> getSerializerCollection();

    <T> @Nullable Serializer<T, S> getSerializer(Class<T> clazz);

    <T> void registerSerializer(Class<T> clazz, Serializer<T, S> serializer);

    <T> void unregisterSerializer(Class<T> clazz);

    default <T extends Storage<R, S>> T getAs(Class<T> clazz) {
        return clazz.cast(this);
    }
}
