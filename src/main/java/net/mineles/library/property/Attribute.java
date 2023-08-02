/*
 * MIT License
 *
 * Copyright (c) 2023 Kafein
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

package net.mineles.library.property;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class Attribute<T> {
    private final @NotNull String key;
    private final @Nullable T value;

    public Attribute(@NotNull String key, @Nullable T value) {
        this.key = key;
        this.value = value;
    }

    @NotNull
    public static <T> Attribute<T> of(@NotNull String key, @Nullable T value) {
        return new Attribute<>(key, value);
    }

    public @NotNull String getKey() {
        return this.key;
    }

    public @Nullable T getValue() {
        return this.value;
    }

    public <U> @Nullable U getAs(@NotNull Class<U> type) {
        return type.cast(this.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Attribute)) {
            return false;
        }

        Attribute<?> attribute = (Attribute<?>) obj;
        return Objects.equals(this.key, attribute.key) &&
                Objects.equals(this.value, attribute.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key, this.value);
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "key='" + this.key + '\'' +
                ", value=" + this.value +
                '}';
    }
}
