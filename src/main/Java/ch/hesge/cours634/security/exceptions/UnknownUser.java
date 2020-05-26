package ch.hesge.cours634.security.exceptions;

public class UnknownUser extends Exception {
    public UnknownUser(String msg) {
        super(msg);
    }

    public UnknownUser(String message, Throwable cause) {
        super(message, cause);
    }
}
