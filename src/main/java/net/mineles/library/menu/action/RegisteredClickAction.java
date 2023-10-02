package net.mineles.library.menu.action;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public final class RegisteredClickAction {
    private final @NotNull String key;
    private final @Nullable String value;

    public RegisteredClickAction(@NotNull String key, @Nullable String value) {
        this.key = key;
        this.value = value;
    }

    @NotNull
    public static RegisteredClickAction of(@NotNull String keyValue) {
        String key = keyValue.substring(1, keyValue.indexOf("]")).toUpperCase(Locale.ENGLISH);
        String value = keyValue.substring(keyValue.indexOf("]") + 2);
        return new RegisteredClickAction(key, value);
    }

    @NotNull
    public static RegisteredClickAction of(@NotNull String key, @Nullable String value) {
        return new RegisteredClickAction(key, value);
    }

    public @NotNull String getKey() {
        return this.key;
    }

    public @Nullable String getValue() {
        return this.value;
    }
}
