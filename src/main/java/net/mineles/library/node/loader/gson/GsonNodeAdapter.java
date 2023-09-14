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

package net.mineles.library.node.loader.gson;

import com.google.common.collect.Lists;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.mineles.library.node.Node;
import net.mineles.library.node.NodeFactory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collection;

final class GsonNodeAdapter extends TypeAdapter<Node> {
    @Override
    public void write(JsonWriter out, Node value) throws IOException {
        if (value.key() != null) {
            out.beginObject();
            out.name(value.toStringKey());
        }
        write0(out, value);
        if (value.key() != null) {
            out.endObject();
        }
    }

    private void write0(JsonWriter out, Node value) throws IOException {
        if (value.childList().isEmpty()) {
            Object object = value.get();

            if (object instanceof String) {
                out.value((String) object);
            } else if (object instanceof Number) {
                out.value((Number) object);
            } else if (object instanceof Boolean) {
                out.value((Boolean) object);
            } else if (object instanceof Collection) {
                out.beginArray();
                for (Object o : (Collection<?>) object) {
                    if (o instanceof Node) {
                        write0(out, (Node) o);
                    } else {
                        out.value(o.toString());
                    }
                }
                out.endArray();
            } else if (object == null) {
                out.nullValue();
            } else {
                out.value(object.toString());
            }
        } else {
            out.beginObject();
            for (Node child : value.childList()) {
                out.name(child.toStringKey());
                write0(out, child);
            }
            out.endObject();
        }
    }

    @Override
    public Node read(JsonReader in) throws IOException {
        Node node = NodeFactory.createNode();
        read0(in, node);

        return node;
    }

    private void read0(@NotNull JsonReader reader, @NotNull Node node) throws IOException {
        JsonToken peek = reader.peek();
        switch (peek) {
            case BEGIN_OBJECT:
                reader.beginObject();

                JsonToken peek1;
                while ((peek1 = reader.peek()) != null) {
                    if (peek1 == JsonToken.NAME) {
                        Node childNode = node.attach(reader.nextName());
                        read0(reader, childNode);
                    } else if (peek1 == JsonToken.END_OBJECT || peek1 == JsonToken.END_ARRAY) {
                        reader.endObject();
                        break;
                    } else {
                        throw new IllegalStateException("Unexpected value: " + peek1);
                    }
                }
                break;
            case BEGIN_ARRAY:
                reader.beginArray();

                JsonToken peek2;
                while ((peek2 = reader.peek()) != null) {
                    if (peek2 == JsonToken.END_ARRAY) {
                        reader.endArray();
                        break;
                    } else {
                        if (node.get() == null) {
                            node.set(Lists.newArrayList());
                        }

                        Node listValue = NodeFactory.createNode();
                        node.set(listValue);

                        read0(reader, listValue);
                    }
                }
                break;
            case STRING:
                node.set(reader.nextString());
                break;
            case NUMBER:
                String number = reader.nextString();
                if (number.contains(".")) {
                    node.set(Double.parseDouble(number));
                } else {
                    long l = Long.parseLong(number);
                    if (l <= Integer.MAX_VALUE && l >= Integer.MIN_VALUE) {
                        node.set((int) l);
                    } else {
                        node.set(l);
                    }
                }
                break;
            case BOOLEAN:
                node.set(reader.nextBoolean());
                break;
            case NULL:
                reader.nextNull();
                node.set(null);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + peek);
        }
    }
}
