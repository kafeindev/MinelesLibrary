/*
 * MIT License
 *
 * Copyright (c) 2023 Kafein
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

package net.mineles.library.property;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class AttributeMap {
    private final Map<String, Attribute<?>> attributes;

    public AttributeMap() {
        this.attributes = Maps.newHashMap();
    }

    public static AttributeMap create() {
        return new AttributeMap();
    }

    public static AttributeMap create(@NotNull Attribute<?>... attributes) {
        AttributeMap attributeMap = new AttributeMap();
        attributeMap.set(attributes);

        return attributeMap;
    }

    public static AttributeMap create(@NotNull Map<String, Attribute<?>> attributes) {
        AttributeMap attributeMap = new AttributeMap();
        attributeMap.set(attributes);

        return attributeMap;
    }

    public static AttributeMap create(@NotNull String key, @Nullable Object value) {
        AttributeMap attributeMap = new AttributeMap();
        attributeMap.set(key, value);

        return attributeMap;
    }

    public static AttributeMap create(@NotNull Enum<?> key, @Nullable Object value) {
        return create(key.name(), value);
    }

    public void set(@NotNull Attribute<?> attribute) {
        this.attributes.put(attribute.getKey(), attribute);
    }

    public void set(@NotNull Attribute<?>... attributes) {
        for (Attribute<?> attribute : attributes) {
            this.set(attribute);
        }
    }

    public void set(@NotNull Map<String, Attribute<?>> attributes) {
        this.attributes.putAll(attributes);
    }

    public void set(@NotNull String key, @Nullable Object value) {
        Attribute<?> attribute = Attribute.of(key, value);
        this.set(attribute);
    }

    public void set(@NotNull Enum<?> key, @Nullable Object value) {
        this.set(key.name(), value);
    }

    @SuppressWarnings("unchecked")
    public <T> @Nullable Attribute<T> get(@NotNull String key) {
        return (Attribute<T>) this.attributes.get(key);
    }

    public <T> @Nullable Attribute<T> get(@NotNull String key,
                                          @NotNull Class<T> type) {
        return (Attribute<T>) this.attributes.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> @Nullable Attribute<T> get(@NotNull String key,
                                          @NotNull Attribute<T> defaultValue) {
        return (Attribute<T>) this.attributes.getOrDefault(key, defaultValue);
    }

    public <T> @Nullable Attribute<T> get(@NotNull String key,
                                          @NotNull Attribute<T> defaultValue,
                                          @NotNull Class<T> type) {
        return get(key, defaultValue);
    }

    public <T> @Nullable Attribute<T> get(@NotNull Enum<?> key) {
        return this.get(key.name());
    }

    public <T> @Nullable Attribute<T> get(@NotNull Enum<?> key,
                                          @NotNull Class<T> type) {
        return this.get(key.name(), type);
    }

    public <T> @Nullable Attribute<T> get(@NotNull Enum<?> key,
                                          @NotNull Attribute<T> defaultValue) {
        return this.get(key.name(), defaultValue);
    }

    public <T> @Nullable Attribute<T> get(@NotNull Enum<?> key,
                                          @NotNull Attribute<T> defaultValue,
                                          @NotNull Class<T> type) {
        return this.get(key.name(), defaultValue, type);
    }

    public <T> @Nullable T getValue(@NotNull String key) {
        Attribute<T> attribute = this.get(key);
        return attribute == null ? null : attribute.getValue();
    }

    public <T> @Nullable T getValue(@NotNull String key,
                                    @NotNull Class<T> type) {
        Attribute<T> attribute = this.get(key, type);
        return attribute == null ? null : attribute.getValue();
    }

    public <T> @Nullable T getValue(@NotNull String key,
                                    @NotNull T defaultValue) {
        Attribute<T> attribute = this.get(key);
        return attribute == null ? defaultValue : attribute.getValue();
    }

    public <T> @Nullable T getValue(@NotNull String key,
                                    @NotNull T defaultValue,
                                    @NotNull Class<T> type) {
        Attribute<T> attribute = this.get(key, type);
        return attribute == null ? defaultValue : attribute.getValue();
    }

    public <T> @Nullable T getValue(@NotNull Enum<?> key) {
        return this.getValue(key.name());
    }

    public <T> @Nullable T getValue(@NotNull Enum<?> key,
                                    @NotNull Class<T> type) {
        return this.getValue(key.name(), type);
    }

    public <T> @Nullable T getValue(@NotNull Enum<?> key,
                                    @NotNull T defaultValue) {
        return this.getValue(key.name(), defaultValue);
    }

    public <T> @Nullable T getValue(@NotNull Enum<?> key,
                                    @NotNull T defaultValue,
                                    @NotNull Class<T> type) {
        return this.getValue(key.name(), defaultValue, type);
    }

    public boolean has(@NotNull String key) {
        return this.attributes.containsKey(key);
    }

    public boolean has(@NotNull Enum<?> key) {
        return this.has(key.name());
    }
}
