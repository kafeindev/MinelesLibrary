package net.mineles.library.configuration;

import net.mineles.library.components.CuboidComponent;
import net.mineles.library.components.ItemComponent;
import net.mineles.library.components.LocationComponent;
import net.mineles.library.configuration.serializers.CuboidComponentAdapter;
import net.mineles.library.configuration.serializers.ItemComponentAdapter;
import net.mineles.library.configuration.serializers.LocationComponentAdapter;
import net.mineles.library.configuration.serializers.RedisCredentialsAdapter;
import net.mineles.library.manager.AbstractManager;
import net.mineles.library.redis.RedisCredentials;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Path;

public final class ConfigManager extends AbstractManager<String, Config> {
    private final Path dataFolder;
    private final KeyInjector keyInjector;
    private final ConfigurationOptions options;

    public ConfigManager(Path dataFolder) {
        this.dataFolder = dataFolder;
        this.keyInjector = new KeyInjector();
        this.options = ConfigurationOptions.defaults()
                .serializers(builder -> {
                    builder.register(RedisCredentials.class, RedisCredentialsAdapter.INSTANCE);
                    builder.register(LocationComponent.class, LocationComponentAdapter.INSTANCE);
                    builder.register(CuboidComponent.class, CuboidComponentAdapter.INSTANCE);
                    builder.register(ItemComponent.class, ItemComponentAdapter.INSTANCE);
                    //builder.register(ItemStack.class, BukkitItemStackAdapter.INSTANCE);
                });
    }

    public Config register(@NotNull String name, @NotNull Config config) {
        return put(name, config);
    }

    public Config register(@NotNull String name, @NotNull Path path) {
        Config config = ConfigBuilder.builder(path)
                .options(this.options)
                .build();
        return put(name, config);
    }

    public Config register(@NotNull String name, @NotNull Path path, @NotNull InputStream stream) {
        Config config = ConfigBuilder.builder(path)
                .options(this.options)
                .resource(stream)
                .build();
        return put(name, config);
    }

    public Config register(@NotNull String name, @NotNull Path path, @NotNull Class<?> clazz, @NotNull String resource) {
        Config config = ConfigBuilder.builder(path)
                .options(this.options)
                .resource(clazz, resource)
                .build();
        return put(name, config);
    }

    public Config register(@NotNull String name, @NotNull String... path) {
        Config config = ConfigBuilder.builder(this.dataFolder, path)
                .options(this.options)
                .build();
        return put(name, config);
    }

    public Config register(@NotNull String name, @NotNull InputStream stream, @NotNull String... path) {
        Config config = ConfigBuilder.builder(this.dataFolder, path)
                .options(this.options)
                .resource(stream)
                .build();
        return put(name, config);
    }

    public Config register(@NotNull String name, @NotNull Class<?> clazz, @NotNull String resource, @NotNull String... path) {
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

    public Path getDataFolder() {
        return this.dataFolder;
    }

    public KeyInjector getKeyInjector() {
        return this.keyInjector;
    }

    public ConfigurationOptions getOptions() {
        return this.options;
    }
}
