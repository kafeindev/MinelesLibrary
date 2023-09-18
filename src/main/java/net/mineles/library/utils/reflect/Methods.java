package net.mineles.library.utils.reflect;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings("unchecked")
public final class Methods {
    private Methods() {}

    public static void invoke(@NotNull Method method) {
        invoke(method, null);
    }

    public static void invoke(@NotNull Method method, @NotNull Object... args) {
        invoke(method, null, args);
    }

    public static void invoke(@NotNull Method method, @NotNull Object instance, @NotNull Object... args) {
        try {
            method.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to invoke method", e);
        }
    }
}
