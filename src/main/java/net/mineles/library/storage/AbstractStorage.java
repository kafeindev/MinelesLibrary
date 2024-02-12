package net.mineles.library.storage;

import net.mineles.library.serializer.Serializer;
import net.mineles.library.serializer.SerializerCollection;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractStorage<R extends Repository<?>, S> implements Storage<R, S> {
    protected final RepositoryMap<R> repositoryMap;
    protected final SerializerCollection<S> serializerCollection;

    public AbstractStorage() {
        this.repositoryMap = new RepositoryMap<R>();
        this.serializerCollection = new SerializerCollection<S>();
    }

    public AbstractStorage(RepositoryMap<R> repositoryMap, SerializerCollection<S> serializerCollection) {
        this.repositoryMap = repositoryMap;
        this.serializerCollection = serializerCollection;
    }

    @Override
    public RepositoryMap<R> getRepositoryMap() {
        return this.repositoryMap;
    }

    @Override
    public @Nullable R getRepository(String name) {
        return this.repositoryMap.get(name);
    }

    @Override
    public void registerRepository(R repository) {
        this.repositoryMap.register(repository);
    }

    @Override
    public void unregisterRepository(R repository) {
        this.repositoryMap.unregister(repository);
    }

    @Override
    public SerializerCollection<S> getSerializerCollection() {
        return this.serializerCollection;
    }

    @Override
    public @Nullable <T> Serializer<T, S> getSerializer(Class<T> clazz) {
        return this.serializerCollection.get(clazz);
    }

    @Override
    public <T> void registerSerializer(Class<T> clazz, Serializer<T, S> serializer) {
        this.serializerCollection.register(clazz, serializer);
    }

    @Override
    public <T> void unregisterSerializer(Class<T> clazz) {
        this.serializerCollection.unregister(clazz);
    }
}
