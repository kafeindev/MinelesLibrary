package net.mineles.library.cluster.exception;

public abstract class DockerException extends RuntimeException {

    protected DockerException(String message) {
        super(message);
    }

    protected DockerException(String message, Throwable cause) {
        super(message, cause);
    }

    protected DockerException(Throwable cause) {
        super(cause);
    }
}
