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

package net.mineles.library.components;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.tr7zw.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.mineles.library.compatibility.Compatibility;
import net.mineles.library.compatibility.ItemsAdderCompatibility;
import net.mineles.library.utils.text.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static net.mineles.library.utils.text.ComponentSerializer.deserialize;
import static net.mineles.library.utils.text.PlaceholderParser.applyPlaceholders;

public final class ItemComponent {
    public static final Serializer SERIALIZER = new Serializer();

    private final @Nullable ItemStack itemStack;
    private final @Nullable ItemMeta itemMeta;

    private @Nullable NBTItem nbtItem;

    private ItemComponent(@NotNull ConfigurationNode node) {
        this(node, Map.of());
    }

    private ItemComponent(@NotNull ConfigurationNode node,
                          @NotNull Map<String, String> placeholders) {
        this(applyPlaceholders(node.node("material").getString(), placeholders),
                node.node("amount").empty() ? 1 : node.node("amount").getInt());
    }

    private ItemComponent(@Nullable ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack == null ? null : itemStack.getItemMeta();

        if (itemStack != null && itemStack.getAmount() > 0 && itemStack.getType() != Material.AIR) {
            this.nbtItem = new NBTItem(itemStack);
        }
    }

    private ItemComponent(@NotNull String materialName,
                          int amount) {
        this(XMaterial.matchXMaterial(materialName).orElse(XMaterial.STONE), amount);
    }

    private ItemComponent(@NotNull XMaterial material,
                          int amount) {
        this.itemStack = material.parseItem();
        this.itemStack.setAmount(amount);
        this.itemMeta = this.itemStack.getItemMeta();
        this.nbtItem = new NBTItem(this.itemStack);
    }

    @NotNull
    public static ItemComponent create(@NotNull String materialName,
                                       int amount) {
        return new ItemComponent(materialName, amount);
    }

    @NotNull
    public static ItemComponent create(@NotNull XMaterial material,
                                       int amount) {
        return new ItemComponent(material, amount);
    }

    @NotNull
    public static ItemComponent from(@Nullable ItemStack itemStack) {
        return new ItemComponent(itemStack == null ? null : itemStack.clone());
    }

    @NotNull
    public static ItemComponent from(@NotNull ConfigurationNode node) {
        return from(node, new HashMap<>());
    }

    @NotNull
    public static ItemComponent from(@NotNull ConfigurationNode node,
                                     @NotNull Map<String, String> placeholders) {
        return new ItemComponent(node, placeholders)
                .setCustomModel(node)
                .setCustomModelData(node)
                .setName(node, placeholders)
                .setLore(node, placeholders)
                .setEnchantments(node)
                .setFlags(node)
                .glow(node)
                .setUnbreakable(node)
                .setSkullOwner(node, placeholders)
                .setHeadTexture(node)
                .setNbt(node)
                .merge();
    }

    @NotNull
    public static ItemComponent from(@NotNull ConfigurationNode node,
                                     @NotNull Map<String, String> placeholders,
                                     @NotNull Player player) {
        return new ItemComponent(node, placeholders)
                .setCustomModel(node)
                .setCustomModelData(node)
                .setName(node, placeholders, player)
                .setLore(node, placeholders, player)
                .setEnchantments(node)
                .setFlags(node)
                .glow(node)
                .setUnbreakable(node)
                .setSkullOwner(node, placeholders)
                .setHeadTexture(node)
                .setNbt(node)
                .merge();
    }

    public @Nullable ItemStack getHandle() {
        return getHandle(true);
    }

    public @Nullable ItemStack getHandle(boolean merge) {
        if (merge) {
            merge();
        }
        return this.itemStack == null ? null : this.itemStack.clone();
    }

    public boolean isSimilar(@NotNull ItemComponent itemComponent) {
        return this.itemStack != null && this.itemStack.isSimilar(itemComponent.getHandle());
    }

    public @NotNull String getMaterial() {
        return this.itemStack.getType().name();
    }

    public int getAmount() {
        return this.itemStack.getAmount();
    }

    public @NotNull ItemComponent setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public @NotNull ItemComponent setAmount(@NotNull ConfigurationNode node) {
        ConfigurationNode amountNode = node.node("amount");
        return amountNode.empty() ? this : setAmount(amountNode.getInt());
    }

    public @Nullable Component getName() {
        return this.itemMeta.displayName();
    }

    public @NotNull ItemComponent setName(@NotNull Component name) {
        this.itemMeta.displayName(name.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        return this;
    }

    public @NotNull ItemComponent setName(@NotNull String name) {
        return setName(deserialize(name));
    }

    public @NotNull ItemComponent setName(@NotNull String name,
                                          @NotNull Map<String, String> placeholders) {
        return setName(applyPlaceholders(name, placeholders));
    }

    public @NotNull ItemComponent setName(@NotNull ConfigurationNode node) {
        return setName(node, new HashMap<>());
    }

    public @NotNull ItemComponent setName(@NotNull ConfigurationNode node,
                                          @NotNull Map<String, String> placeholders) {
        ConfigurationNode nameNode = node.node("name");
        return nameNode.empty() ? this : setName(nameNode.getString(), placeholders);
    }

    public @NotNull ItemComponent setName(@NotNull ConfigurationNode node,
                                          @NotNull Map<String, String> placeholders,
                                          @NotNull Player player) {
        ConfigurationNode nameNode = node.node("name");
        return nameNode.empty() ? this : setName(deserialize(player, nameNode.getString(), placeholders));
    }

    public @Nullable List<Component> getLore() {
        return this.itemMeta.lore();
    }

    public @Nullable List<String> getLoreString() {
        return this.itemMeta.getLore();
    }

    public @NotNull ItemComponent setLore(@NotNull List<Component> lore) {
        this.itemMeta.lore(lore.stream()
                .map(c -> c.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                .collect(Collectors.toList()));
        return this;
    }

    public @NotNull ItemComponent setLore(@NotNull Component... lore) {
        return setLore(Arrays.asList(lore));
    }

    public @NotNull ItemComponent setLoreString(@NotNull List<String> lore) {
        List<Component> components = lore.stream()
                .map(ComponentSerializer::deserialize)
                .collect(Collectors.toList());
        return setLore(components);
    }

    public @NotNull ItemComponent setLoreString(@NotNull List<String> lore,
                                                @NotNull Map<String, String> placeholders) {
        return setLoreString(lore.stream()
                .map(s -> applyPlaceholders(s, placeholders))
                .collect(Collectors.toList()));
    }

    public @NotNull ItemComponent setLoreString(@NotNull String... lore) {
        return setLoreString(Arrays.asList(lore));
    }

    public @NotNull ItemComponent setLore(@NotNull ConfigurationNode node) {
        return setLore(node, new HashMap<>());
    }

    public @NotNull ItemComponent setLore(@NotNull ConfigurationNode node,
                                          @NotNull Map<String, String> placeholders) {
        ConfigurationNode loreNode = node.node("lore");
        try {
            return loreNode.empty() ? this : setLoreString(loreNode.getList(String.class), placeholders);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    public @NotNull ItemComponent setLore(@NotNull ConfigurationNode node,
                                          @NotNull Map<String, String> placeholders,
                                          @NotNull Player player) {
        ConfigurationNode loreNode = node.node("lore");
        if (loreNode.empty()) {
            return this;
        }

        List<Component> loreList = new ArrayList<>();
        try {
            loreNode.getList(String.class).forEach(s -> loreList.add(deserialize(player, s, placeholders)));
            return setLore(loreList);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    public @NotNull ItemComponent addLore(@NotNull List<Component> lore) {
        List<Component> loreList = hasLore() ? getLore() : new ArrayList<>();
        loreList.addAll(lore);

        return setLore(loreList);
    }

    public @NotNull ItemComponent addLore(@NotNull Component lore) {
        List<Component> loreList = hasLore() ? getLore() : new ArrayList<>();
        loreList.add(lore);

        return setLore(loreList);
    }

    public @NotNull ItemComponent addLores(@NotNull List<String> lore) {
        List<String> loreList = hasLore() ? getLoreString() : new ArrayList<>();
        loreList.addAll(lore);

        return setLoreString(loreList);
    }

    public @NotNull ItemComponent addLores(@NotNull List<String> lore,
                                           @NotNull Map<String, String> placeholders) {
        List<String> loreList = hasLore() ? getLoreString() : new ArrayList<>();
        loreList.addAll(lore.stream()
                .map(s -> applyPlaceholders(s, placeholders))
                .collect(Collectors.toList()));

        return setLoreString(loreList);
    }

    public @NotNull ItemComponent addLore(@NotNull String lore) {
        List<String> loreList = hasLore() ? getLoreString() : new ArrayList<>();
        loreList.add(lore);

        return setLoreString(loreList);
    }

    public @NotNull ItemComponent removeLore(@NotNull Component lore) {
        if (hasLore()) {
            List<Component> loreList = getLore();
            loreList.remove(lore);

            return setLore(loreList);
        }

        return this;
    }

    public @NotNull ItemComponent removeLore(@NotNull String lore) {
        return removeLore(deserialize(lore));
    }

    public @NotNull ItemComponent clearLore() {
        return setLore(new ArrayList<>());
    }

    public boolean containsLore(@NotNull Component lore) {
        return hasLore() && getLore().contains(lore);
    }

    public boolean containsLore(@NotNull String lore) {
        return containsLore(deserialize(lore));
    }

    public boolean hasLore() {
        return this.itemMeta.hasLore();
    }

    public @Nullable Map<String, Integer> getEnchantments() {
        return this.itemMeta.getEnchants().entrySet().stream()
                .collect(HashMap::new, (map, entry) -> map.put(entry.getKey().getKey().getKey(), entry.getValue()), HashMap::putAll);
    }

    public @NotNull ItemComponent setEnchantments(@NotNull Map<String, Integer> enchantments) {
        enchantments.entrySet().forEach(entry -> XEnchantment.matchXEnchantment(entry.getKey()).get().getEnchant());
        return this;
    }

    public @NotNull ItemComponent setEnchantments(@NotNull ConfigurationNode node) {
        ConfigurationNode enchantmentsNode = node.node("enchantments");
        if (enchantmentsNode.empty()) return this;

        try {
            Map<String, Integer> enchantments = enchantmentsNode.getList(String.class).stream()
                    .map(enchantment -> enchantment.split(": "))
                    .collect(Collectors.toMap(enchantment -> enchantment[0], enchantment -> Integer.parseInt(enchantment[1])));
            return setEnchantments(enchantments);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    public @NotNull ItemComponent addEnchantment(@NotNull String enchantment,
                                                 int level) {
        this.itemMeta.addEnchant(XEnchantment.matchXEnchantment(enchantment).get().getEnchant(), level, true);
        return this;
    }

    public @NotNull ItemComponent removeEnchantment(@NotNull String enchantment) {
        this.itemMeta.removeEnchant(XEnchantment.matchXEnchantment(enchantment).get().getEnchant());
        return this;
    }

    public @NotNull ItemComponent clearEnchantments() {
        this.itemMeta.getEnchants().clear();
        return this;
    }

    public int getEnchantmentLevel(@NotNull String enchantment) {
        return this.itemMeta.getEnchantLevel(XEnchantment.matchXEnchantment(enchantment).get().getEnchant());
    }

    public boolean containsEnchantment(@NotNull String enchantment) {
        return this.itemMeta.hasEnchant(XEnchantment.matchXEnchantment(enchantment).get().getEnchant());
    }

    public boolean hasEnchantments() {
        return !this.itemMeta.getEnchants().isEmpty();
    }

    public @NotNull ItemComponent glow(boolean glow) {
        if (!hasEnchantments()) {
            addEnchantment("DURABILITY", 1);
        }
        return addFlag(ItemFlag.HIDE_ENCHANTS.name());
    }

    public @NotNull ItemComponent glow(@NotNull ConfigurationNode node) {
        ConfigurationNode glowNode = node.node("glow");
        return glowNode.empty() ? this : glow(glowNode.getBoolean());
    }

    public boolean isGlowing() {
        return this.itemMeta.hasItemFlag(ItemFlag.HIDE_ENCHANTS);
    }

    public @NotNull ItemComponent setSkullOwner(@NotNull String owner) {
        if (this.itemStack.getType() == Material.PLAYER_HEAD) {
            SkullMeta skullMeta = (SkullMeta) this.itemMeta;
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(owner));
        }
        return this;
    }

    public @NotNull ItemComponent setSkullOwner(@NotNull ConfigurationNode node) {
        ConfigurationNode skullOwnerNode = node.node("skull-owner");
        return skullOwnerNode.empty() ? this : setSkullOwner(skullOwnerNode.getString());
    }

    public @NotNull ItemComponent setSkullOwner(@NotNull ConfigurationNode node,
                                                @NotNull Map<String, String> placeholders) {
        ConfigurationNode skullOwnerNode = node.node("skull-owner");
        return skullOwnerNode.empty() ? this : setSkullOwner(applyPlaceholders(skullOwnerNode.getString(), placeholders));
    }

    public @NotNull ItemComponent setHeadTexture(@NotNull String texture) {
        if (this.itemStack.getType() != Material.PLAYER_HEAD) {
            return this;
        }

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture));
        try {
            Field profileField = this.itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(this.itemMeta, profile);
            return this;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set head texture", e);
        }
    }

    public @NotNull ItemComponent setHeadTexture(@NotNull ConfigurationNode node) {
        ConfigurationNode headTextureNode = node.node("head-texture");
        return headTextureNode.empty() ? this : setHeadTexture(headTextureNode.getString());
    }

    public @NotNull ItemComponent setHeadTexture(@NotNull ConfigurationNode node,
                                                 @NotNull Map<String, String> placeholders) {
        ConfigurationNode headTextureNode = node.node("head-texture");
        return headTextureNode.empty() ? this : setHeadTexture(applyPlaceholders(headTextureNode.getString(), placeholders));
    }

    public @NotNull Set<String> getFlags() {
        return itemMeta.getItemFlags().stream()
                .map(ItemFlag::name)
                .collect(Collectors.toSet());
    }

    public @NotNull ItemComponent setFlags(@NotNull Set<String> flags) {
        flags.forEach(flag -> this.itemMeta.addItemFlags(ItemFlag.valueOf(flag)));
        return this;
    }

    public @NotNull ItemComponent setFlags(@NotNull ConfigurationNode node) {
        ConfigurationNode flagsNode = node.node("flags");
        if (flagsNode.empty()) return this;

        try {
            Set<String> flags = flagsNode.getList(String.class).stream()
                    .map(String::toUpperCase)
                    .collect(Collectors.toSet());
            return setFlags(flags);
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }
    }

    public @NotNull ItemComponent addFlag(@NotNull String flag) {
        this.itemMeta.addItemFlags(ItemFlag.valueOf(flag));
        return this;
    }

    public @NotNull ItemComponent removeFlag(@NotNull String flag) {
        this.itemMeta.removeItemFlags(ItemFlag.valueOf(flag));
        return this;
    }

    public @NotNull ItemComponent clearFlags() {
        this.itemMeta.getItemFlags().clear();
        return this;
    }

    public boolean containsFlag(@NotNull String flag) {
        return this.itemMeta.hasItemFlag(ItemFlag.valueOf(flag));
    }

    public @NotNull ItemComponent setUnbreakable(boolean unbreakable) {
        this.itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    public @NotNull ItemComponent setUnbreakable(@NotNull ConfigurationNode node) {
        ConfigurationNode unbreakableNode = node.node("unbreakable");
        return unbreakableNode.empty() ? this : setUnbreakable(unbreakableNode.getBoolean());
    }

    public boolean isUnbreakable() {
        return this.itemMeta.isUnbreakable();
    }

    public @NotNull ItemComponent setCustomModel(@NotNull String model) {
        if (!Compatibility.ITEMS_ADDER.isCompatible()) {
            return this;
        }

        int modelData = ItemsAdderCompatibility.getItemModel(model);
        return setCustomModelData(modelData);
    }

    public @NotNull ItemComponent setCustomModel(@NotNull ConfigurationNode node) {
        ConfigurationNode customModelNode = node.node("custom-model");
        return customModelNode.empty() ? this : setCustomModel(customModelNode.getString());
    }

    public @NotNull ItemComponent setCustomModelData(int customModelData) {
        this.itemMeta.setCustomModelData(customModelData);
        return this;
    }

    public @NotNull ItemComponent setCustomModelData(@NotNull ConfigurationNode node) {
        ConfigurationNode customModelDataNode = node.node("model-data");
        return customModelDataNode.empty() ? this : setCustomModelData(customModelDataNode.getInt());
    }

    public int getCustomModelData() {
        return this.itemMeta.getCustomModelData();
    }

    public @Nullable String getNbt(@NotNull String nbt) {
        return this.nbtItem.getString(nbt);
    }

    public int getNbtInt(@NotNull String nbt) {
        return this.nbtItem.getInteger(nbt);
    }

    public boolean hasNbt(@NotNull String nbt) {
        return this.nbtItem != null && this.nbtItem.hasTag(nbt);
    }

    public @NotNull ItemComponent setNbt(@NotNull String nbt, @NotNull String value) {
        this.nbtItem.setString(nbt, value);
        return this;
    }

    public @NotNull ItemComponent setNbt(@NotNull String nbt, int value) {
        this.nbtItem.setInteger(nbt, value);
        return this;
    }

    public @NotNull ItemComponent setNbt(@NotNull String nbt, double value) {
        this.nbtItem.setDouble(nbt, value);
        return this;
    }

    public @NotNull ItemComponent setNbt(@NotNull String nbt, boolean value) {
        this.nbtItem.setBoolean(nbt, value);
        return this;
    }

    public @NotNull ItemComponent setNbt(@NotNull ConfigurationNode node) {
        ConfigurationNode nbtNode = node.node("nbt");
        if (nbtNode.empty()) return this;

        nbtNode.childrenMap().forEach((key, value) -> {
            if (value.raw() instanceof String) {
                setNbt(key.toString(), value.getString());
            } else if (value.raw() instanceof Integer) {
                setNbt(key.toString(), value.getInt());
            } else if (value.raw() instanceof Double) {
                setNbt(key.toString(), value.getDouble());
            } else if (value.raw() instanceof Boolean) {
                setNbt(key.toString(), value.getBoolean());
            }
        });
        return this;
    }

    public @NotNull ItemComponent merge() {
        if (this.itemStack == null) {
            return this;
        }

        this.itemStack.setItemMeta(this.itemMeta);
        if (this.nbtItem != null) {
            this.nbtItem.mergeNBT(this.itemStack);
        }
        return this;
    }

    public @NotNull ItemComponent clone() {
        merge();
        return ItemComponent.from(this.itemStack.clone());
    }

    public static final class Serializer implements net.mineles.library.serializer.Serializer<ItemComponent, String> {
        private Serializer() {
        }

        @Override
        public ItemComponent deserialize(@NotNull String s) {
            return ItemComponent.from(deserializeItemStack(s));
        }

        public ItemComponent[] deserializeArray(@NotNull String s) {
            return Arrays.stream(deserializeItemStackArray(s))
                    .map(ItemComponent::from)
                    .toArray(ItemComponent[]::new);
        }

        public ItemStack deserializeItemStack(@NotNull String s) {
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(s));
                BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

                ItemStack item = (ItemStack) dataInput.readObject();
                dataInput.close();

                return item;
            } catch (IOException | ClassNotFoundException e) {
                throw new IllegalArgumentException("Unable to decode class type.", e);
            }
        }

        public ItemStack[] deserializeItemStackArray(@NotNull String s) {
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(s));
                BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

                ItemStack[] items = (ItemStack[]) dataInput.readObject();
                dataInput.close();

                return items;
            } catch (IOException | ClassNotFoundException e) {
                throw new IllegalArgumentException("Unable to decode class type.", e);
            }
        }

        @Override
        public String serialize(@NotNull ItemComponent itemComponent) {
            return serializeItemStack(itemComponent.getHandle(false));
        }

        public String serializeArray(@NotNull ItemComponent[] itemComponents) {
            return serializeItemStackArray(Arrays.stream(itemComponents)
                    .map(itemComponent -> itemComponent.getHandle(false))
                    .toArray(ItemStack[]::new));
        }

        public String serializeItemStack(@NotNull ItemStack itemStack) {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

                dataOutput.writeObject(itemStack);

                dataOutput.close();
                return Base64Coder.encodeLines(outputStream.toByteArray());
            } catch (Exception e) {
                throw new IllegalStateException("Unable to save item stacks.", e);
            }
        }

        public String serializeItemStackArray(@NotNull ItemStack[] itemStacks) {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

                dataOutput.writeObject(itemStacks);

                dataOutput.close();
                return Base64Coder.encodeLines(outputStream.toByteArray());
            } catch (Exception e) {
                throw new IllegalStateException("Unable to save item stacks.", e);
            }
        }

    }
}
