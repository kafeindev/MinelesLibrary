package net.mineles.library.cluster.binding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractBinding<K, V> {

    private final @NotNull K key;

    private final @Nullable V value;

    private final char separator;

    protected AbstractBinding(@NotNull K key, @Nullable V value) {
        this(key, value, ':');
    }

    protected AbstractBinding(@NotNull K key, @Nullable V value, char separator) {
        this.key = key;
        this.value = value;
        this.separator = separator;
    }

    public @NotNull String getFullBinding() {
        return String.format("%s%c%s", key, separator, value);
    }

    public @NotNull K getKey() {
        return key;
    }

    public @Nullable V getValue() {
        return value;
    }
}
