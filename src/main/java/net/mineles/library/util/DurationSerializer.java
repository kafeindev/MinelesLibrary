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

package net.mineles.library.util;

import net.mineles.library.serializer.Serializer;
import org.jetbrains.annotations.NotNull;

public final class DurationSerializer implements Serializer<String, Long> {
    public static final DurationSerializer INSTANCE = new DurationSerializer();

    private DurationSerializer() {}

    @Override
    public @NotNull String deserialize(@NotNull Long millis) {
        long seconds = millis / 1000L;
        long minutes = seconds / 60L;
        long hours = minutes / 60L;
        long days = hours / 24L;

        if (days > 0) {
            return days + "d " + (hours % 24) + "h " + (minutes % 60) + "m " + (seconds % 60) + "s";
        } else if (hours > 0) {
            return hours + "h " + (minutes % 60) + "m " + (seconds % 60) + "s";
        } else if (minutes > 0) {
            return minutes + "m " + (seconds % 60) + "s";
        } else if (seconds > 0) {
            return seconds + "s";
        } else {
            return millis + "ms";
        }
    }

    @Override
    public Long serialize(@NotNull String s) {
        long millis = 0;

        String[] parts = s.split(" ");
        for (String part : parts) {
            if (part.endsWith("ms")) {
                millis = Long.parseLong(part.substring(0, part.length() - 2));
            } else if (part.endsWith("s")) {
                millis = Long.parseLong(part.substring(0, part.length() - 1)) * 1000L;
            } else if (part.endsWith("m")) {
                millis = Long.parseLong(part.substring(0, part.length() - 1)) * 60000L;
            } else if (part.endsWith("h")) {
                millis = Long.parseLong(part.substring(0, part.length() - 1)) * 3600000L;
            } else if (part.endsWith("d")) {
                millis = Long.parseLong(part.substring(0, part.length() - 1)) * 86400000L;
            } else {
                throw new IllegalArgumentException("Invalid duration format: " + s);
            }
        }

        return millis;
    }
}
