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

package net.mineles.library.node;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface Node {
    @Nullable Object key();

    boolean isRoot();

    boolean isNull();

    boolean isEmpty();

    @NotNull NodeTree tree();

    @Nullable Node root();

    @Nullable Node parent();

    @Nullable List<Node> path();

    @NotNull List<Node> childList();

    @Nullable Map<Object, Node> childMap();

    Node node(@NotNull Object... path);

    Node node(@NotNull Iterable<?> path);

    boolean has(@NotNull Object... path);

    boolean has(@NotNull Iterable<?> path);

    @Nullable Object get();

    @Nullable <V> V get(@NotNull Class<V> clazz);

    @Nullable <V> V get(@NotNull Class<V> clazz, @NotNull V defaultValue);

    @Nullable <V> V get(@NotNull TypeToken<V> typeToken);

    @Nullable <V> V get(@NotNull TypeToken<V> typeToken, @NotNull V defaultValue);

    @Nullable <V> List<V> getList();

    @Nullable <V> List<V> getList(@NotNull Class<V> clazz);

    @Nullable <V> List<V> getList(@NotNull Class<V> clazz, @NotNull List<V> defaultValue);

    @Nullable <V> List<V> getList(@NotNull TypeToken<V> typeToken);

    @Nullable <V> List<V> getList(@NotNull TypeToken<V> typeToken, @NotNull List<V> defaultValue);

    @Nullable String getString();

    @Nullable String getString(@NotNull String defaultValue);

    int getInt();

    int getInt(int defaultValue);

    double getDouble();

    double getDouble(double defaultValue);

    boolean getBoolean();

    boolean getBoolean(boolean defaultValue);

    @NotNull Node set(@Nullable Object value);

    @NotNull Node set(@Nullable Object value, @NotNull Object... path);

    @NotNull Node attach(@Nullable Object... path);

    @NotNull Node attach(@NotNull Node node);

    @NotNull Node attach(@NotNull Iterable<Node> nodes);

    @NotNull Node remove(@Nullable Object key);

    @NotNull Node setKey(@Nullable Object key);

    @Nullable String toStringKey();
}
