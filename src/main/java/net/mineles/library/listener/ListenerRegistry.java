/*
 * MIT License
 *
 * Copyright (c) 2022-2023 MinelesNetwork
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

package net.mineles.library.listener;

import net.mineles.library.plugin.BukkitPlugin;
import net.mineles.library.plugin.BungeePlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public final class ListenerRegistry {
    private ListenerRegistry() {}

    public static void register(@NotNull BukkitPlugin plugin,
                                @NotNull Set<Class<?>> listenerClasses) {
        BukkitRegistry.register(plugin, listenerClasses);
    }

    public static void register(@NotNull BungeePlugin plugin,
                                @NotNull Set<Class<?>> listenerClasses) {
        BungeeRegistry.register(plugin, listenerClasses);
    }

    private static final class BukkitRegistry {
        private BukkitRegistry() {}

        public static void register(@NotNull BukkitPlugin plugin,
                                    @NotNull Set<Class<?>> listenerClasses) {
            org.bukkit.plugin.Plugin handle = plugin.getPlugin();
            org.bukkit.plugin.PluginManager pluginManager = handle.getServer().getPluginManager();

            for (Class<?> listenerClass : listenerClasses) {
                org.bukkit.event.Listener listener = cast(plugin, listenerClass);

                pluginManager.registerEvents(listener, handle);
            }
        }

        private static org.bukkit.event.Listener cast(@NotNull BukkitPlugin plugin,
                                     @NotNull Class<?> clazz) {
            try {
                if (clazz.getConstructors()[0].getParameterCount() == 0) {
                    return (org.bukkit.event.Listener) clazz
                            .getConstructor()
                            .newInstance();
                } else {
                    return (org.bukkit.event.Listener) clazz
                            .getConstructor(BukkitPlugin.class)
                            .newInstance(plugin);
                }
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Cannot cast class to listener: " + clazz.getName(), e);
            }
        }
    }

    private static final class BungeeRegistry {
        private BungeeRegistry() {}

        public static void register(@NotNull BungeePlugin plugin,
                                    @NotNull Set<Class<?>> listenerClasses) {
            net.md_5.bungee.api.plugin.Plugin handle = plugin.getPlugin();
            net.md_5.bungee.api.plugin.PluginManager pluginManager = handle.getProxy().getPluginManager();

            for (Class<?> listenerClass : listenerClasses) {
                net.md_5.bungee.api.plugin.Listener listener = cast(plugin, listenerClass);

                pluginManager.registerListener(handle, listener);
            }
        }

        private static net.md_5.bungee.api.plugin.Listener cast(@NotNull BungeePlugin plugin,
                                                                @NotNull Class<?> clazz) {
            try {
                if (clazz.getConstructors()[0].getParameterCount() == 0) {
                    return (net.md_5.bungee.api.plugin.Listener) clazz
                            .getConstructor()
                            .newInstance();
                } else {
                    return (net.md_5.bungee.api.plugin.Listener) clazz
                            .getConstructor(BungeePlugin.class)
                            .newInstance(plugin);
                }
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Cannot cast class to listener: " + clazz.getName(), e);
            }
        }
    }
}
