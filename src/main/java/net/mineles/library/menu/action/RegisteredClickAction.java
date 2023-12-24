package net.mineles.library.menu.action;

import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public final class RegisteredClickAction {
    private final String key;
    private final @Nullable String value;

    public RegisteredClickAction(String key, @Nullable String value) {
        this.key = key;
        this.value = value;
    }

    public static RegisteredClickAction of(String keyValue) {
        String key = keyValue.substring(1, keyValue.indexOf("]")).toUpperCase(Locale.ENGLISH);
        String value = keyValue.substring(keyValue.indexOf("]") + 2);
        return new RegisteredClickAction(key, value);
    }

    public static RegisteredClickAction of(String key, @Nullable String value) {
        return new RegisteredClickAction(key, value);
    }

    public String getKey() {
        return this.key;
    }

    public @Nullable String getValue() {
        return this.value;
    }
}
