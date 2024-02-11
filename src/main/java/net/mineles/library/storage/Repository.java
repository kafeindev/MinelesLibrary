package net.mineles.library.storage;

public interface Repository {
    String getName();

    default <T extends Repository> T getAs(Class<T> clazz) {
        return clazz.cast(this);
    }
}
