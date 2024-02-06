package net.mineles.library.server;

import net.mineles.library.docker.client.DockerTemplate;
import net.mineles.library.docker.container.ContainerTemplate;
import net.mineles.library.docker.container.DockerContainerProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ServerController {
    private final ServerManager serverManager;
    private final @Nullable DockerTemplate dockerTemplate;

    public ServerController(ServerManager serverManager, @Nullable DockerTemplate dockerTemplate) {
        this.serverManager = serverManager;
        this.dockerTemplate = dockerTemplate;
    }

    public void startServer(@NotNull String name) {
        DockerTemplate dockerTemplate = this.dockerTemplate;
        if (dockerTemplate == null) {
            throw new NullPointerException("Docker template is null");
        }

        ContainerTemplate template = dockerTemplate.getContainerTemplates().getTemplate(name);
        if (template == null) {
            throw new NullPointerException("Container template not found for " + name);
        }

        startServer(template);
    }

    public void startServer(@NotNull ContainerTemplate template) {
        DockerTemplate dockerTemplate = this.dockerTemplate;
        if (dockerTemplate == null) {
            throw new NullPointerException("Docker template is null");
        }

        String id = dockerTemplate.createContainer(template)
                .block();
        if (id == null) {
            throw new NullPointerException("Id is null");
        }

        DockerContainerProperties properties = dockerTemplate.findByContainerId(id)
                .block();
        if (properties == null) {
            throw new NullPointerException("Properties is null");
        }

        dockerTemplate.startContainer(id)
                .block();

        String name = properties.names().get(0);
        name = name.replace("/", "");

        Server server = new Server(id, name, properties.image());
        this.serverManager.getServerCache().setServer(name, server);
    }

    public void stopServer(@NotNull Server server) {
        DockerTemplate dockerTemplate = this.dockerTemplate;
        if (dockerTemplate == null) {
            throw new NullPointerException("Docker template is null");
        }

        if (!server.isStarted()) {
            return;
        }

        this.serverManager.sendDispatchCommand(server.getName(), "stop");
    }
}
