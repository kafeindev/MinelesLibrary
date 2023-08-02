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

package net.mineles.library.action.context;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public class Context {
    private final @NotNull Object[] args;

    public Context(@NotNull Object... args) {
        this.args = Arrays.stream(args)
                .filter(Objects::nonNull)
                .toArray();
    }

    @NotNull
    public static Context create(@NotNull Object... args) {
        return new Context(args);
    }

    public @NotNull Object[] getArgs() {
        return this.args;
    }

    public @Nullable Object get(int index) {
        return this.args[index];
    }

    public <T> @Nullable T get(@NotNull Class<T> clazz) {
        for (Object object : this.args) {
            if (isInstance(object, clazz)) {
                return clazz.cast(object);
            }
        }

        return null;
    }

    public <T> @Nullable T get(int index, @NotNull Class<T> clazz) {
        return clazz.cast(this.args[index]);
    }

    public <T> @NotNull T get(int index, @NotNull T defaultValue) {
        if (this.args.length <= index) {
            return defaultValue;
        }

        return (T) this.args[index];
    }

    public <T> @NotNull T get(int index, @NotNull Class<T> clazz, @NotNull T defaultValue) {
        if (this.args.length <= index) {
            return defaultValue;
        }

        return clazz.cast(this.args[index]);
    }

    public <T> @Nullable T get(@NotNull Class<T> clazz, int index) {
        int count = 0;
        for (Object object : this.args) {
            if (isInstance(object, clazz)) {
                if (count == index) {
                    return clazz.cast(object);
                }

                count++;
            }
        }

        return null;
    }

    public <T> @NotNull T get(@NotNull Class<T> clazz, int index, @NotNull T defaultValue) {
        int count = 0;
        for (Object object : this.args) {
            if (isInstance(object, clazz)) {
                if (count == index) {
                    return clazz.cast(object);
                }

                count++;
            }
        }

        return defaultValue;
    }

    public boolean has(int index) {
        return this.args.length > index;
    }

    public boolean has(int index, @NotNull Class<?> clazz) {
        return this.args.length > index && clazz.isInstance(this.args[index]);
    }

    public boolean has(@NotNull Class<?> clazz) {
        for (Object object : this.args) {
            if (isInstance(object, clazz)) {
                return true;
            }
        }

        return false;
    }

    public boolean has(@NotNull Class<?> clazz, int index) {
        int count = 0;
        for (Object object : this.args) {
            if (isInstance(object, clazz)) {
                if (count == index) {
                    return true;
                }

                count++;
            }
        }

        return false;
    }

    public int size() {
        return this.args.length;
    }

    boolean isInstance(@NotNull Object object, @NotNull Class<?> clazz) {
        return clazz.isInstance(object)
                || clazz.isAssignableFrom(object.getClass())
                || object.getClass().isAssignableFrom(clazz);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Context)) return false;

        Context context = (Context) obj;
        return Arrays.equals(this.args, context.args);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.args);
    }

    @Override
    public String toString() {
        return "Context{" +
                "args=" + Arrays.toString(this.args) +
                '}';
    }
}
