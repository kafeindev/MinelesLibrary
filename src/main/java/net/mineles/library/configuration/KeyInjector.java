package net.mineles.library.configuration;

import net.mineles.library.utils.ArrayUtils;
import net.mineles.library.utils.reflect.Fields;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.mineles.library.libs.configurate.ConfigurationNode;
import net.mineles.library.libs.configurate.serialize.SerializationException;

import java.lang.reflect.Field;
import java.util.Collection;

final class KeyInjector {
    KeyInjector() {
    }

    void inject(@NotNull Class<?> clazz, @NotNull Config config) {
        inject(clazz, config.getNode());
    }

    void inject(@NotNull Class<?> clazz, @NotNull ConfigurationNode node) {
        for (Field field : clazz.getDeclaredFields()) {
            inject(field, node);
        }
    }

    void inject(@NotNull Field field, @NotNull Config config) {
        inject(field, config.getNode());
    }

    void inject(@NotNull Field field, @NotNull ConfigurationNode node) {
        if (!field.getType().isAssignableFrom(ConfigKey.class)) {
            return;
        }

        try {
            ConfigKey<?> key = (ConfigKey<?>) field.get(null);
            Class<?> type = key.getValue().getClass();

            ConfigurationNode child = node.node(key.getPath());
            if (child.isNull()) {
                return;
            }

            Object value = Collection.class.isAssignableFrom(type) ? child.raw() : child.get(type);
            if (String.class.isAssignableFrom(type) && (value == null || ((String) value).isEmpty())) {
                String env = ConfigKey.getValueFromEnv(key.getPath());
                if (env != null) {
                    value = env;
                }
            }

            Fields.set(field, key, value, "value");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to inject config key: " + field.getName(), e);
        } catch (SerializationException e) {
            throw new RuntimeException("Failed to deserialize config key: " + field.getName(), e);
        }
    }
}
