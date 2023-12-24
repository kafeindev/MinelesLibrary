package net.mineles.library.utils.text;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class PlaceholderParser {
    public static String applyPlaceholders(@NotNull String message,
                                           @NotNull Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }

        return message;
    }

    private PlaceholderParser() {}
}
