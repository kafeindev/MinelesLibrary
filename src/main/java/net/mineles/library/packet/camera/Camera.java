package net.mineles.library.packet.camera;

import com.comphenix.protocol.events.PacketContainer;
import net.mineles.library.components.LocationComponent;
import net.mineles.library.metadata.Metadata;
import net.mineles.library.packet.PacketContainerFactory;
import net.mineles.library.plugin.BukkitPlugin;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class Camera {
    private final int entityId;

    public Camera() {
        this.entityId = (int) (Math.random() * Integer.MAX_VALUE);
    }

    public Camera(int entityId) {
        this.entityId = entityId;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public void apply(@NotNull BukkitPlugin plugin,
                      @NotNull Player player,
                      @NotNull LocationComponent location) {
        PacketContainer spawnPacket = PacketContainerFactory.spawnEntity(this.entityId, EntityType.ARMOR_STAND, location);
        plugin.getProtocolManager().sendServerPacket(player, spawnPacket);

        PacketContainer metadataPacket = PacketContainerFactory.createAndApplyMetaData(this.entityId, 0, (byte) 0x20);
        plugin.getProtocolManager().sendServerPacket(player, metadataPacket);

        PacketContainer headRotationPacket = PacketContainerFactory.headRotation(this.entityId,
                location.getYaw());
        plugin.getProtocolManager().sendServerPacket(player, headRotationPacket);

        PacketContainer teleportPacket = PacketContainerFactory.teleportEntity(this.entityId,
                location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        plugin.getProtocolManager().sendServerPacket(player, teleportPacket);

        PacketContainer gameStateChangePacket = PacketContainerFactory.changeGameState(3, 3);
        plugin.getProtocolManager().sendServerPacket(player, gameStateChangePacket);

        PacketContainer spectatePacket = PacketContainerFactory.spectateEntity(this.entityId);
        plugin.getProtocolManager().sendServerPacket(player, spectatePacket);

        plugin.getMetadataStore().put(player.getUniqueId(), Metadata.create("camera", this));
    }

    public void remove(@NotNull BukkitPlugin plugin,
                       @NotNull Player player) {
        PacketContainer spectatePacket = PacketContainerFactory.spectateEntity(player.getEntityId());
        plugin.getProtocolManager().sendServerPacket(player, spectatePacket);

        PacketContainer destroyPacket = PacketContainerFactory.destroyEntity(this.entityId);
        plugin.getProtocolManager().sendServerPacket(player, destroyPacket);

        plugin.getMetadataStore().remove(player.getUniqueId(), "camera");
    }
}
