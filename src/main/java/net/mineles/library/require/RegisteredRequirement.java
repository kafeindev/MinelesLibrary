package net.mineles.library.require;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public final class RegisteredRequirement {
    private final String key;
    private final String value;

    public RegisteredRequirement(@NotNull String key,
                                 @NotNull String value) {
        this.key = key;
        this.value = value;
    }

    public static RegisteredRequirement of(@NotNull String keyValue) {
        String key = keyValue.substring(1, keyValue.indexOf("]")).toUpperCase(Locale.ENGLISH);
        String value = keyValue.substring(keyValue.indexOf("]") + 2);
        return new RegisteredRequirement(key, value);
    }

    public static RegisteredRequirement of(@NotNull String key,
                                           @NotNull String value) {
        return new RegisteredRequirement(key, value);
    }

    public @NotNull String getKey() {
        return this.key;
    }

    public @NotNull String getValue() {
        return this.value;
    }
}
