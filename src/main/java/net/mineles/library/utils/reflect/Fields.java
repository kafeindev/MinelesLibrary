package net.mineles.library.utils.reflect;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

@SuppressWarnings("unchecked")
public final class Fields {
    private Fields() {}

    public static void set(@NotNull Field field, @NotNull Object value) {
        set(field, null, value);
    }

    public static void set(@NotNull Field field, @Nullable Object instance, @NotNull Object value) {
        try {
            field.setAccessible(true);
            field.set(instance, value);
            field.setAccessible(false);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to set field", e);
        }
    }

    public static void set(@NotNull Field field, @NotNull Object instance, @NotNull Object value, @NotNull String subFieldName) {
        try {
            Field subField = field.getType().getDeclaredField(subFieldName);
            set(subField, instance, value);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Failed to set field", e);
        }
    }
}
