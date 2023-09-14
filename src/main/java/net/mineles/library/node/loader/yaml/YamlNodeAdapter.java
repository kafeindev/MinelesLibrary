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

package net.mineles.library.node.loader.yaml;

import net.mineles.library.node.Node;
import net.mineles.library.node.NodeFactory;
import net.mineles.library.node.loader.NodeAdapter;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public final class YamlNodeAdapter implements NodeAdapter {
    private final @NotNull Yaml yaml;

    public YamlNodeAdapter(@NotNull DumperOptions dumperOptions,
                           @NotNull LoaderOptions loaderOptions) {
        this.yaml = new Yaml(new Constructor(loaderOptions), new Representer(dumperOptions), dumperOptions, loaderOptions);
    }

    @Override
    public void write(@NotNull BufferedWriter writer, @NotNull Node value) throws IOException {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(value.toStringKey(), this.convert(value));

        this.yaml.dump(map, writer);
    }

    private Object convert(Node node) {
        if (node.childList().isEmpty()) {
            return node.get();
        } else {
            Map<String, Object> map = new LinkedHashMap<>();
            for (Node child : node.childList()) {
                Object childMap = this.convert(child);
                map.put(child.toStringKey(), childMap);
            }
            return map;
        }
    }

    @Override
    public Node read(@NotNull BufferedReader reader) throws IOException {
        Map<String, Object> map = this.yaml.load(reader);

        Node node = NodeFactory.createNode(map.keySet().iterator().next());
        this.read0(map.values().iterator().next(), node);

        return node;
    }

    private void read0(Object data, Node node) {
        if (data instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) data;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                Node child = node.attach(entry.getKey().toString());
                this.read0(entry.getValue(), child);
            }
        } else {
            node.set(data);
        }
    }
}
