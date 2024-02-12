package net.mineles.library.storage;

import com.google.common.collect.Maps;

import java.util.Map;

public final class RepositoryMap<T extends Repository<?>> {
    private final Map<String, T> repositories;

    public RepositoryMap() {
        this.repositories = Maps.newHashMap();
    }

    public RepositoryMap(Map<String, T> repositories) {
        this.repositories = repositories;
    }

    public Map<String, T> getMap() {
        return this.repositories;
    }

    public T get(String name) {
        return this.repositories.get(name);
    }

    @SuppressWarnings("unchecked")
    public <E extends Repository<V>, V> E get(Class<V> clazz) {
        return (E) getMap().values().stream()
                .filter(repository -> repository.getProperties().valueClass().equals(clazz))
                .findFirst().orElse(null);
    }

    public void register(T repository) {
        this.repositories.put(repository.getProperties().name(), repository);
    }

    public void unregister(T repository) {
        this.repositories.remove(repository.getProperties().name());
    }
}
