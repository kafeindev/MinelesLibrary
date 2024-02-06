package net.mineles.library.server;

import com.google.common.collect.Sets;
import net.mineles.library.connection.HostAndPort;

import java.util.Set;
import java.util.UUID;

public final class RegisteredServer extends Server {
    private final HostAndPort hostAndPort;
    private final Set<UUID> players;
    private final Set<UUID> entries;
    private final long startTime;

    public RegisteredServer(Server handle, HostAndPort hostAndPort) {
        this(handle, hostAndPort, Sets.newHashSet(), Sets.newHashSet());
    }

    public RegisteredServer(Server handle, HostAndPort hostAndPort, Set<UUID> players, Set<UUID> entries) {
        this(handle, hostAndPort, players, entries, System.currentTimeMillis());
    }

    public RegisteredServer(Server handle, HostAndPort hostAndPort, Set<UUID> players, Set<UUID> entries, long startTime) {
        super(handle.getId(), handle.getName(), handle.getImage());
        this.hostAndPort = hostAndPort;
        this.players = players;
        this.entries = entries;
        this.startTime = startTime;
    }

    public HostAndPort getHostAndPort() {
        return hostAndPort;
    }

    public Set<UUID> getPlayers() {
        return players;
    }

    public int getPlayerCount() {
        return players.size();
    }

    public Set<UUID> getEntries() {
        return entries;
    }

    public int getEntryCount() {
        return entries.size();
    }

    public long getStartTime() {
        return startTime;
    }
}
