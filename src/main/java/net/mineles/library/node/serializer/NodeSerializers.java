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

package net.mineles.library.node.serializer;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import net.mineles.library.node.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public final class NodeSerializers {
    static final NodeSerializers DEFAULT = NodeSerializers.create();

    static {
        DEFAULT.register(TypeToken.of(Boolean.class), TypeSerializers.BOOLEAN);
        DEFAULT.register(TypeToken.of(Character.class), TypeSerializers.CHARACTER);
        DEFAULT.register(TypeToken.of(String.class), TypeSerializers.STRING);
        DEFAULT.register(TypeToken.of(Byte.class), TypeSerializers.BYTE);
        DEFAULT.register(TypeToken.of(Short.class), TypeSerializers.SHORT);
        DEFAULT.register(TypeToken.of(Integer.class), TypeSerializers.INTEGER);
        DEFAULT.register(TypeToken.of(Long.class), TypeSerializers.LONG);
        DEFAULT.register(TypeToken.of(Double.class), TypeSerializers.DOUBLE);
        DEFAULT.register(TypeToken.of(Float.class), TypeSerializers.FLOAT);
        DEFAULT.register(new TypeToken<Enum<?>>() {}, TypeSerializers.ENUM);
        DEFAULT.register(new TypeToken<Optional<?>>() {}, TypeSerializers.OPTIONAL);
        DEFAULT.register(TypeToken.of(UUID.class), TypeSerializers.UUID);
        DEFAULT.register(TypeToken.of(Date.class), TypeSerializers.DATE);
        DEFAULT.register(TypeToken.of(URI.class), TypeSerializers.URI);
        DEFAULT.register(TypeToken.of(URL.class), TypeSerializers.URL);
        DEFAULT.register(TypeToken.of(Path.class), TypeSerializers.PATH);
        DEFAULT.register(TypeToken.of(File.class), TypeSerializers.FILE);
    }

    private final @NotNull Map<TypeToken<?>, NodeSerializer<?>> serializers;

    public NodeSerializers() {
        this(Maps.newHashMap());
    }

    public NodeSerializers(@NotNull Map<TypeToken<?>, NodeSerializer<?>> serializers) {
        this.serializers = checkNotNull(serializers, "serializers");
    }

    @NotNull
    public static NodeSerializers create() {
        return new NodeSerializers();
    }

    @NotNull
    public static NodeSerializers create(@NotNull Map<TypeToken<?>, NodeSerializer<?>> serializers) {
        return new NodeSerializers(serializers);
    }

    @NotNull
    public static NodeSerializers defaultSerializers() {
        return DEFAULT;
    }

    public <T> @Nullable T deserialize(@NotNull TypeToken<T> type, @NotNull Node node) {
        if (!isRegistered(type)) {
            throw new IllegalArgumentException("Type " + type + " is not registered.");
        }

        return this.getSerializer(type).deserialize(type.getType(), node);
    }

    public <T> void serialize(@NotNull Node node, @NotNull T object) {
        TypeToken<T> type = (TypeToken<T>) TypeToken.of(checkNotNull(object, "object").getClass());
        serialize(type, node, object);
    }

    public <T> void serialize(@NotNull TypeToken<T> type, @NotNull Node node, @Nullable T object) {
        if (!isRegistered(type)) {
            throw new IllegalArgumentException("Type " + type + " is not registered.");
        }

        this.getSerializer(type).serialize(node, object);
    }

    public @NotNull Map<TypeToken<?>, NodeSerializer<?>> getSerializers() {
        return this.serializers;
    }

    public <T> @NotNull NodeSerializer<T> getSerializer(@NotNull TypeToken<T> type) {
        return (NodeSerializer<T>) this.serializers.get(type);
    }

    public <T> void register(@NotNull TypeToken<T> type, @NotNull NodeSerializer<T> serializer) {
        this.serializers.put(type, serializer);
    }

    public void unregister(@NotNull TypeToken<?> type) {
        this.serializers.remove(type);
    }

    public boolean isRegistered(@NotNull TypeToken<?> type) {
        return this.serializers.containsKey(type);
    }
}
