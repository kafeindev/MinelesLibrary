package net.mineles.library.configuration;

import net.mineles.library.manager.AbstractManager;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Path;

public final class ConfigManager extends AbstractManager<String, Config> {
    private final @NotNull Path dataFolder;
    private final @NotNull KeyInjector keyInjector;

    public ConfigManager(@NotNull Path dataFolder) {
        this.dataFolder = dataFolder;
        this.keyInjector = new KeyInjector();
    }

    public @NotNull Config register(@NotNull String name, @NotNull Config config) {
        return put(name, config);
    }

    public @NotNull Config register(@NotNull String name, @NotNull Path path) {
        Config config = ConfigBuilder.builder(path)
                .init()
                .build();
        return put(name, config);
    }

    public @NotNull Config register(@NotNull String name, @NotNull Path path, @NotNull InputStream stream) {
        Config config = ConfigBuilder.builder(path)
                .initViaStream(stream)
                .build();
        return put(name, config);
    }

    public @NotNull Config register(@NotNull String name, @NotNull Path path, @NotNull Class<?> clazz, @NotNull String resource) {
        Config config = ConfigBuilder.builder(path)
                .initFromResource(clazz, resource)
                .build();
        return put(name, config);
    }

    public @NotNull Config register(@NotNull String name, @NotNull String... path) {
        Config config = ConfigBuilder.builder(this.dataFolder, path)
                .init()
                .build();
        return put(name, config);
    }

    public @NotNull Config register(@NotNull String name, @NotNull InputStream stream, @NotNull String... path) {
        Config config = ConfigBuilder.builder(this.dataFolder, path)
                .initViaStream(stream)
                .build();
        return put(name, config);
    }

    public @NotNull Config register(@NotNull String name, @NotNull Class<?> clazz, @NotNull String resource, @NotNull String... path) {
        Config config = ConfigBuilder.builder(this.dataFolder, path)
                .initFromResource(clazz, resource)
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

    public void injectKeys(@NotNull Field field, @NotNull ConfigurationNode node) {
        this.keyInjector.inject(field, node);
    }
}
