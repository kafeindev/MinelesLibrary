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

package net.mineles.library.node.loader;

import static com.google.common.base.Preconditions.*;

import dev.kafein.multiduels.node.Node;
import dev.kafein.multiduels.node.NodeException;
import dev.kafein.multiduels.node.NodeFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

public abstract class NodeLoader {
    protected final @Nullable Callable<BufferedReader> reader;
    protected final @Nullable Callable<BufferedWriter> writer;

    protected final int indent;

    protected NodeLoader(@NotNull Builder<?> builder) {
        this.reader = builder.reader;
        this.writer = builder.writer;
        this.indent = builder.indent;
    }

    public @Nullable Node load() throws NodeException {
        checkNotNull(this.reader, "reader cannot be null");

        try (BufferedReader bufferedReader = this.reader.call()) {
            Node node = NodeFactory.createNode();
            return loadInternal(node, bufferedReader);
        } catch (Exception e) {
            throw new NodeException("Failed to load node", e);
        }
    }

    protected abstract @Nullable Node loadInternal(@NotNull Node node, @NotNull BufferedReader reader) throws NodeException;

    public boolean save(@NotNull Node node) throws NodeException {
        checkNotNull(this.writer, "writer cannot be null");

        try (BufferedWriter bufferedWriter = this.writer.call()) {
            return saveInternal(node, bufferedWriter);
        } catch (Exception e) {
            throw new NodeException("Failed to save node", e);
        }
    }

    protected abstract boolean saveInternal(@NotNull Node node, @NotNull BufferedWriter writer) throws NodeException;

    protected abstract static class Builder<T extends Builder<T>> {
        protected @Nullable Callable<BufferedReader> reader;
        protected @Nullable Callable<BufferedWriter> writer;

        protected int indent = 2;

        protected Builder() {}

        @SuppressWarnings("unchecked")
        public T self() {
            return (T) this;
        }

        public @NotNull T indent(int indent) {
            this.indent = indent;
            return self();
        }

        public int indent() {
            return this.indent;
        }

        public @NotNull T file(@NotNull File file) {
            return path(file.toPath());
        }

        public @NotNull T path(@NotNull Path path) {
            Path absolutePath = path.toAbsolutePath();
            this.reader = () -> Files.newBufferedReader(absolutePath, StandardCharsets.UTF_8);
            this.writer = () -> Files.newBufferedWriter(absolutePath, StandardCharsets.UTF_8);
            return self();
        }

        public @NotNull T url(@NotNull URL url) {
            this.reader = () -> new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), StandardCharsets.UTF_8));
            return self();
        }

        public abstract @NotNull NodeLoader build();
    }
}
