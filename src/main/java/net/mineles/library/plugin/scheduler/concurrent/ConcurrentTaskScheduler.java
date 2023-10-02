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

package net.mineles.library.plugin.scheduler.concurrent;

import net.mineles.library.plugin.BukkitPlugin;
import net.mineles.library.plugin.scheduler.AbstractTaskExecutor;
import net.mineles.library.plugin.scheduler.task.ScheduledTask;
import net.mineles.library.plugin.scheduler.task.TaskScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public final class ConcurrentTaskScheduler extends AbstractTaskExecutor<ScheduledThreadPoolExecutor> implements TaskScheduler {
    public ConcurrentTaskScheduler(@NotNull Logger logger,
                                   @NotNull String name) {
        super(logger, name);
    }

    public ConcurrentTaskScheduler(@NotNull Logger logger,
                                   @NotNull String name,
                                   @NotNull ThreadFactory threadFactory) {
        super(logger, name, threadFactory);
    }

    public ConcurrentTaskScheduler(@NotNull Logger logger,
                                   @NotNull String name,
                                   @NotNull ThreadFactory threadFactory,
                                   int poolSize) {
        super(logger, name, threadFactory, poolSize);
    }

    @Override
    protected ScheduledThreadPoolExecutor createExecutor(@NotNull ThreadFactory threadFactory, int poolSize) {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(poolSize, threadFactory);
        executor.setRemoveOnCancelPolicy(true);
        executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);

        return executor;
    }

    @Override
    public @NotNull ScheduledTask schedule(@NotNull BukkitPlugin plugin,
                                           @NotNull Runnable task,
                                           long delay,
                                           boolean async,
                                           boolean bukkit) {
        ScheduledFuture<?> future = getExecutor().schedule(
                () -> execute(plugin, task, async, bukkit), delay, TimeUnit.MILLISECONDS);

        return () -> future.cancel(false);
    }

    @Override
    public @NotNull ScheduledTask scheduleRepeating(@NotNull BukkitPlugin plugin,
                                                    @NotNull Runnable task,
                                                    long delay,
                                                    long period,
                                                    boolean async,
                                                    boolean bukkit) {
        ScheduledFuture<?> future = getExecutor().scheduleAtFixedRate(
                () -> execute(plugin, task, async, bukkit), delay, period, TimeUnit.MILLISECONDS);

        return () -> future.cancel(false);
    }
}
