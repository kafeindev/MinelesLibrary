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

import dev.kafein.multiduels.node.serializer.NodeSerializers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class NodeTree {
    private final @NotNull NodeSerializers serializers;

    private @Nullable Node root;
    private @Nullable List<Node> nodes;

    NodeTree(@Nullable Node root,
             @Nullable List<Node> nodes,
             @NotNull NodeSerializers serializers) {
        this.root = root;
        this.nodes = nodes;
        this.serializers = serializers;
    }

    @NotNull
    public static NodeTree empty() {
        return new NodeTree(null, null, NodeSerializers.defaultSerializers());
    }

    public @Nullable Node getRoot() {
        return this.root;
    }

    public void setRoot(@Nullable Node root) {
        this.root = root;
    }

    public @Nullable List<Node> getNodes() {
        return this.nodes;
    }

    public void setNodes(@Nullable List<Node> nodes) {
        this.nodes = nodes;
    }

    public void addNodes(@Nullable List<Node> nodes) {
        if (nodes == null) {
            return;
        }

        nodes.forEach(this::addNode);
    }

    public void addNode(@Nullable Node node) {
        if (node == null) {
            return;
        }

        if (this.nodes == null) {
            this.nodes = new ArrayList<>();
        }
        this.nodes.add(node);
    }

    public @NotNull NodeSerializers getSerializers() {
        return this.serializers;
    }

    @NotNull
    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private @Nullable Node root;
        private @Nullable List<Node> nodes;
        private @Nullable NodeSerializers serializers;

        private Builder() {}

        public @NotNull Builder root(@Nullable Node root) {
            this.root = root;
            return this;
        }

        public @NotNull Builder nodes(@Nullable List<Node> nodes) {
            this.nodes = nodes;
            return this;
        }

        public @NotNull Builder serializers(@Nullable NodeSerializers serializers) {
            this.serializers = serializers;
            return this;
        }

        public @NotNull NodeTree build() {
            if (this.serializers == null) {
                this.serializers = NodeSerializers.defaultSerializers();
            }

            return new NodeTree(this.root, this.nodes, this.serializers);
        }
    }
}
