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

package net.mineles.library.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import net.mineles.library.components.LocationComponent;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class PacketContainerFactory {
    private static WatcherApplier WATCHER_APPLIER;

    static {
        try {
            Class.forName("com.comphenix.protocol.wrappers.WrappedDataValue");
            WATCHER_APPLIER = new NewerWatcherApplier();
        } catch (ClassNotFoundException e) {
            WATCHER_APPLIER = new OlderWatcherApplier();
        }
    }

    private PacketContainerFactory() {
    }

    @NotNull
    public static PacketContainer spawnEntity(int id,
                                              @NotNull EntityType entityType,
                                              @NotNull LocationComponent location) {
        return spawnEntity(id, UUID.randomUUID(), entityType, location);
    }

    @NotNull
    public static PacketContainer spawnEntity(int id,
                                              @NotNull UUID uniqueId,
                                              @NotNull EntityType entityType,
                                              @NotNull LocationComponent location) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
        packet.getIntegers().write(0, id);
        packet.getUUIDs().write(0, uniqueId);
        packet.getEntityTypeModifier().write(0, entityType);

        packet.getDoubles()
                .write(0, location.getX())
                .write(1, location.getY())
                .write(2, location.getZ());

        packet.getIntegers()
                .write(3, (int) (location.getPitch() * 256.0F / 360.0F))
                .write(4, (int) (location.getYaw() * 256.0F / 360.0F));
        return packet;
    }

    @NotNull
    public static PacketContainer collectItem(int id, int collectorId) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.COLLECT);
        packet.getIntegers()
                .write(0, id)
                .write(1, collectorId);
        return packet;
    }

    @NotNull
    public static PacketContainer animation(int id, int animationId) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ANIMATION);
        packet.getIntegers()
                .write(0, id)
                .write(1, animationId);
        return packet;
    }

    @NotNull
    public static PacketContainer entityEquipment(int id, @NotNull Pair<EnumWrappers.ItemSlot, ItemStack>... items) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
        packet.getIntegers().write(0, id);
        packet.getSlotStackPairLists().write(0, List.of(items));

        return packet;
    }

    @NotNull
    public static PacketContainer destroyEntity(int id) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        packet.getIntLists().write(0, List.of(new Integer[]{id}));
        return packet;
    }

    @NotNull
    public static PacketContainer spectateEntity(int id) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.CAMERA);
        packet.getIntegers().write(0, id);
        return packet;
    }

    @NotNull
    public static PacketContainer mountEntity(int id, int... passengers) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.MOUNT);
        packet.getIntegers().write(0, id);
        packet.getIntegerArrays().write(0, passengers);
        return packet;
    }

    @NotNull
    public static PacketContainer teleportEntity(int id, double x, double y, double z, float yaw, float pitch) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT);
        packet.getIntegers().write(0, id);
        packet.getDoubles()
                .write(0, x)
                .write(1, y)
                .write(2, z);
        packet.getBytes()
                .write(0, (byte) (yaw * 256.0F / 360.0F))
                .write(1, (byte) (pitch * 256.0F / 360.0F));
        packet.getBooleans().write(0, false);
        return packet;
    }

    @NotNull
    public static PacketContainer applyVelocity(int id, double x, double y, double z) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_VELOCITY);
        packet.getIntegers().write(0, id)
                .write(1, (int) (x * 8000.0))
                .write(2, (int) (y * 8000.0))
                .write(3, (int) (z * 8000.0));

        return packet;
    }

    @NotNull
    public static PacketContainer moveEntity(int id, double x, double y, double z, float yaw, float pitch) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.REL_ENTITY_MOVE_LOOK);
        packet.getIntegers().write(0, id);
        packet.getShorts()
                .write(0, (short) (x * 4096.0D))
                .write(1, (short) (y * 4096.0D))
                .write(2, (short) (z * 4096.0D));
        packet.getBytes()
                .write(0, (byte) (yaw * 256.0F / 360.0F))
                .write(1, (byte) (pitch * 256.0F / 360.0F));
        packet.getBooleans().write(0, false);
        return packet;
    }

    @NotNull
    public static PacketContainer position(int id, double x, double y, double z, float yaw, float pitch) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.POSITION);
        packet.getIntegers().write(0, id);
        packet.getDoubles()
                .write(0, x)
                .write(1, y)
                .write(2, z);
        packet.getFloat()
                .write(0, yaw)
                .write(1, pitch);
        return packet;
    }

    @NotNull
    public static PacketContainer entityLook(int id, float yaw, float pitch) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_LOOK);
        packet.getIntegers().write(0, id);
        packet.getBytes()
                .write(0, (byte) (yaw * 256.0F / 360.0F))
                .write(1, (byte) (pitch * 256.0F / 360.0F));
        packet.getBooleans().write(0, false);
        return packet;
    }

    @NotNull
    public static PacketContainer headRotation(int id, float headYaw) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
        packet.getIntegers().write(0, id);
        packet.getBytes().write(0, (byte) (headYaw * 256.0F / 360.0F));
        return packet;
    }

    @NotNull
    public static PacketContainer applyEffect(int id, int effectId, int amplifier, int duration, boolean particles) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_EFFECT);
        packet.getIntegers()
                .write(0, id)
                .write(1, effectId)
                .write(2, duration);
        packet.getBytes()
                .write(0, (byte) (amplifier & 255))
                .write(1, (byte) (particles ? 1 : 0));
        return packet;
    }

    @NotNull
    public static PacketContainer updateAttributes(int id, @NotNull List<WrappedAttribute> attributes) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.UPDATE_ATTRIBUTES);
        packet.getIntegers().write(0, id);
        packet.getAttributeCollectionModifier().write(0, attributes);
        return packet;
    }

    @NotNull
    public static PacketContainer walkingSpeed(int id, float speed) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ABILITIES);
        packet.getFloat().write(1, speed);
        return packet;
    }

    @NotNull
    public static PacketContainer changeGameState(int reason, float value) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.GAME_STATE_CHANGE);
        packet.getGameStateIDs().write(0, reason);
        packet.getFloat().write(0, value);
        return packet;
    }

    @NotNull
    public static PacketContainer createAndApplyMetaData(int id, int index, @NotNull Object value) {
        return createAndApplyMetaData(id, index, value, false);
    }

    @NotNull
    public static PacketContainer createAndApplyMetaData(int id, int index, @NotNull Object value, boolean optional) {
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(value.getClass(), optional);
        return createAndApplyMetaData(id, index, value, serializer);
    }

    @NotNull
    public static PacketContainer createAndApplyMetaData(int id, int index, @NotNull Object value,
                                                         @NotNull WrappedDataWatcher.Serializer serializer) {
        return WATCHER_APPLIER.createAndApplyMetaData(id, index, value, serializer);
    }

    @NotNull
    public static PacketContainer createAndApplyMetaData(int id, @NotNull Map<Integer, Object> data) {
        return WATCHER_APPLIER.createAndApplyMetaData(id, data);
    }

    @NotNull
    public static PacketContainer applyMetadata(int id, @NotNull WrappedDataWatcher watcher) {
        return WATCHER_APPLIER.applyMetadata(id, watcher);
    }

    private interface WatcherApplier {
        @NotNull
        PacketContainer createAndApplyMetaData(int id, int index, @NotNull Object value,
                                               @NotNull WrappedDataWatcher.Serializer serializer);

        @NotNull
        PacketContainer createAndApplyMetaData(int id, @NotNull Map<Integer, Object> data);

        @NotNull
        PacketContainer applyMetadata(int id, @NotNull WrappedDataWatcher watcher);
    }

    private static final class OlderWatcherApplier implements WatcherApplier {
        @Override
        public @NotNull PacketContainer createAndApplyMetaData(int id, int index, @NotNull Object value,
                                                               WrappedDataWatcher.@NotNull Serializer serializer) {
            WrappedDataWatcher watcher = new WrappedDataWatcher();
            watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(index, serializer), value);

            return applyMetadata(id, watcher);
        }

        @Override
        public @NotNull PacketContainer createAndApplyMetaData(int id, @NotNull Map<Integer, Object> data) {
            WrappedDataWatcher watcher = new WrappedDataWatcher();

            for (Map.Entry<Integer, Object> entry : data.entrySet()) {
                WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(entry.getValue().getClass());
                watcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(entry.getKey(), serializer), entry.getValue());
            }

            return applyMetadata(id, watcher);
        }

        @Override
        public @NotNull PacketContainer applyMetadata(int id, @NotNull WrappedDataWatcher watcher) {
            PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
            packet.getIntegers().write(0, id);
            packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());

            return packet;
        }
    }

    private static final class NewerWatcherApplier implements WatcherApplier {
        @Override
        public @NotNull PacketContainer createAndApplyMetaData(int id, int index, @NotNull Object value,
                                                               WrappedDataWatcher.@NotNull Serializer serializer) {
            return applyMetadata(id, List.of(new WrappedDataValue(index, serializer, value)));
        }

        @Override
        public @NotNull PacketContainer createAndApplyMetaData(int id, @NotNull Map<Integer, Object> data) {
            List<WrappedDataValue> values = new ArrayList<>(data.size());

            for (Map.Entry<Integer, Object> entry : data.entrySet()) {
                WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(entry.getValue().getClass());
                values.add(new WrappedDataValue(entry.getKey(), serializer, entry.getValue()));
            }

            return applyMetadata(id, values);
        }

        @Override
        public @NotNull PacketContainer applyMetadata(int id, @NotNull WrappedDataWatcher watcher) {
            List<WrappedDataValue> values = watcher.getWatchableObjects().stream()
                    .map(entry -> {
                        WrappedDataWatcher.WrappedDataWatcherObject object = entry.getWatcherObject();
                        return new WrappedDataValue(object.getIndex(), object.getSerializer(), entry.getValue());
                    }).toList();

            return applyMetadata(id, values);
        }

        @NotNull
        private static PacketContainer applyMetadata(int id, @NotNull List<WrappedDataValue> values) {
            PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
            packet.getIntegers().write(0, id);
            packet.getDataValueCollectionModifier().write(0, values);

            return packet;
        }
    }
}
