package net.mineles.library.configuration;

import net.mineles.library.node.Node;
import net.mineles.library.utils.reflect.Fields;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;

public final class KeyInjector {
    private KeyInjector() {}

    public void inject(@NotNull Class<?> clazz, @NotNull Config config) {
        inject(clazz, config.getNode());
    }

    public void inject(@NotNull Class<?> clazz, @NotNull Node node) {
        for (Field field : clazz.getDeclaredFields()) {
            inject(field, node);
        }
    }

    public void inject(@NotNull Field field, @NotNull Node parentNode) {
        if (!field.getType().isAssignableFrom(ConfigKey.class)) {
            return;
        }

        try {
            ConfigKey<?> key = (ConfigKey<?>) field.get(null);

            Node node = parentNode.node(Arrays.asList(key.getPath()));
            Object value = node.get(key.getValue().getClass());

            Fields.set(field, key, value, "value");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to inject config key", e);
        }
    }
}
