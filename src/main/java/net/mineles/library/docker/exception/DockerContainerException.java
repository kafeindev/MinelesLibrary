package net.mineles.library.docker.exception;

public class DockerContainerException extends DockerException {

    public DockerContainerException(String containerId, String message) {
        super(String.format("Container %s: %s", containerId, message));
    }

    public DockerContainerException(String containerId, String message, Throwable cause) {
        super(String.format("Container %s: %s", containerId, message), cause);
    }

    public DockerContainerException(String message) {
        super(message);
    }

    public DockerContainerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DockerContainerException(Throwable cause) {
        super(cause);
    }
}
