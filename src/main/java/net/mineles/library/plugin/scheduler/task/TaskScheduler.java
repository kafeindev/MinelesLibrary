/*
 * MIT License
 *
 * Copyright (c) 2022 FarmerPlus
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

package net.mineles.library.plugin.scheduler.task;

import net.mineles.library.plugin.BukkitPlugin;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public interface TaskScheduler {
    default @NotNull ScheduledTask schedule(@NotNull BukkitPlugin plugin,
                                            @NotNull Runnable task,
                                            @NotNull Duration delay) {
        return schedule(plugin, task, delay, false);
    }

    default @NotNull ScheduledTask schedule(@NotNull BukkitPlugin plugin,
                                            @NotNull Runnable task,
                                            @NotNull Duration delay,
                                            boolean async) {
        return schedule(plugin, task, delay.toMillis(), async);
    }

    default @NotNull ScheduledTask schedule(@NotNull BukkitPlugin plugin,
                                            @NotNull Runnable task,
                                            @NotNull Duration delay,
                                            boolean async, boolean bukkit) {
        return schedule(plugin, task, delay.toMillis(), async, bukkit);
    }

    default @NotNull ScheduledTask schedule(@NotNull BukkitPlugin plugin,
                                            @NotNull Runnable task,
                                            long delay) {
        return schedule(plugin, task, delay, false);
    }

    default @NotNull ScheduledTask schedule(@NotNull BukkitPlugin plugin,
                                            @NotNull Runnable task,
                                            long delay, boolean async) {
        return schedule(plugin, task, delay, async, false);
    }

    @NotNull ScheduledTask schedule(@NotNull BukkitPlugin plugin,
                                    @NotNull Runnable task,
                                    long delay,
                                    boolean async, boolean bukkit);

    default @NotNull ScheduledTask scheduleRepeating(@NotNull BukkitPlugin plugin,
                                                     @NotNull Runnable task,
                                                     @NotNull Duration period) {
        return scheduleRepeating(plugin, task, period, false);
    }

    default @NotNull ScheduledTask scheduleRepeating(@NotNull BukkitPlugin plugin,
                                                     @NotNull Runnable task,
                                                     @NotNull Duration period,
                                                     boolean async) {
        return scheduleRepeating(plugin, task, Duration.ZERO, period, async);
    }

    default @NotNull ScheduledTask scheduleRepeating(@NotNull BukkitPlugin plugin,
                                                     @NotNull Runnable task,
                                                     @NotNull Duration period,
                                                     boolean async, boolean bukkit) {
        return scheduleRepeating(plugin, task, Duration.ZERO, period, async, bukkit);
    }

    default @NotNull ScheduledTask scheduleRepeating(@NotNull BukkitPlugin plugin,
                                                     @NotNull Runnable task,
                                                     @NotNull Duration delay,
                                                     @NotNull Duration period) {
        return scheduleRepeating(plugin, task, delay, period, false);
    }

    default @NotNull ScheduledTask scheduleRepeating(@NotNull BukkitPlugin plugin,
                                                     @NotNull Runnable task,
                                                     @NotNull Duration delay,
                                                     @NotNull Duration period,
                                                     boolean async) {
        return scheduleRepeating(plugin, task, delay.toMillis(), period.toMillis(), async);
    }

    default @NotNull ScheduledTask scheduleRepeating(@NotNull BukkitPlugin plugin,
                                                     @NotNull Runnable task,
                                                     @NotNull Duration delay,
                                                     @NotNull Duration period,
                                                     boolean async, boolean bukkit) {
        return scheduleRepeating(plugin, task, delay.toMillis(), period.toMillis(), async, bukkit);
    }

    default @NotNull ScheduledTask scheduleRepeating(@NotNull BukkitPlugin plugin,
                                                     @NotNull Runnable task,
                                                     long period) {
        return scheduleRepeating(plugin, task, period, false);
    }

    default @NotNull ScheduledTask scheduleRepeating(@NotNull BukkitPlugin plugin,
                                                     @NotNull Runnable task,
                                                     long period,
                                                     boolean async) {
        return scheduleRepeating(plugin, task, 0L, period, async);
    }

    default @NotNull ScheduledTask scheduleRepeating(@NotNull BukkitPlugin plugin,
                                                     @NotNull Runnable task,
                                                     long period,
                                                     boolean async, boolean bukkit) {
        return scheduleRepeating(plugin, task, 0L, period, async, bukkit);
    }

    default @NotNull ScheduledTask scheduleRepeating(@NotNull BukkitPlugin plugin,
                                                     @NotNull Runnable task,
                                                     long delay, long period) {
        return scheduleRepeating(plugin, task, delay, period, false);
    }

    default @NotNull ScheduledTask scheduleRepeating(@NotNull BukkitPlugin plugin,
                                                     @NotNull Runnable task,
                                                     long delay, long period,
                                                     boolean async) {
        return scheduleRepeating(plugin, task, delay, period, async, false);
    }

    @NotNull ScheduledTask scheduleRepeating(@NotNull BukkitPlugin plugin,
                                             @NotNull Runnable task,
                                             long delay, long period,
                                             boolean async, boolean bukkit);
}
