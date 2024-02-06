package net.mineles.library.npc;

import net.mineles.library.components.LocationComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.mineles.library.libs.configurate.ConfigurationNode;
import net.mineles.library.libs.configurate.serialize.SerializationException;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface Npc {
    void spawn();

    void spawn(@NotNull Player player);

    void spawn(@NotNull Player... players);

    void spawn(@NotNull Iterable<? extends Player> players);

    void update();

    void update(@NotNull Player player);

    void update(@NotNull Player... players);

    void update(@NotNull Iterable<? extends Player> players);

    void destroy();

    void destroy(@NotNull Player player);

    void destroy(@NotNull Player... players);

    void destroy(@NotNull Iterable<? extends Player> players);

    String getName();

    void setName(@NotNull String name);

    String getDisplayName();

    void setDisplayName(@NotNull String displayName);

    @Nullable LocationComponent getLocation();

    void setLocation(@NotNull LocationComponent location);

    Map<EquipmentSlot, ItemStack> getEquipments();

    @Nullable ItemStack getEquipment(@NotNull EquipmentSlot equipmentSlot);

    void setEquipment(@NotNull EquipmentSlot equipmentSlot, @Nullable ItemStack itemStack);

    void setEquipment(@NotNull Map<EquipmentSlot, ItemStack> equipment);

    boolean isEveryoneCanSee();

    Set<UUID> getViewers();

    boolean isViewing(@NotNull UUID uuid);

    void save(@NotNull Path path);

    void save(@NotNull ConfigurationNode node) throws SerializationException;

    default @NotNull <T extends Npc> T getAs(@NotNull Class<T> type) {
        return type.cast(this);
    }
}
