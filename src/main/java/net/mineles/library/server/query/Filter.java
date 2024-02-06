package net.mineles.library.server.query;

import net.mineles.library.server.Server;

import java.util.function.Predicate;

@FunctionalInterface
public interface Filter extends Predicate<Server> {
    static boolean test(Filter filter, Server server) {
        return filter.test(server);
    }

    static boolean test(Filter[] filters, Server server) {
        for (Filter filter : filters) {
            if (!test(filter, server)) {
                return false;
            }
        }
        return true;
    }
}
