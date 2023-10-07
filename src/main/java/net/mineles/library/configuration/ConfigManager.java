package net.mineles.library.configuration;

import net.mineles.library.configuration.serializers.RedissonConfigSerializer;
import net.mineles.library.manager.AbstractManager;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Path;

public final class ConfigManager extends AbstractManager<String, Config> {
    private final @NotNull Path dataFolder;
    private final @NotNull KeyInjector keyInjector;
    private final @NotNull ConfigurationOptions options;

    public ConfigManager(@NotNull Path dataFolder) {
        this.dataFolder = dataFolder;
        this.keyInjector = new KeyInjector();
        this.options = ConfigurationOptions.defaults()
                .serializers(builder -> {
                    builder.register(org.redisson.config.Config.class, RedissonConfigSerializer.INSTANCE);
                });
    }

    public @NotNull Config register(@NotNull String name, @NotNull Config config) {
        return put(name, config);
    }

    public @NotNull Config register(@NotNull String name, @NotNull Path path) {
        Config config = ConfigBuilder.builder(path)
                .options(this.options)
                .build();
        return put(name, config);
    }

    public @NotNull Config register(@NotNull String name, @NotNull Path path, @NotNull InputStream stream) {
        Config config = ConfigBuilder.builder(path)
                .options(this.options)
                .resource(stream)
                .build();
        return put(name, config);
    }

    public @NotNull Config register(@NotNull String name, @NotNull Path path, @NotNull Class<?> clazz, @NotNull String resource) {
        Config config = ConfigBuilder.builder(path)
                .options(this.options)
                .resource(clazz, resource)
                .build();
        return put(name, config);
    }

    public @NotNull Config register(@NotNull String name, @NotNull String... path) {
        Config config = ConfigBuilder.builder(this.dataFolder, path)
                .options(this.options)
                .build();
        return put(name, config);
    }

    public @NotNull Config register(@NotNull String name, @NotNull InputStream stream, @NotNull String... path) {
        Config config = ConfigBuilder.builder(this.dataFolder, path)
                .options(this.options)
                .resource(stream)
                .build();
        return put(name, config);
    }

    public @NotNull Config register(@NotNull String name, @NotNull Class<?> clazz, @NotNull String resource, @NotNull String... path) {
        Config config = ConfigBuilder.builder(this.dataFolder, path)
                .options(this.options)
                .resource(clazz, resource)
                .build();
        return put(name, config);
    }

    public void injectKeys(@NotNull Class<?> clazz, @NotNull Config config) {
        this.keyInjector.inject(clazz, config);
    }

    public void injectKeys(@NotNull Class<?> clazz, @NotNull String name) {
        this.keyInjector.inject(clazz, get(name));
    }

    public void injectKeys(@NotNull Class<?> clazz, @NotNull ConfigurationNode node) {
        this.keyInjector.inject(clazz, node);
    }

    public void injectKeys(@NotNull Field field, @NotNull Config config) {
        this.keyInjector.inject(field, config);
    }

    public void injectKeys(@NotNull Field field, @NotNull ConfigurationNode node) {
        this.keyInjector.inject(field, node);
    }
}
