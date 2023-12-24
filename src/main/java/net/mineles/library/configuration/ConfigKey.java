package net.mineles.library.configuration;

import java.util.Arrays;
import java.util.Objects;

public final class ConfigKey<T> {
    private final String[] path;
    private final String key;
    private final T value;

    public ConfigKey(String[] path, String key, T defaultValue) {
        this.path = path;
        this.key = key;
        this.value = defaultValue;
    }

    public static <T> ConfigKey<T> of(T defaultValue, String... path) {
        return new ConfigKey<>(path, path[path.length - 1], defaultValue);
    }

    public static <T> ConfigKey<T> of(T defaultValue, String[] path, String key) {
        return new ConfigKey<>(path, key, defaultValue);
    }

    public String[] getPath() {
        return this.path;
    }

    public String getKey() {
        return this.key;
    }

    public T getValue() {
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
