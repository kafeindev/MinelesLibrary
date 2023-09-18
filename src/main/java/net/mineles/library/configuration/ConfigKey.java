package net.mineles.library.configuration;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public final class ConfigKey<T> {
    private final @NotNull String[] path;
    private final @NotNull String key;
    private final @NotNull T value;

    public ConfigKey(@NotNull String[] path, @NotNull String key, @NotNull T defaultValue) {
        this.path = path;
        this.key = key;
        this.value = defaultValue;
    }

    @NotNull
    public static <T> ConfigKey<T> of(@NotNull T defaultValue, @NotNull String... path) {
        return new ConfigKey<>(path, path[path.length - 1], defaultValue);
    }

    @NotNull
    public static <T> ConfigKey<T> of(@NotNull T defaultValue, @NotNull String[] path, @NotNull String key) {
        return new ConfigKey<>(path, key, defaultValue);
    }

    public @NotNull String[] getPath() {
        return this.path;
    }

    public @NotNull String getKey() {
        return this.key;
    }

    public @NotNull T getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ConfigKey)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        ConfigKey configKey = (ConfigKey) obj;
        return Arrays.equals(this.path, configKey.path)
                && this.key.equals(configKey.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.path, this.key);
    }

    @Override
    public String toString() {
        return "ConfigKey{" +
                "path=" + Arrays.toString(this.path) +
                ", key='" + this.key + '\'' +
                '}';
    }
}
