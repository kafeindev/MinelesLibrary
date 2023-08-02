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

package net.mineles.library.plugin.scheduler;

import net.mineles.library.plugin.BukkitPlugin;
import net.mineles.library.plugin.scheduler.concurrent.ConcurrentThreadFactory;
import net.mineles.library.plugin.scheduler.concurrent.forkjoin.ForkJoinPoolBuilder;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.*;
import java.util.logging.Logger;

public abstract class AbstractTaskExecutor<E extends ExecutorService> {
    private static final int DEFAULT_POOL_SIZE = 1;

    private final @NotNull Logger logger;
    private final @NotNull String name;
    private final @NotNull E executor;

    private int awaitTerminationSeconds = 60;
    private ForkJoinPool workerPool;

    protected AbstractTaskExecutor(@NotNull Logger logger,
                                   @NotNull String name) {
        this(logger, name, new ConcurrentThreadFactory(name, true));
    }

    protected AbstractTaskExecutor(@NotNull Logger logger,
                                   @NotNull String name,
                                   @NotNull ThreadFactory threadFactory) {
        this(logger, name, threadFactory, DEFAULT_POOL_SIZE);
    }

    protected AbstractTaskExecutor(@NotNull Logger logger,
                                   @NotNull String name,
                                   @NotNull ThreadFactory threadFactory,
                                   int poolSize) {
        this.logger = logger;
        this.name = name;
        this.executor = createExecutor(threadFactory, poolSize);
    }

    protected AbstractTaskExecutor(@NotNull Logger logger,
                                   @NotNull String name,
                                   @NotNull E executor) {
        this.logger = logger;
        this.name = name;
        this.executor = executor;
    }

    protected abstract E createExecutor(@NotNull ThreadFactory threadFactory, int poolSize);

    public void execute(@NotNull BukkitPlugin plugin,
                        @NotNull Runnable task,
                        boolean async,
                        boolean bukkit) {
        if (bukkit && async) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin.getPlugin(), task);
        } else if (bukkit) {
            Bukkit.getScheduler().runTask(plugin.getPlugin(), task);
        } else if (async && workerPool != null) {
            this.workerPool.execute(task);
        } else {
            this.executor.execute(task);
        }
    }

    public @NotNull E getExecutor() {
        return this.executor;
    }

    public void setAwaitTerminationSeconds(int awaitTerminationSeconds) {
        this.awaitTerminationSeconds = awaitTerminationSeconds;
    }

    public @NotNull ForkJoinPoolBuilder createWorkerPoolBuilder() {
        return new ForkJoinPoolBuilder(this.logger, this.name);
    }

    public @Nullable ForkJoinPool getWorkerPool() {
        return this.workerPool;
    }

    public void setWorkerPool(@NotNull ForkJoinPool workerPool) {
        this.workerPool = workerPool;
    }

    public boolean isAsyncable() {
        return this.workerPool != null;
    }

    public void shutdownExecutor() {
        shutdown(this.executor);
    }

    public void shutdownWorkerPool() {
        if (this.workerPool != null) {
            shutdown(this.workerPool);
        }
    }

    private void shutdown(@NotNull ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(this.awaitTerminationSeconds, TimeUnit.SECONDS)) {
                this.logger.severe("Task executor did not terminate in the specified time.");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.getAllStackTraces().keySet().stream()
                    .filter(thread -> thread.getName().startsWith(this.name + "-thread-"))
                    .forEach(thread -> {
                        this.logger.severe("Thread " + thread.getName() + " is still running. Interrupting...");
                        thread.interrupt();
                    });
        }
    }
}
