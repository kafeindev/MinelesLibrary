/*
 * MIT License
 *
 * Copyright (c) 2023 DreamCoins
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
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class LegacyTextConverter {
    private static final char AMPERSAND_CHAR = '&';
    private static final ImmutableMap<String, String> COLOR_MAP = ImmutableMap.<String, String>builder()
            .put("0", "black")
            .put("1", "dark_blue")
            .put("2", "dark_green")
            .put("3", "dark_aqua")
            .put("4", "dark_red")
            .put("5", "dark_purple")
            .put("6", "gold")
            .put("7", "gray")
            .put("8", "dark_gray")
            .put("9", "blue")
            .put("a", "green")
            .put("b", "aqua")
            .put("c", "red")
            .put("d", "light_purple")
            .put("e", "yellow")
            .put("f", "white")
            .put("l", "bold")
            .put("n", "underline")
            .put("o", "italic")
            .put("r", "reset")
            .build();

    public static String convert(@NotNull String message) {
        for (Map.Entry<String, String> entry : COLOR_MAP.entrySet()) {
            message = message.replaceAll(AMPERSAND_CHAR + entry.getKey(), "<" + entry.getValue() + ">");
        }
        
        return message;
    }

    private LegacyTextConverter() {}
}
