package net.mineles.library.storage;

public record RepositoryProperties<T>(
        String name,
        String key,
        Class<T> valueClass
) {

    public static <T> RepositoryProperties<T> of(String name, String key, Class<T> valueClass) {
        return new RepositoryProperties<T>(name, key, valueClass);
    }
}
