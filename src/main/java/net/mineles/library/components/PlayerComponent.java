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

package net.mineles.library.components;

import com.cryptomorin.xseries.XSound;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.mineles.library.metadata.Metadata;
import net.mineles.library.metadata.store.MetadataMap;
import net.mineles.library.plugin.BukkitPlugin;
import net.mineles.library.utils.text.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public final class PlayerComponent {
    private final @NotNull BukkitPlugin plugin;

    private final @NotNull UUID uniqueId;
    private final @NotNull String name;

    private PlayerComponent(@NotNull BukkitPlugin plugin,
                            @NotNull UUID uniqueId,
                            @NotNull String name) {
        this.plugin = plugin;
        this.uniqueId = uniqueId;
        this.name = name;
    }

    @NotNull
    public static PlayerComponent of(@NotNull BukkitPlugin plugin,
                                     @NotNull UUID uniqueId,
                                     @NotNull String name) {
        return new PlayerComponent(plugin, uniqueId, name);
    }

    @NotNull
    public static PlayerComponent from(@NotNull BukkitPlugin plugin,
                                       @NotNull Player player) {
        return of(plugin, player.getUniqueId(), player.getName());
    }

    public @Nullable Player getHandle() {
        return Bukkit.getPlayer(this.uniqueId);
    }

    public @NotNull BukkitPlugin getPlugin() {
        return this.plugin;
    }

    public @NotNull UUID getUniqueId() {
        return this.uniqueId;
    }

    public @NotNull String getName() {
        return this.name;
    }

    public @Nullable MetadataMap getMetadataMap() {
        return this.plugin.getMetadataStore().get(this.uniqueId);
    }

    public @Nullable <T> T getMetadata(@NotNull String key) {
        return this.plugin.getMetadataStore().get(this.uniqueId, key);
    }

    public @Nullable <T> T getMetadata(@NotNull Enum<?> key) {
        return getMetadata(key.name());
    }

    public @Nullable <T> T getMetadata(@NotNull String key, @NotNull Class<T> type) {
        Metadata<T> metadata = getMetadataMap().get(key, type);

        return metadata != null ? metadata.getValue() : null;
    }

    public @Nullable <T> T getMetadata(@NotNull Enum<?> key, @NotNull Class<T> type) {
        return getMetadata(key.name(), type);
    }

    public <T> boolean ifPresent(@NotNull String key, @NotNull Consumer<? super T> consumer) {
        return getMetadataMap().ifPresent(key, consumer);
    }

    public <T> boolean ifPresent(@NotNull Enum<?> key, @NotNull Consumer<? super T> consumer) {
        return ifPresent(key.name(), consumer);
    }

    public void putMetadata(@NotNull Metadata<?> metadata) {
        MetadataMap metadataMap = this.plugin.getMetadataStore().getOrDefault(this.uniqueId, new MetadataMap());
        metadataMap.put(metadata);

        this.plugin.getMetadataStore().put(this.uniqueId, metadataMap);
    }

    public void putMetadata(@NotNull String key, @NotNull Object value) {
        Metadata<?> metadata = Metadata.create(key, value);
        putMetadata(metadata);
    }

    public void putMetadata(@NotNull Enum<?> key, @NotNull Object value) {
        putMetadata(key.name(), value);
    }

    public boolean hasMetadata(@NotNull String key) {
        return getMetadataMap() != null && getMetadataMap().contains(key);
    }

    public boolean hasMetadata(@NotNull Enum<?> key) {
        return hasMetadata(key.name());
    }

    public void removeMetadata(@NotNull String key) {
        getMetadataMap().remove(key);
    }

    public void removeMetadata(@NotNull Enum<?> key) {
        removeMetadata(key.name());
    }

    public boolean isOnline() {
        Player player = Bukkit.getPlayer(this.uniqueId);
        return player != null && player.isOnline();
    }

    public @Nullable LocationComponent getLocation() {
        Player player = Bukkit.getPlayer(this.uniqueId);

        return player != null && player.isOnline()
                ? LocationComponent.from(player.getLocation().clone())
                : null;
    }

    public void teleport(@NotNull LocationComponent location) {
        sync(player -> player.teleport(location.getAsBukkitLocation()));
    }

    public void switchServer(@NotNull String targetServer) {
        sync(bukkitPlayer -> {
            ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
            dataOutput.writeUTF("Connect");
            dataOutput.writeUTF(targetServer);

            bukkitPlayer.sendPluginMessage(this.plugin.getPlugin(), "BungeeCord", dataOutput.toByteArray());
        });
    }

    public void showAllPlayers() {
        sync(player -> {
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                onlinePlayer.showPlayer(this.plugin.getPlugin(), player);
                player.showPlayer(this.plugin.getPlugin(), onlinePlayer);
            });
        });
    }

    public void hideAllPlayers() {
        sync(player -> {
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                onlinePlayer.hidePlayer(this.plugin.getPlugin(), player);
                player.hidePlayer(this.plugin.getPlugin(), onlinePlayer);
            });
        });
    }

    public void hidePlayer(@NotNull Player player) {
        sync(p -> p.hidePlayer(this.plugin.getPlugin(), player));
    }

    public void hideHimselfForPlayer(@NotNull Player player) {
        sync(p -> player.hidePlayer(this.plugin.getPlugin(), p));
    }

    public void vanish() {
        sync(player -> {
            player.setMetadata("vanished", new FixedMetadataValue(this.plugin.getPlugin(), true));
            putMetadata("vanished", true);

            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                onlinePlayer.hidePlayer(this.plugin.getPlugin(), player);
            });
        });
    }

    public void unvanish() {
        sync(player -> {
            player.removeMetadata("vanished", this.plugin.getPlugin());
            removeMetadata("vanished");

            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                onlinePlayer.showPlayer(this.plugin.getPlugin(), player);
            });
        });
    }

    public boolean isAllowedFlight() {
        return getHandle().getAllowFlight();
    }

    public void setAllowFlight(boolean allowFlight) {
        sync(player -> player.setAllowFlight(allowFlight));
    }

    public void setFlySpeed(float speed) {
        sync(player -> player.setFlySpeed(speed));
    }

    public @NotNull InventoryView openInventory(@NotNull Inventory inventory) {
        return getHandle().openInventory(inventory);
    }

    public boolean isInventoryOpen() {
        Player player = Bukkit.getPlayer(this.uniqueId);
        return player != null && player.getOpenInventory().getType() == InventoryType.CHEST;
    }

    public void closeInventory() {
        sync(Player::closeInventory);
    }

    public void playSound(@NotNull String sound) {
        Optional<XSound> xSound = XSound.matchXSound(sound);
        if (xSound.isPresent()) {
            playSound(xSound.get());
        } else {
            sync(player -> player.playSound(player.getLocation(), sound, 1, 1));
        }
    }

    public void playSound(@Nullable XSound sound) {
        if (sound == null) {
            return;
        }

        sync(sound::play);
    }

    public void clearChat() {
        sync(player -> {
            for (int i = 0; i < 100; i++) {
                sendEmptyMessage();
            }
        });
    }

    public void sendEmptyMessage() {
        sendMessage(Component.empty());
    }

    public void sendMessage(@NotNull String message) {
        sendMessage(message, Map.of());
    }

    public void sendMessage(@NotNull String message,
                            @NotNull Map<String, String> placeholders) {
        Component component = ComponentSerializer.deserialize(getHandle(), message, placeholders);
        sendMessage(component);
    }

    public void sendMessage(@NotNull Component component) {
        sync(player -> player.sendMessage(component));
    }

    public void sendMessage(@NotNull List<String> messages) {
        messages.forEach(this::sendMessage);
    }

    public void sendMessage(@NotNull List<String> messages,
                            @NotNull Map<String, String> placeholders) {
        messages.forEach(message -> sendMessage(message, placeholders));
    }

    public void sendActionBar(@NotNull String message) {
        sendActionBar(message, Map.of());
    }

    public void sendActionBar(@NotNull String message,
                              @NotNull Map<String, String> placeholders) {
        Component component = ComponentSerializer.deserialize(getHandle(), message, placeholders);
        sendActionBar(component);
    }

    public void sendActionBar(@NotNull Component component) {
        sync(player -> player.sendActionBar(component));
    }

    public void sendTitle(@NotNull String title) {
        sendTitle(title, 200, 600, 200);
    }

    public void sendTitle(@NotNull String title,
                          @NotNull Map<String, String> placeholders) {
        sendTitle(title, placeholders, 600, 200, 200);
    }

    public void sendTitle(@NotNull String title,
                          @NotNull String subtitle) {
        sendTitle(title, subtitle, 200, 600, 200);
    }

    public void sendTitle(@NotNull String title,
                          @NotNull String subtitle,
                          @NotNull Map<String, String> placeholders) {
        sendTitle(title, subtitle, placeholders, 600, 200, 200);
    }

    public void sendTitle(@NotNull String title, long fadeIn, long stay, long fadeOut) {
        sendTitle(title, Map.of(), stay, fadeOut, fadeIn);
    }

    public void sendTitle(@NotNull String title,
                          @NotNull String subtitle,
                          long fadeIn, long stay, long fadeOut) {
        sendTitle(title, subtitle, Map.of(), stay, fadeOut, fadeIn);
    }

    public void sendTitle(@NotNull String title,
                          @NotNull Map<String, String> placeholders,
                          long stay, long fadeOut, long fadeIn) {
        Component titleComponent = ComponentSerializer.deserialize(getHandle(), title, placeholders);
        sendTitle(titleComponent, fadeIn, stay, fadeOut);
    }

    public void sendTitle(@NotNull String title,
                          @NotNull String subtitle,
                          @NotNull Map<String, String> placeholders,
                          long stay, long fadeOut, long fadeIn) {
        Component titleComponent = ComponentSerializer.deserialize(getHandle(), title, placeholders);
        Component subtitleComponent = ComponentSerializer.deserialize(getHandle(), subtitle, placeholders);
        sendTitle(titleComponent, subtitleComponent, fadeIn, stay, fadeOut);
    }

    public void sendTitle(@NotNull Component title) {
        sendTitle(title, 200, 600, 200);
    }

    public void sendTitle(@NotNull Component title, @NotNull Component subtitle) {
        sendTitle(title, subtitle, 200, 600, 200);
    }

    public void sendTitle(@NotNull Component title,
                          long fadeIn, long stay, long fadeOut) {
        sendTitle(title, Component.empty(), fadeIn, stay, fadeOut);
    }

    public void sendTitle(@NotNull Component title,
                          @NotNull Component subtitle,
                          long fadeIn, long stay, long fadeOut) {
        Title.Times times = Title.Times.times(Duration.ofMillis(fadeIn), Duration.ofMillis(stay), Duration.ofMillis(fadeOut));

        Title titleComponent = Title.title(title, subtitle, times);
        sync(player -> player.showTitle(titleComponent));
    }

    public void sync(@NotNull Consumer<Player> consumer) {
        if (Bukkit.isPrimaryThread()) {
            Player player = Bukkit.getPlayer(this.uniqueId);
            if (player != null) {
                consumer.accept(player);
            }
        } else {
            Bukkit.getScheduler().runTask(this.plugin.getPlugin(), () -> {
                Player player = Bukkit.getPlayer(this.uniqueId);
                if (player != null) {
                    consumer.accept(player);
                }
            });
        }
    }

    public void syncLater(@NotNull Consumer<Player> consumer, long delay) {
        Bukkit.getScheduler().runTaskLater(this.plugin.getPlugin(), () -> {
            Player player = Bukkit.getPlayer(this.uniqueId);
            if (player != null) {
                consumer.accept(player);
            }
        }, delay);
    }
}
