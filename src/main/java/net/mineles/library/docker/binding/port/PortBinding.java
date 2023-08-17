package net.mineles.library.docker.binding.port;

import com.github.dockerjava.api.model.ContainerPort;
import net.mineles.library.docker.binding.AbstractBinding;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class PortBinding extends AbstractBinding<Integer, Integer> {

    private final Protocol protocol;

    public PortBinding(int key, int value, Protocol protocol) {
        super(key, value);
        this.protocol = protocol;
    }

    public PortBinding(Integer key, Integer value) {
        this(key, value, Protocol.TCP);
    }

    public static @NotNull PortBinding fromBinding(String binding) {
        String[] parts = binding.split(":", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid port: " + binding);
        }

        String[] portParts = parts[1].split("/", 2);
        int privatePort = Integer.parseInt(parts[0]);
        int publicPort = Integer.parseInt(portParts[0]);

        if (portParts.length == 1) {
            return new PortBinding(privatePort, publicPort);
        } else if (portParts.length == 2) {
            return new PortBinding(privatePort, publicPort, Protocol.fromType(portParts[1]));
        } else {
            throw new IllegalArgumentException("Invalid port: " + binding);
        }
    }

    public static @NotNull PortBinding fromContainerPort(ContainerPort port) {
        if (port.getType() == null) return new PortBinding(port.getPrivatePort(), port.getPublicPort());

        return new PortBinding(port.getPrivatePort(), port.getPublicPort(), Protocol.fromType(port.getType()));
    }

    @Override
    public @NotNull String getFullBinding() {
        String protocolType = getProtocol().name().toLowerCase(Locale.ROOT);
        return String.format("%d:%d/%s", getKey(), getValue(), protocolType);
    }

    public Protocol getProtocol() {
        return protocol;
    }
}
