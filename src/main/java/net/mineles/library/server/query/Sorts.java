package net.mineles.library.server.query;

public final class Sorts {
    private Sorts() {
    }

    public static Sort players() {
        return (server1, server2) -> Integer.compare(server1.getPlayers().size(), server2.getPlayers().size());
    }

    public static Sort entries() {
        return (server1, server2) -> Integer.compare(server1.getEntryCount(), server2.getEntryCount());
    }
}
