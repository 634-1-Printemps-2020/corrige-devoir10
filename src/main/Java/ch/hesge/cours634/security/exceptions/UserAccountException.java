package ch.hesge.cours634.security.exceptions;


public class UserAccountException extends Throwable {
    public UserAccountException(String s, Exception e) {
        super(s);
    }
}
