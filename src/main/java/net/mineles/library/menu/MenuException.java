package net.mineles.library.menu;

public class MenuException extends RuntimeException {
    private static final long serialVersionUID = 3859325542954513163L;

    public MenuException(String message) {
        super(message);
    }

    public MenuException(String message, Throwable cause) {
        super(message, cause);
    }

    public MenuException(Throwable cause) {
        super(cause);
    }

    public MenuException() {
        super();
    }
}
