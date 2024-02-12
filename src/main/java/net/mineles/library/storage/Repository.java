package net.mineles.library.storage;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface Repository<T> {
    RepositoryProperties<T> getProperties();

    Map<String, T> load(@NotNull Iterator<String> keyValues);

    T load(@NotNull String keyValue);

    T load(@NotNull String keyValue, @NotNull T def);

    void save(@NotNull Map<String, T> map);

    void save(@NotNull String keyValue, @NotNull T value);

    void delete(@NotNull Iterator<String> keyValues);

    void delete(@NotNull String keyValue);

    default CompletableFuture<Map<String, T>> loadAsync(@NotNull Iterator<String> keyValues) {
        return CompletableFuture.supplyAsync(() -> load(keyValues))
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return null;
                });
    }

    default CompletableFuture<T> loadAsync(@NotNull String keyValue) {
        return CompletableFuture.supplyAsync(() -> load(keyValue))
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return null;
                });
    }

    default CompletableFuture<T> loadAsync(@NotNull String keyValue, @NotNull T def) {
        return CompletableFuture.supplyAsync(() -> load(keyValue, def))
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return null;
                });
    }

    default CompletableFuture<Void> saveAsync(@NotNull Map<String, T> map) {
        return CompletableFuture.runAsync(() -> save(map))
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return null;
                });
    }

    default CompletableFuture<Void> saveAsync(@NotNull String keyValue, @NotNull T value) {
        return CompletableFuture.runAsync(() -> save(keyValue, value))
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return null;
                });
    }

    default CompletableFuture<Void> deleteAsync(@NotNull Iterator<String> keyValues)  {
        return CompletableFuture.runAsync(() -> delete(keyValues))
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return null;
                });
    }

    default CompletableFuture<Void> deleteAsync(@NotNull String keyValue) {
        return CompletableFuture.runAsync(() -> delete(keyValue))
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return null;
                });
    }
}
