package net.mineles.library.server.strategy;

public interface Strategy {
    boolean check(String serverName);

    boolean checkForNewServer(String serverName);
}
