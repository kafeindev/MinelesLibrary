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

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class DefaultNode implements Node {
    private volatile @Nullable Object key;
    private volatile @Nullable Object value;

    private final @NotNull NodeTree tree;
    private final @Nullable Node parent;
    private final @Nullable List<Node> path;
    private final @NotNull List<Node> childList;

    DefaultNode(@Nullable Object key,
                @Nullable Object value,
                @NotNull NodeTree tree,
                @Nullable Node parent,
                @Nullable List<Node> path,
                @Nullable List<Node> childList) {
        this.key = key;
        this.value = value;
        this.tree = tree;
        this.parent = parent;
        this.path = path;
        this.childList = childList == null ? Lists.newArrayList() : childList;
    }

    @Override
    public @Nullable Object key() {
        return this.key;
    }

    @Override
    public boolean isRoot() {
        return root() == null || root() == this;
    }

    @Override
    public boolean isNull() {
        return key() == null && get() == null;
    }

    @Override
    public boolean isEmpty() {
        return isNull() && this.childList.isEmpty();
    }

    @Override
    public @NotNull NodeTree tree() {
        return this.tree;
    }

    @Override
    public @Nullable Node root() {
        return this.tree == null ? null : this.tree.getRoot();
    }

    @Override
    public @Nullable Node parent() {
        if (this.parent == null && root() != null) {
            return root();
        }

        return this.parent;
    }

    @Override
    public @Nullable List<Node> path() {
        return this.path;
    }

    @Override
    public @NotNull List<Node> childList() {
        return this.childList;
    }

    @Override
    public @Nullable Map<Object, Node> childMap() {
        return this.childList.stream().collect(Collectors.toMap(Node::key, Function.identity()));
    }

    @Override
    public Node node(@NotNull Object... path) {
        Node target = this;
        for (Object key : path) {
            List<Node> childList = target.childList();
            target = childList.stream()
                    .filter(node -> node.key() != null && node.key().equals(key))
                    .findFirst()
                    .orElse(null);
        }

        return target;
    }

    @Override
    public Node node(@NotNull Iterable<?> path) {
        Node target = this;
        for (Object key : path) {
            List<Node> childList = target.childList();
            target = childList.stream()
                    .filter(node -> node.key() != null && node.key().equals(key))
                    .findFirst()
                    .orElse(null);
        }

        return target;
    }

    @Override
    public boolean has(@NotNull Object... path) {
        return node(path) != null;
    }

    @Override
    public boolean has(@NotNull Iterable<?> path) {
        return node(path) != null;
    }

    @Override
    public @Nullable Object get() {
        return this.value;
    }

    @Override
    public <V> @Nullable V get(@NotNull Class<V> clazz) {
        return get(TypeToken.of(clazz));
    }

    @Override
    public <V> @Nullable V get(@NotNull Class<V> clazz, @NotNull V defaultValue) {
        return get(TypeToken.of(clazz), defaultValue);
    }

    @Override
    public <V> @Nullable V get(@NotNull TypeToken<V> typeToken) {
        if (this.value == null) {
            return null;
        }

        return this.tree.getSerializers().deserialize(typeToken, this);
    }

    @Override
    public <V> @Nullable V get(@NotNull TypeToken<V> typeToken, @NotNull V defaultValue) {
        return this.value == null ? defaultValue : get(typeToken);
    }

    @Override
    public @Nullable <V> List<V> getList() {
        TypeToken<List<V>> typeToken = new TypeToken<List<V>>() {};
        return (List<V>) getList(typeToken);
    }

    @Override
    public @Nullable <V> List<V> getList(@NotNull Class<V> clazz) {
        TypeToken<List<V>> typeToken = new TypeToken<List<V>>() {};
        return (List<V>) getList(typeToken);
    }

    @Override
    public @Nullable <V> List<V> getList(@NotNull Class<V> clazz, @NotNull List<V> defaultValue) {
        return this.value == null ? defaultValue : getList(clazz);
    }

    @Override
    public @Nullable <V> List<V> getList(@NotNull TypeToken<V> typeToken) {
        if (this.value == null) {
            return null;
        }

        return (List<V>) get(typeToken);
    }

    @Override
    public @Nullable <V> List<V> getList(@NotNull TypeToken<V> typeToken, @NotNull List<V> defaultValue) {
        return this.value == null ? defaultValue : getList(typeToken);
    }

    @Override
    public @Nullable String getString() {
        return get(String.class);
    }

    @Override
    public @Nullable String getString(@NotNull String defaultValue) {
        return get(String.class, defaultValue);
    }

    @Override
    public int getInt() {
        return get(Integer.class);
    }

    @Override
    public int getInt(int defaultValue) {
        return get(Integer.class, defaultValue);
    }

    @Override
    public double getDouble() {
        return get(Double.class);
    }

    @Override
    public double getDouble(double defaultValue) {
        return get(Double.class, defaultValue);
    }

    @Override
    public boolean getBoolean() {
        return get(Boolean.class);
    }

    @Override
    public boolean getBoolean(boolean defaultValue) {
        return get(Boolean.class, defaultValue);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull Node set(@Nullable Object value) {
        if (this.value == null) {
            this.value = value;
            return this;
        }

        if (this.value instanceof Collection) {
            get(Collection.class).add(value);
        } else {
            this.value = value;
        }
        return this;
    }

    @Override
    public @NotNull Node set(@Nullable Object value, @NotNull Object... path) {
        Node target = this;
        for (Object key : path) {
            if (target.has(key)) {
                target = target.node(key);
                continue;
            }

            target = target.attach(key);
        }

        return target.set(value);
    }

    @Override
    public @NotNull Node remove(@Nullable Object key) {
        this.childList.removeIf(node -> node.key() != null && node.key().equals(key));
        return this;
    }

    @Override
    public @NotNull Node setKey(@Nullable Object key) {
        this.key = key;
        return this;
    }

    @Override
    public @NotNull Node attach(@Nullable Object... path) {
        if (path == null || path.length == 0) {
            return this;
        }

        Node target = this;
        for (Object key : path) {
            target = NodeFactory.createNode(key, null, this, target);
        }

        return target;
    }

    @Override
    public @NotNull Node attach(@NotNull Node node) {
        this.childList.add(node);
        this.tree.addNode(node);
        return this;
    }

    @Override
    public @NotNull Node attach(@NotNull Iterable<Node> nodes) {
        nodes.forEach(this::attach);
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Node)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Node node = (Node) obj;
        return Objects.equals(this.root(), node.root())
                && Objects.equals(this.parent, node.parent())
                && Objects.equals(this.path, node.path())
                && Objects.equals(this.key, node.key())
                && Objects.equals(this.value, node.get())
                && Objects.equals(this.childList, node.childList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key,
                this.value,
                this.root(),
                this.parent,
                this.path,
                this.childList);
    }

    @Override
    public @Nullable String toStringKey() {
        return this.key == null ? null : this.key.toString();
    }

    @Override
    public String toString() {
        return "Node{" +
                "key=" + this.key +
                ", value=" + this.value +
                ", childList=" + this.childList +
                '}';
    }
}
