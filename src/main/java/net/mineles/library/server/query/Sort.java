package net.mineles.library.server.query;

import net.mineles.library.server.RegisteredServer;

import java.util.Comparator;
import java.util.stream.Stream;

@FunctionalInterface
public interface Sort extends Comparator<RegisteredServer> {
    static Stream<RegisteredServer> sort(Sort sort, Stream<RegisteredServer> servers) {
        return servers.sorted(sort);
    }

    static Stream<RegisteredServer> sort(Sort[] sorts, Stream<RegisteredServer> servers) {
        for (Sort sort : sorts) {
            servers = sort(sort, servers);
        }
        return servers;
    }
}
