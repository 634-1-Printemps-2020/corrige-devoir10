package ch.hesge.cours634.security.exceptions;

import java.sql.SQLException;

public class UserAccountException extends Throwable {
    public UserAccountException(String s, SQLException e) {
        super(s);
    }
}
