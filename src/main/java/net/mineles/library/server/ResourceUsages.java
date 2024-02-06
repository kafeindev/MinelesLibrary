package net.mineles.library.server;

import me.lucko.spark.api.SparkProvider;
import me.lucko.spark.api.statistic.StatisticWindow;

public final class ResourceUsages {
    private final double cpuProcess;
    private final double cpuSystem;
    private final double memory;
    private final double tps;
    private final double mspt;

    public ResourceUsages(double cpuProcess, double cpuSystem, double memory, double tps, double mspt) {
        this.cpuProcess = cpuProcess;
        this.cpuSystem = cpuSystem;
        this.memory = memory;
        this.tps = tps;
        this.mspt = mspt;
    }
}
