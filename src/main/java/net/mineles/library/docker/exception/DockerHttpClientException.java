package net.mineles.library.docker.exception;

public class DockerHttpClientException extends DockerException {

    public DockerHttpClientException(String message) {
        super(message);
    }

    public DockerHttpClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public DockerHttpClientException(Throwable cause) {
        super(cause);
    }
}
