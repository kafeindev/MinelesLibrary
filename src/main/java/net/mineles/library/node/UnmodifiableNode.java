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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

final class UnmodifiableNode extends DefaultNode implements Node {
    static final String ERROR_MESSAGE = "This node is unmodifiable.";

    UnmodifiableNode(@Nullable Object key,
                     @Nullable Object value,
                     @Nullable NodeTree tree,
                     @Nullable Node parent,
                     @Nullable List<Node> path,
                     @Nullable List<Node> childList) {
        super(key, value, tree, parent, path, childList);
    }

    @Override
    public @NotNull Node set(@Nullable Object value) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public @NotNull Node set(@Nullable Object value, @NotNull Object... path) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public @NotNull Node attach(@Nullable Object... path) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public @NotNull Node attach(@NotNull Node node) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public @NotNull Node attach(@NotNull Iterable<Node> nodes) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public @NotNull Node remove(@Nullable Object key) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }

    @Override
    public @NotNull Node setKey(@Nullable Object key) {
        throw new UnsupportedOperationException(ERROR_MESSAGE);
    }
}
