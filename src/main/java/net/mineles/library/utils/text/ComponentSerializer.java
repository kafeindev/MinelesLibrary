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
import com.google.common.collect.Maps;
import de.themoep.minedown.MineDown;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class ComponentSerializer {
    private static final BungeeComponentSerializer SERIALIZER = BungeeComponentSerializer.get();
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("%(.*?)%|\\{(.*?)\\}|:(.*?):");

    public static Component deserialize(@NotNull String message) {
        return deserialize(message, Maps.newHashMap());
    }

    public static Component deserialize(@NotNull String message, @NotNull Map<String, String> placeholders) {
        //message = LegacyTextConverter.convert(message);
        message = PlaceholderParser.applyPlaceholders(message, placeholders);

        BaseComponent[] baseComponent = new MineDown(message).toComponent();
        return SERIALIZER.deserialize(baseComponent);
    }

    public static Component deserialize(@NotNull OfflinePlayer player, @NotNull String message) {
        return deserialize(player, message, Maps.newHashMap());
    }

    public static Component deserialize(@NotNull OfflinePlayer player, @NotNull String message, @NotNull Map<String, String> placeholders) {
        Map<String, String> newPlaceholders = new ImmutableMap.Builder<String, String>()
                .putAll(placeholders)
                .putAll(PLACEHOLDER_PATTERN.matcher(message).results()
                        .map(MatchResult::group)
                        .filter(key -> !placeholders.containsKey(key))
                        .collect(Collectors.toMap(key -> key, key -> {
                            key = PlaceholderAPI.setPlaceholders(player, key);
                            return key;
                        })))
                .build();

        return deserialize(message, newPlaceholders);
    }

/*
    static String convertPlaceholderKeys(@NotNull String message,
                                         @NotNull Collection<String> placeholderKeys) {
        for (String placeholderKey : placeholderKeys) {
            String parsedPlaceholderKey = convertKeys(placeholderKey);
            message = PlaceholderParser.applyPlaceholders(message, Map.of(
                    "%" + parsedPlaceholderKey + "%", "<" + parsedPlaceholderKey + ">",
                    "{" + parsedPlaceholderKey + "}", "<" + parsedPlaceholderKey + ">",
                    ":" + parsedPlaceholderKey + ":", "<" + parsedPlaceholderKey + ">"
            ));
        }

        return message;
    }

    static String convertKeys(@NotNull String message) {
        if (message.startsWith("%") || message.startsWith("{") || message.startsWith(":")) {
            message = message.substring(1);
            message = message.substring(0, message.length() - 1);
        }

        return message;
    }
*/

    private ComponentSerializer() {}
}
