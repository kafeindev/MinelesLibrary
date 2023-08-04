/*
 * MIT License
 *
 * Copyright (c) 2023 MinelesNetwork
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.mineles.library.utils.text;

import com.google.common.collect.ImmutableMap;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class PlaceholderParser {
    private static final LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.builder()
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("%(.*?)%|\\{(.*?)\\}|:(.*?):");

    @NotNull
    public static Component parseComponent(@NotNull String message,
                                           @NotNull Map<String, String> placeholders) {
        message = convertPlaceholderKeys(message, placeholders.keySet());

        TagResolver.Single[] tagResolvers = placeholders.entrySet().stream()
                .map(entry -> {
                    String key = convert(entry.getKey());
                    String value = entry.getValue();

                    try {
                        return Placeholder.component(key, MINI_MESSAGE.deserialize(value));
                    }catch (Exception e) {
                        return Placeholder.component(key, LEGACY_COMPONENT_SERIALIZER.deserialize(value));
                    }
                })
                .toArray(TagResolver.Single[]::new);

        return MINI_MESSAGE.deserialize(message, tagResolvers);
    }

    @NotNull
    public static Component parseComponent(@NotNull OfflinePlayer player,
                                           @NotNull String message,
                                           @NotNull Map<String, String> placeholders) {
        Map<String, String> newPlaceholders = new ImmutableMap.Builder<String, String>()
                .putAll(placeholders)
                .putAll(PLACEHOLDER_PATTERN.matcher(message).results()
                        .map(MatchResult::group)
                        .filter(key -> !placeholders.containsKey(key))
                        .collect(Collectors.toMap(key -> key, key -> {
                                    key = PlaceholderAPI.setPlaceholders(player, key);
                                    return key;
                                }
                        )))
                .build();

        return parseComponent(message, newPlaceholders);
    }

    @NotNull
    public static String applyPlaceholders(@NotNull String message,
                                           @NotNull Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }

        return message;
    }

    @NotNull
    public static String convertPlaceholderKeys(@NotNull String message,
                                                @NotNull Collection<String> placeholderKeys) {
        for (String placeholderKey : placeholderKeys) {
            String parsedPlaceholderKey = convert(placeholderKey);
            message = applyPlaceholders(message, Map.of(
                    "%" + parsedPlaceholderKey + "%", "<" + parsedPlaceholderKey + ">",
                    "{" + parsedPlaceholderKey + "}", "<" + parsedPlaceholderKey + ">",
                    ":" + parsedPlaceholderKey + ":", "<" + parsedPlaceholderKey + ">"
            ));
        }

        return message;
    }

    @NotNull
    private static String convert(@NotNull String message) {
        if (message.startsWith("%") || message.startsWith("{") || message.startsWith(":")) {
            message = message.substring(1);
            message = message.substring(0, message.length() - 1);
        }

        return message;
    }

    private PlaceholderParser() {}
}
