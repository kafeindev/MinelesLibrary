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

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.tr7zw.nbtapi.NBTItem;
import net.mineles.library.node.Node;
import net.mineles.library.util.text.PlaceholderParser;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public final class ItemComponent {
    @NotNull
    public static ItemComponent create(@NotNull String materialName, int amount) {
        return new ItemComponent(materialName, amount);
    }

    @NotNull
    public static ItemComponent create(@NotNull XMaterial material, int amount) {
        return new ItemComponent(material, amount);
    }

    @NotNull
    public static ItemComponent from(@Nullable ItemStack itemStack) {
        return new ItemComponent(itemStack == null ? null : itemStack.clone());
    }

    @NotNull
    public static ItemComponent from(@NotNull Node node) {
        return from(node, new HashMap<>());
    }

    @NotNull
    public static ItemComponent from(@NotNull Node node,
                                     @NotNull Map<String, String> placeholders) {
        return new ItemComponent(node, placeholders)
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

    public static final net.mineles.library.serializer.Serializer<ItemComponent, String> SERIALIZER = new Serializer();

    @Nullable
    private final ItemStack itemStack;

    @Nullable
    private final ItemMeta itemMeta;

    private NBTItem nbtItem;

    private ItemComponent(@NotNull Node node) {
        this(node, ImmutableMap.of());
    }

    private ItemComponent(@NotNull Node node, @NotNull Map<String, String> placeholders) {
        this(PlaceholderParser.applyPlaceholders(node.node("material").getString(), placeholders),
                node.node("amount").isEmpty() ? 1 : node.node("amount").getInt());
    }

    private ItemComponent(@Nullable ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack == null ? null : itemStack.getItemMeta();

        if (itemStack != null && itemStack.getAmount() > 0 && itemStack.getType() != Material.AIR) {
            this.nbtItem = new NBTItem(itemStack);
        }
    }

    private ItemComponent(@NotNull String materialName, int amount) {
        this(XMaterial.matchXMaterial(materialName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid material name: " + materialName)), amount);
    }

    private ItemComponent(@NotNull XMaterial material, int amount) {
        this.itemStack = material.parseItem();
        this.itemStack.setAmount(amount);
        this.itemMeta = itemStack.getItemMeta();
        this.nbtItem = new NBTItem(itemStack);
    }

    public @Nullable ItemStack getHandle() {
        return getHandle(true);
    }

    public @Nullable ItemStack getHandle(boolean merge) {
        if (merge) {
            merge();
        }
        return this.itemStack;
    }

    public boolean isSimilar(@NotNull ItemComponent itemComponent) {
        return this.itemStack != null && this.itemStack.isSimilar(itemComponent.getHandle());
    }

    @NotNull
    public String getMaterial() {
        return this.itemStack.getType().name();
    }

    public int getAmount() {
        return this.itemStack.getAmount();
    }

    @NotNull
    public ItemComponent setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    @NotNull
    public ItemComponent setAmount(@NotNull Node node) {
        Node amountNode = node.node("amount");
        return amountNode.isEmpty() ? this : setAmount(amountNode.getInt());
    }

    public @Nullable Component getName() {
        return this.itemMeta.displayName();
    }

    @NotNull
    public ItemComponent setName(@NotNull Component name) {
        this.itemMeta.displayName(name);
        return this;
    }

    @NotNull
    public ItemComponent setName(@NotNull String name) {
        itemMeta.setDisplayName(name);
        return this;
    }

    @NotNull
    public ItemComponent setName(@NotNull String name, @NotNull Map<String, String> placeholders) {
        return setName(PlaceholderParser.applyPlaceholders(name, placeholders));
    }

    @NotNull
    public ItemComponent setName(@NotNull Node node) {
        return setName(node, new HashMap<>());
    }

    @NotNull
    public ItemComponent setName(@NotNull Node node, @NotNull Map<String, String> placeholders) {
        Node nameNode = node.node("name");
        return nameNode.isEmpty() ? this : setName(nameNode.getString(), placeholders);
    }

    public @Nullable List<Component> getLore() {
        return this.itemMeta.lore();
    }

    @NotNull
    public ItemComponent setLore(@NotNull List<Component> lore) {
        this.itemMeta.lore(lore);
        return this;
    }

    @NotNull
    public ItemComponent setLoreString(@NotNull List<String> lore) {
        itemMeta.setLore(lore);
        return this;
    }

    @NotNull
    public ItemComponent setLore(@NotNull List<String> lore,
                                 @NotNull Map<String, String> placeholders) {
        return setLoreString(lore.stream()
                .map(s -> PlaceholderParser.applyPlaceholders(s, placeholders))
                .collect(Collectors.toList()));
    }

    @NotNull
    public ItemComponent setLore(@NotNull Node node) {
        return setLore(node, new HashMap<>());
    }

    @NotNull
    public ItemComponent setLore(@NotNull Node node,
                                 @NotNull Map<String, String> placeholders) {
        Node loreNode = node.node("lore");
        return loreNode.isEmpty() ? this : setLore(loreNode.getList(TypeToken.of(String.class)), placeholders);
    }

    @NotNull
    public ItemComponent addLore(@NotNull Component lore) {
        List<Component> loreList = hasLore() ? getLore() : new ArrayList<>();
        loreList.add(lore);

        return setLore(loreList);
    }

    @NotNull
    public ItemComponent removeLore(@NotNull Component lore) {
        if (hasLore()) {
            List<Component> loreList = getLore();
            loreList.remove(lore);

            return setLore(loreList);
        }

        return this;
    }

    @NotNull
    public ItemComponent clearLore() {
        return setLore(new ArrayList<>());
    }

    public boolean containsLore(@NotNull Component lore) {
        return hasLore() && getLore().contains(lore);
    }

    public boolean hasLore() {
        return this.itemMeta.hasLore();
    }

    public @Nullable Map<String, Integer> getEnchantments() {
        return this.itemMeta.getEnchants().entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().getKey().getKey(), Map.Entry::getValue));
    }

    @NotNull
    public ItemComponent setEnchantments(@NotNull Map<String, Integer> enchantments) {
        enchantments.entrySet().forEach(entry -> XEnchantment.matchXEnchantment(entry.getKey()).get().getEnchant());
        return this;
    }

    @NotNull
    public ItemComponent setEnchantments(@NotNull Node node) {
        Node enchantmentsNode = node.node("enchantments");
        if (enchantmentsNode.isEmpty()) return this;

        Map<String, Integer> enchantments = enchantmentsNode.getList(TypeToken.of(String.class)).stream()
                .map(enchantment -> enchantment.split(": "))
                .collect(Collectors.toMap(enchantment -> enchantment[0], enchantment -> Integer.parseInt(enchantment[1])));
        return setEnchantments(enchantments);
    }

    @NotNull
    public ItemComponent addEnchantment(@NotNull String enchantment, int level) {
        this.itemMeta.addEnchant(XEnchantment.matchXEnchantment(enchantment).get().getEnchant(), level, true);
        return this;
    }

    @NotNull
    public ItemComponent removeEnchantment(@NotNull String enchantment) {
        this.itemMeta.removeEnchant(XEnchantment.matchXEnchantment(enchantment).get().getEnchant());
        return this;
    }

    @NotNull
    public ItemComponent clearEnchantments() {
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

    @NotNull
    public ItemComponent glow(boolean glow) {
        if (!hasEnchantments()) {
            addEnchantment("DURABILITY", 1);
        }
        return addFlag(ItemFlag.HIDE_ENCHANTS.name());
    }

    @NotNull
    public ItemComponent glow(@NotNull Node node) {
        Node glowNode = node.node("glow");
        return glowNode.isEmpty() ? this : glow(glowNode.getBoolean());
    }

    public boolean isGlowing() {
        return this.itemMeta.hasItemFlag(ItemFlag.HIDE_ENCHANTS);
    }

    @NotNull
    public ItemComponent setSkullOwner(@NotNull String owner) {
        if (this.itemStack.getType() == Material.PLAYER_HEAD) {
            SkullMeta skullMeta = (SkullMeta) this.itemMeta;
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(owner));
        }
        return this;
    }

    @NotNull
    public ItemComponent setSkullOwner(@NotNull Node node) {
        Node skullOwnerNode = node.node("skull-owner");
        return skullOwnerNode.isEmpty() ? this : setSkullOwner(skullOwnerNode.getString());
    }

    @NotNull
    public ItemComponent setSkullOwner(@NotNull Node node, @NotNull Map<String, String> placeholders) {
        Node skullOwnerNode = node.node("skull-owner");
        return skullOwnerNode.isEmpty() ? this : setSkullOwner(PlaceholderParser.applyPlaceholders(skullOwnerNode.getString(), placeholders));
    }

    @NotNull
    public ItemComponent setHeadTexture(@NotNull String texture) {
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

    @NotNull
    public ItemComponent setHeadTexture(@NotNull Node node) {
        Node headTextureNode = node.node("head-texture");
        return headTextureNode.isEmpty() ? this : setHeadTexture(headTextureNode.getString());
    }

    @NotNull
    public ItemComponent setHeadTexture(@NotNull Node node,
                                        @NotNull Map<String, String> placeholders) {
        Node headTextureNode = node.node("head-texture");
        return headTextureNode.isEmpty() ? this : setHeadTexture(PlaceholderParser.applyPlaceholders(headTextureNode.getString(), placeholders));
    }

    @NotNull
    public Set<String> getFlags() {
        return itemMeta.getItemFlags().stream()
                .map(ItemFlag::name)
                .collect(Collectors.toSet());
    }

    @NotNull
    public ItemComponent setFlags(@NotNull Set<String> flags) {
        flags.forEach(flag -> this.itemMeta.addItemFlags(ItemFlag.valueOf(flag)));
        return this;
    }

    @NotNull
    public ItemComponent setFlags(@NotNull Node node) {
        Node flagsNode = node.node("flags");
        if (flagsNode.isEmpty()) return this;

        Set<String> flags = flagsNode.getList(TypeToken.of(String.class)).stream()
                .map(String::toUpperCase)
                .collect(Collectors.toSet());
        return setFlags(flags);
    }

    @NotNull
    public ItemComponent addFlag(@NotNull String flag) {
        this.itemMeta.addItemFlags(ItemFlag.valueOf(flag));
        return this;
    }

    @NotNull
    public ItemComponent removeFlag(@NotNull String flag) {
        this.itemMeta.removeItemFlags(ItemFlag.valueOf(flag));
        return this;
    }

    @NotNull
    public ItemComponent clearFlags() {
        this.itemMeta.getItemFlags().clear();
        return this;
    }

    public boolean containsFlag(@NotNull String flag) {
        return this.itemMeta.hasItemFlag(ItemFlag.valueOf(flag));
    }

    @NotNull
    public ItemComponent setUnbreakable(boolean unbreakable) {
        this.itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    @NotNull
    public ItemComponent setUnbreakable(@NotNull Node node) {
        Node unbreakableNode = node.node("unbreakable");
        return unbreakableNode.isEmpty() ? this : setUnbreakable(unbreakableNode.getBoolean());
    }

    public boolean isUnbreakable() {
        return this.itemMeta.isUnbreakable();
    }

    @NotNull
    public ItemComponent setCustomModelData(int customModelData) {
        this.itemMeta.setCustomModelData(customModelData);
        return this;
    }

    @NotNull
    public ItemComponent setCustomModelData(@NotNull Node node) {
        Node customModelDataNode = node.node("model-data");
        return customModelDataNode.isEmpty() ? this : setCustomModelData(customModelDataNode.getInt());
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
        return this.nbtItem.hasTag(nbt);
    }

    @NotNull
    public ItemComponent setNbt(@NotNull String nbt, @NotNull String value) {
        this.nbtItem.setString(nbt, value);
        return this;
    }

    @NotNull
    public ItemComponent setNbt(@NotNull String nbt, int value) {
        this.nbtItem.setInteger(nbt, value);
        return this;
    }

    @NotNull
    public ItemComponent setNbt(@NotNull String nbt, double value) {
        this.nbtItem.setDouble(nbt, value);
        return this;
    }

    @NotNull
    public ItemComponent setNbt(@NotNull String nbt, boolean value) {
        this.nbtItem.setBoolean(nbt, value);
        return this;
    }

    @NotNull
    public ItemComponent setNbt(@NotNull Node node) {
        Node nbtNode = node.node("nbt");
        if (nbtNode.isEmpty()) return this;

        nbtNode.childMap().forEach((key, value) -> {
            if (value.get() instanceof String) {
                setNbt(key.toString(), value.getString());
            } else if (value.get() instanceof Integer) {
                setNbt(key.toString(), value.getInt());
            } else if (value.get() instanceof Double) {
                setNbt(key.toString(), value.getDouble());
            } else if (value.get() instanceof Boolean) {
                setNbt(key.toString(), value.getBoolean());
            }
        });
        return this;
    }

    @NotNull
    public ItemComponent merge() {
        if (this.itemStack == null) {
            return this;
        }

        this.itemStack.setItemMeta(this.itemMeta);
        if (this.nbtItem != null) {
            this.nbtItem.mergeNBT(this.itemStack);
        }
        return this;
    }

    @NotNull
    public ItemComponent clone() {
        merge();
        return ItemComponent.from(this.itemStack.clone());
    }

    private static final class Serializer implements net.mineles.library.serializer.Serializer<ItemComponent, String> {
        @Override
        public ItemComponent deserialize(@NotNull String s) {
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(s));
                BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

                ItemStack item = (ItemStack) dataInput.readObject();
                dataInput.close();

                return ItemComponent.from(item);
            } catch (IOException | ClassNotFoundException e) {
                throw new IllegalArgumentException("Unable to decode class type.", e);
            }
        }

        @Override
        public String serialize(@NotNull ItemComponent itemComponent) {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

                dataOutput.writeObject(itemComponent.getHandle(false));

                dataOutput.close();
                return Base64Coder.encodeLines(outputStream.toByteArray());
            } catch (Exception e) {
                throw new IllegalStateException("Unable to save item stacks.", e);
            }
        }
    }
}
