package net.mineles.library.npc.abstraction;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.mineles.library.components.ItemComponent;
import net.mineles.library.components.LocationComponent;
import net.mineles.library.npc.Npc;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.mineles.library.libs.configurate.ConfigurateException;
import net.mineles.library.libs.configurate.ConfigurationNode;
import net.mineles.library.libs.configurate.gson.GsonConfigurationLoader;
import net.mineles.library.libs.configurate.serialize.SerializationException;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractNpc implements Npc {
    private final Set<UUID> viewers;
    private final Map<EquipmentSlot, ItemStack> equipments;

    private String name;
    private String displayName;
    private LocationComponent location;
    private boolean canEveryoneSee;

    protected AbstractNpc(Npc npc) {
        this(npc.getName(), npc.getDisplayName(), npc.getLocation(), npc.getEquipments());
    }

    protected AbstractNpc(String name) {
        this(name, name);
    }

    protected AbstractNpc(String name, String displayName) {
        this(name, displayName, null);
    }

    protected AbstractNpc(String name, String displayName, @Nullable LocationComponent location) {
        this(name, displayName, location, null);
    }

    protected AbstractNpc(String name, String displayName, @Nullable LocationComponent location, @Nullable Map<EquipmentSlot, ItemStack> equipments) {
        this.viewers = Sets.newHashSet();
        this.equipments = equipments == null ? Maps.newHashMap() : equipments;
        this.name = name;
        this.displayName = displayName;
        this.location = location;
    }

    protected AbstractNpc(Path path) throws ConfigurateException {
        this(GsonConfigurationLoader.builder()
                .path(path)
                .build()
                .load());
    }

    protected AbstractNpc(ConfigurationNode node) throws ConfigurateException {
        this(
                checkNotNull(node.node("name").getString(), "name"),
                checkNotNull(node.node("displayName").getString(), "displayName"),
                node.node("location").get(LocationComponent.class),
                deserializeEquipments(node.node("equipments"))
        );
    }

    @Override
    public void spawn() {
        this.canEveryoneSee = true;
        spawn(Bukkit.getOnlinePlayers());
    }

    @Override
    public void spawn(@NotNull Player player) {
        this.viewers.add(player.getUniqueId());
    }

    @Override
    public void spawn(@NotNull Player... players) {
        for (Player player : players) {
            spawn(player);
        }
    }

    @Override
    public void spawn(@NotNull Iterable<? extends Player> players) {
        for (Player player : players) {
            spawn(player);
        }
    }

    @Override
    public void destroy() {
        this.canEveryoneSee = false;
        destroy(Bukkit.getOnlinePlayers());
    }

    @Override
    public void destroy(@NotNull Player player) {
        this.viewers.remove(player.getUniqueId());
    }

    @Override
    public void destroy(@NotNull Player... players) {
        for (Player player : players) {
            destroy(player);
        }
    }

    @Override
    public void destroy(@NotNull Iterable<? extends Player> players) {
        for (Player player : players) {
            destroy(player);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public void setDisplayName(@NotNull String displayName) {
        this.displayName = displayName;
    }

    @Override
    public @Nullable LocationComponent getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(@NotNull LocationComponent location) {
        this.location = location;
    }

    @Override
    public Map<EquipmentSlot, ItemStack> getEquipments() {
        return this.equipments;
    }

    @Override
    public @Nullable ItemStack getEquipment(@NotNull EquipmentSlot equipmentSlot) {
        return this.equipments.get(equipmentSlot);
    }

    @Override
    public void setEquipment(@NotNull EquipmentSlot equipmentSlot,
                             @Nullable ItemStack itemStack) {
        this.equipments.put(equipmentSlot, itemStack);
    }

    @Override
    public void setEquipment(@NotNull Map<EquipmentSlot, ItemStack> equipment) {
        this.equipments.clear();
        this.equipments.putAll(equipment);
    }

    @Override
    public boolean isEveryoneCanSee() {
        return this.canEveryoneSee;
    }

    @Override
    public Set<UUID> getViewers() {
        return this.viewers;
    }

    @Override
    public void save(@NotNull Path path) {
        GsonConfigurationLoader loader = GsonConfigurationLoader.builder()
                .path(path)
                .build();

        try {
            ConfigurationNode node = loader.createNode();
            save(node);

            loader.save(node);
        } catch (ConfigurateException e) {
            throw new RuntimeException("Failed to save npc to " + path, e);
        }
    }

    @Override
    public void save(@NotNull ConfigurationNode node) throws SerializationException {
        node.node("name").set(this.name);
        node.node("displayName").set(this.displayName);
        node.node("location").set(this.location);
        serializeEquipments(node.node("equipments"), this.equipments);
    }

    static @NotNull Map<EquipmentSlot, ItemStack> deserializeEquipments(@NotNull ConfigurationNode node) {
        Map<EquipmentSlot, ItemStack> equipments = Maps.newHashMap();

        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            ConfigurationNode equipmentNode = node.node(equipmentSlot.name());
            if (equipmentNode.empty()) continue;

            ItemStack itemStack = ItemComponent.SERIALIZER.deserializeItemStack(equipmentNode.getString());
            equipments.put(equipmentSlot, itemStack);
        }

        return equipments;
    }

    static void serializeEquipments(@NotNull ConfigurationNode node,
                                    @NotNull Map<EquipmentSlot, ItemStack> equipments) throws SerializationException{
        for (Map.Entry<EquipmentSlot, ItemStack> entry : equipments.entrySet()) {
            String serialized = ItemComponent.SERIALIZER.serializeItemStack(entry.getValue());
            node.node(entry.getKey().name()).set(serialized);
        }
    }
}
