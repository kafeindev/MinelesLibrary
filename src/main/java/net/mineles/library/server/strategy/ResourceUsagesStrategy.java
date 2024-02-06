package net.mineles.library.server.strategy;

public final class ResourceUsagesStrategy implements Strategy {


    @Override
    public boolean check(String serverName) {
        return false;
    }

    @Override
    public boolean checkForNewServer(String serverName) {
        return false;
    }
}
