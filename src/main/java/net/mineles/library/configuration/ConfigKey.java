package net.mineles.library.configuration;

import net.mineles.library.utils.ArrayUtils;
import org.jetbrains.annotations.Nullable;
import net.mineles.library.libs.configurate.ConfigurationNode;

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

    public static @Nullable String getValueFromEnv(String[] path) {
        return System.getenv(String.join("_", path).toUpperCase());
    }

    public static @Nullable String getValueFromEnv(ConfigurationNode node) {
        Object[] path = node.path().array();

        String[] stringPath = new String[path.length];
        for (int i = 0; i < path.length; i++) {
            stringPath[i] = path[i].toString();
        }

        return getValueFromEnv(stringPath);
    }

    public static @Nullable String getValueFromEnv(ConfigurationNode node, String defaultValue) {
        String value = getValueFromEnv(node);
        return value == null ? defaultValue : value;
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
