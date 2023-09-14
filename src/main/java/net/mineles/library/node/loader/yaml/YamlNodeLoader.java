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
import net.mineles.library.node.NodeException;
import net.mineles.library.node.loader.NodeAdapter;
import net.mineles.library.node.loader.NodeLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public final class YamlNodeLoader extends NodeLoader {
    private final @NotNull NodeAdapter nodeAdapter;

    YamlNodeLoader(@NotNull Builder builder) {
        super(builder);
        this.nodeAdapter = new YamlNodeAdapter(builder.dumperOptions(), builder.loaderOptions());
    }

    @Override
    protected @Nullable Node loadInternal(@NotNull Node node, @NotNull BufferedReader reader) throws NodeException {
        try {
            return this.nodeAdapter.read(reader);
        } catch (Exception e) {
            throw new NodeException("Failed to load node", e);
        }
    }

    @Override
    protected boolean saveInternal(@NotNull Node node, @NotNull BufferedWriter writer) throws NodeException {
        try {
            this.nodeAdapter.write(writer, node);
            return true;
        } catch (Exception e) {
            throw new NodeException("Failed to save node", e);
        }
    }

    @NotNull
    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder extends NodeLoader.Builder<Builder> {
        private DumperOptions dumperOptions = new DumperOptions();
        private LoaderOptions loaderOptions = new LoaderOptions();

        private Builder() {
        }

        public @NotNull Builder width(int width) {
            this.dumperOptions.setWidth(width);
            return this;
        }

        public @NotNull Builder flowStyle(@NotNull DumperOptions.FlowStyle flowStyle) {
            this.dumperOptions.setDefaultFlowStyle(flowStyle);
            return this;
        }

        public @NotNull Builder prettyFlow(boolean prettyFlow) {
            this.dumperOptions.setPrettyFlow(prettyFlow);
            return this;
        }

        public @NotNull Builder scalarStyle(@NotNull DumperOptions.ScalarStyle scalarStyle) {
            this.dumperOptions.setDefaultScalarStyle(scalarStyle);
            return this;
        }

        public @NotNull Builder allowUnicode(boolean allowUnicode) {
            this.dumperOptions.setAllowUnicode(allowUnicode);
            return this;
        }

        public @NotNull Builder canonical(boolean canonical) {
            this.dumperOptions.setCanonical(canonical);
            return this;
        }

        public @NotNull Builder dumperOptions(@NotNull DumperOptions dumperOptions) {
            this.dumperOptions = dumperOptions;
            return this;
        }

        public @NotNull DumperOptions dumperOptions() {
            return this.dumperOptions;
        }

        public @NotNull Builder loaderOptions(@NotNull LoaderOptions loaderOptions) {
            this.loaderOptions = loaderOptions;
            return this;
        }

        public @NotNull LoaderOptions loaderOptions() {
            return this.loaderOptions;
        }

        public @NotNull Builder processComments(boolean processComments) {
            this.loaderOptions.setProcessComments(processComments);
            return this;
        }

        public @NotNull Builder codePointLimit(int codePointLimit) {
            this.loaderOptions.setCodePointLimit(codePointLimit);
            return this;
        }

        @Override
        public @NotNull NodeLoader build() {
            this.dumperOptions.setIndent(this.indent);
            return new YamlNodeLoader(this);
        }
    }
}
