package net.mineles.library.docker.network.request;

import net.mineles.library.libs.dockerjava.api.model.Network;

public record CreateNetworkRequest(
        String name,
        String driver,
        Network.Ipam ipam
) {
}
