package net.mineles.library.cluster.network.request;

import com.github.dockerjava.api.model.Network;

public record CreateNetworkRequest(
        String name,
        String driver,
        Network.Ipam ipam
) {
}
