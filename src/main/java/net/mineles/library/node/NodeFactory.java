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

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class NodeFactory {
    private NodeFactory() {
    }

    @NotNull
    public static Node createNode() {
        return createNode(null, null, null, null, null, null, true);
    }

    @NotNull
    public static Node createNode(@Nullable Object key) {
        return createNode(key, true);
    }

    @NotNull
    public static Node createNode(@Nullable Object key,
                                  boolean modifiable) {
        return createNode(key, null, null, null, null, null, modifiable);
    }

    @NotNull
    public static Node createNode(@Nullable Object key,
                                  @Nullable Object value) {
        return createNode(key, value, true);
    }

    @NotNull
    public static Node createNode(@Nullable Object key,
                                  @Nullable Object value,
                                  boolean modifiable) {
        return createNode(key, value, null, null, null, null, modifiable);
    }

    @NotNull
    public static Node createNode(@Nullable Object key,
                                  @Nullable Object value,
                                  @Nullable Node root) { // or parent
        return createNode(key, value, root, true);
    }

    @NotNull
    public static Node createNode(@Nullable Object key,
                                  @Nullable Object value,
                                  @Nullable Node root, // or parent
                                  boolean modifiable) {
        if (root == null) {
            return createNode(key, value, modifiable);
        }

        Node node = createNode(key, value, root, root, ImmutableList.of(root), null, modifiable);
        if (root instanceof DefaultNode) {
            root.attach(node);
        }

        return node;
    }

    @NotNull
    public static Node createNode(@Nullable Object key,
                                  @Nullable Object value,
                                  @Nullable Node root,
                                  @Nullable Node parent) {
        return createNode(key, value, root, parent, true);
    }

    @NotNull
    public static Node createNode(@Nullable Object key,
                                  @Nullable Object value,
                                  @Nullable Node root,
                                  @Nullable Node parent,
                                  boolean modifiable) {
        if (parent == null) {
            return createNode(key, value, root, modifiable);
        }
        if (root == null) {
            return createNode(key, value, parent, modifiable);
        }

        List<Node> pathList = new ArrayList<>();
        if (parent.path() != null) {
            pathList.addAll(parent.path());
            pathList.add(parent);
        } else {
            pathList.add(root);
            pathList.add(parent);
        }

        Node node = createNode(key, value, root, parent, pathList, null, modifiable);
        if (parent instanceof DefaultNode) {
            parent.attach(node);
        }

        return node;
    }

    @NotNull
    public static Node createNode(@Nullable Object key,
                                  @Nullable Object value,
                                  @Nullable Node root,
                                  @Nullable Node parent,
                                  @Nullable List<Node> path) {
        return createNode(key, value, root, parent, path, true);
    }

    @NotNull
    public static Node createNode(@Nullable Object key,
                                  @Nullable Object value,
                                  @Nullable Node root,
                                  @Nullable Node parent,
                                  @Nullable List<Node> path,
                                  boolean modifiable) {
        return createNode(key, value, root, parent, path, null, modifiable);
    }

    @NotNull
    public static Node createNode(@Nullable Object key,
                                  @Nullable Object value,
                                  @Nullable Node root,
                                  @Nullable Node parent,
                                  @Nullable List<Node> path,
                                  @Nullable List<Node> childList) {
        return createNode(key, value, root, parent, path, childList, true);
    }

    @NotNull
    public static Node createNode(@Nullable Object key,
                                  @Nullable Object value,
                                  @Nullable Node root,
                                  @Nullable Node parent,
                                  @Nullable List<Node> path,
                                  @Nullable List<Node> childList,
                                  boolean modifiable) {
        NodeTree nodeTree = NodeTree.newBuilder()
                .root(root)
                .build();
        nodeTree.addNode(root);
        nodeTree.addNodes(path);
        nodeTree.addNodes(childList);

        if (modifiable) {
            return new DefaultNode(key, value, nodeTree, parent, path, childList);
        } else {
            return new UnmodifiableNode(key, value, nodeTree, parent, path, childList);
        }
    }
}
