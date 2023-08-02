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

import dev.kafein.multiduels.node.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

final class TypeSerializers {
    static final NodeSerializer<Boolean> BOOLEAN = new NodeSerializer<Boolean>() {
        @Override
        public @Nullable Boolean deserialize(@NotNull Type type, @NotNull Node node) {
            Object value = node.get();
            if (value == null) {
                return null;
            }

            return Boolean.parseBoolean(value.toString());
        }

        @Override
        public void serialize(@NotNull Node node, @Nullable Boolean value) {
            node.set(value);
        }
    };

    static final NodeSerializer<Character> CHARACTER = new NodeSerializer<Character>() {
        @Override
        public @Nullable Character deserialize(@NotNull Type type, @NotNull Node node) {
            Object value = node.get();
            if (value == null) {
                return null;
            }

            return value.toString().charAt(0);
        }

        @Override
        public void serialize(@NotNull Node node, @Nullable Character value) {
            node.set(value);
        }
    };

    static final NodeSerializer<String> STRING = new NodeSerializer<String>() {
        @Override
        public @Nullable String deserialize(@NotNull Type type, @NotNull Node node) {
            Object value = node.get();
            if (value == null) {
                return null;
            }

            return value.toString();
        }

        @Override
        public void serialize(@NotNull Node node, @Nullable String value) {
            node.set(value);
        }
    };

    static final NodeSerializer<Byte> BYTE = new NodeSerializer<Byte>() {
        @Override
        public @Nullable Byte deserialize(@NotNull Type type, @NotNull Node node) {
            Object value = node.get();
            if (value == null) {
                return null;
            }

            return Byte.parseByte(value.toString());
        }

        @Override
        public void serialize(@NotNull Node node, @Nullable Byte value) {
            node.set(value);
        }
    };

    static final NodeSerializer<Short> SHORT = new NodeSerializer<Short>() {
        @Override
        public @Nullable Short deserialize(@NotNull Type type, @NotNull Node node) {
            Object value = node.get();
            if (value == null) {
                return null;
            }

            return Short.parseShort(value.toString());
        }

        @Override
        public void serialize(@NotNull Node node, @Nullable Short value) {
            node.set(value);
        }
    };

    static final NodeSerializer<Integer> INTEGER = new NodeSerializer<Integer>() {
        @Override
        public @Nullable Integer deserialize(@NotNull Type type, @NotNull Node node) {
            Object value = node.get();
            if (value == null) {
                return null;
            }

            return Integer.parseInt(value.toString());
        }

        @Override
        public void serialize(@NotNull Node node, @Nullable Integer value) {
            node.set(value);
        }
    };

    static final NodeSerializer<Long> LONG = new NodeSerializer<Long>() {
        @Override
        public @Nullable Long deserialize(@NotNull Type type, @NotNull Node node) {
            Object value = node.get();
            if (value == null) {
                return null;
            }

            return Long.parseLong(value.toString());
        }

        @Override
        public void serialize(@NotNull Node node, @Nullable Long value) {
            node.set(value);
        }
    };

    static final NodeSerializer<Double> DOUBLE = new NodeSerializer<Double>() {
        @Override
        public @Nullable Double deserialize(@NotNull Type type, @NotNull Node node) {
            Object value = node.get();
            if (value == null) {
                return null;
            }

            return Double.parseDouble(value.toString());
        }

        @Override
        public void serialize(@NotNull Node node, @Nullable Double value) {
            node.set(value);
        }
    };

    static final NodeSerializer<Float> FLOAT = new NodeSerializer<Float>() {
        @Override
        public @Nullable Float deserialize(@NotNull Type type, @NotNull Node node) {
            Object value = node.get();
            if (value == null) {
                return null;
            }

            return Float.parseFloat(value.toString());
        }

        @Override
        public void serialize(@NotNull Node node, @Nullable Float value) {
            node.set(value);
        }
    };

    static final NodeSerializer<Enum<?>> ENUM = new NodeSerializer<Enum<?>>() {
        @Override
        public @Nullable Enum<?> deserialize(@NotNull Type type, @NotNull Node node) {
            Object value = node.get();
            if (value == null) {
                return null;
            }

            Class<?> clazz = (Class<?>) type;
            String name = value.toString();
            for (Enum<?> enumConstant : (Enum<?>[]) clazz.getEnumConstants()) {
                if (enumConstant.name().equals(name)) {
                    return enumConstant;
                }
            }

            return null;
        }

        @Override
        public void serialize(@NotNull Node node, @Nullable Enum<?> value) {
            if (value != null) {
                node.set(value.name());
            } else {
                node.set(null);
            }
        }
    };

    static final NodeSerializer<Optional<?>> OPTIONAL = new NodeSerializer<Optional<?>>() {
        @Override
        public @Nullable Optional<?> deserialize(@NotNull Type type, @NotNull Node node) {
            Object value = node.get();
            if (value == null) {
                return null;
            }

            return Optional.of(value);
        }

        @Override
        public void serialize(@NotNull Node node, @Nullable Optional<?> value) {
            if (value != null) {
                node.set(value.orElse(null));
            } else {
                node.set(null);
            }
        }
    };

    static final NodeSerializer<java.util.UUID> UUID = new NodeSerializer<UUID>() {
        @Override
        public @Nullable UUID deserialize(@NotNull Type type, @NotNull Node node) {
            Object value = node.get();
            if (value == null) {
                return null;
            }

            return java.util.UUID.fromString(value.toString());
        }

        @Override
        public void serialize(@NotNull Node node, @Nullable UUID value) {
            if (value != null) {
                node.set(value.toString());
            } else {
                node.set(null);
            }
        }
    };

    static final NodeSerializer<Date> DATE = new NodeSerializer<Date>() {
        @Override
        public @Nullable Date deserialize(@NotNull Type type, @NotNull Node node) {
            Object value = node.get();
            if (value == null) {
                return null;
            }

            return new Date((long) value);
        }

        @Override
        public void serialize(@NotNull Node node, @Nullable Date value) {
            if (value != null) {
                node.set(value.getTime());
            } else {
                node.set(null);
            }
        }
    };

    static final NodeSerializer<URI> URI = new NodeSerializer<URI>() {
        @Override
        public @Nullable URI deserialize(@NotNull Type type, @NotNull Node node) {
            Object value = node.get();
            if (value == null) {
                return null;
            }

            return java.net.URI.create(value.toString());
        }

        @Override
        public void serialize(@NotNull Node node, @Nullable URI value) {
            if (value != null) {
                node.set(value.toString());
            } else {
                node.set(null);
            }
        }
    };

    static final NodeSerializer<URL> URL = new NodeSerializer<URL>() {
        @Override
        public @Nullable URL deserialize(@NotNull Type type, @NotNull Node node) {
            Object value = node.get();
            if (value == null) {
                return null;
            }

            try {
                return new URL(value.toString());
            } catch (MalformedURLException e) {
                return null;
            }
        }

        @Override
        public void serialize(@NotNull Node node, @Nullable URL value) {
            if (value != null) {
                node.set(value.toString());
            } else {
                node.set(null);
            }
        }
    };

    static final NodeSerializer<Path> PATH = new NodeSerializer<Path>() {
        @Override
        public @Nullable Path deserialize(@NotNull Type type, @NotNull Node node) {
            Object value = node.get();
            if (value == null) {
                return null;
            }

            return Paths.get(value.toString());
        }

        @Override
        public void serialize(@NotNull Node node, @Nullable Path value) {
            if (value != null) {
                node.set(value.toString());
            } else {
                node.set(null);
            }
        }
    };

    static final NodeSerializer<File> FILE = new NodeSerializer<File>() {
        @Override
        public @Nullable File deserialize(@NotNull Type type, @NotNull Node node) {
            Object value = node.get();
            if (value == null) {
                return null;
            }

            return new File(value.toString());
        }

        @Override
        public void serialize(@NotNull Node node, @Nullable File value) {
            if (value != null) {
                node.set(value.toString());
            } else {
                node.set(null);
            }
        }
    };

    private TypeSerializers() {}
}
