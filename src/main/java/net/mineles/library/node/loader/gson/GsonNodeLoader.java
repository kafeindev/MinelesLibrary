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

import com.google.common.base.Strings;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.mineles.library.node.Node;
import net.mineles.library.node.NodeException;
import net.mineles.library.node.loader.NodeLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public final class GsonNodeLoader extends NodeLoader {
    private final @NotNull String indentFormat;
    private final boolean lenient;
    private final boolean serializeNulls;

    GsonNodeLoader(@NotNull Builder builder) {
        super(builder);
        this.indentFormat = Strings.repeat(" ", builder.indent());
        this.lenient = builder.lenient();
        this.serializeNulls = builder.serializeNulls();
    }

    @Override
    protected @Nullable Node loadInternal(@NotNull Node node, @NotNull BufferedReader reader) throws NodeException {
        try (JsonReader jsonReader = new JsonReader(reader)) {
            jsonReader.setLenient(this.lenient);

            return GsonProvider.getGson().fromJson(jsonReader, Node.class);
        } catch (Exception e) {
            throw new NodeException("Failed to load node", e);
        }
    }

    @Override
    protected boolean saveInternal(@NotNull Node node, @NotNull BufferedWriter writer) throws NodeException {
        try (JsonWriter jsonWriter = new JsonWriter(writer)) {
            jsonWriter.setIndent(this.indentFormat);
            jsonWriter.setLenient(this.lenient);
            jsonWriter.setSerializeNulls(this.serializeNulls);

            GsonProvider.getGson().toJson(node, Node.class, jsonWriter);
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
        private boolean lenient = false;
        private boolean serializeNulls = false;

        private Builder() {
        }

        public @NotNull Builder lenient(boolean lenient) {
            this.lenient = lenient;
            return this;
        }

        public boolean lenient() {
            return this.lenient;
        }

        public @NotNull Builder serializeNulls(boolean serializeNulls) {
            this.serializeNulls = serializeNulls;
            return this;
        }

        public boolean serializeNulls() {
            return this.serializeNulls;
        }

        @Override
        public @NotNull NodeLoader build() {
            return new GsonNodeLoader(this);
        }
    }
}

