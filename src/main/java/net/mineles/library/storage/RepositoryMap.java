package net.mineles.library.storage;

import com.google.common.collect.Maps;

import java.util.Map;

public final class RepositoryMap<T extends Repository> {
    private final Map<String, T> repositories;

    public RepositoryMap() {
        this.repositories = Maps.newHashMap();
    }

    public RepositoryMap(Map<String, T> repositories) {
        this.repositories = repositories;
    }

    public Map<String, T> getRepositories() {
        return this.repositories;
    }

    public T getRepository(String name) {
        return this.repositories.get(name);
    }

    public void registerRepository(T repository) {
        this.repositories.put(repository.getName(), repository);
    }

    public void unregisterRepository(T repository) {
        this.repositories.remove(repository.getName());
    }
}
