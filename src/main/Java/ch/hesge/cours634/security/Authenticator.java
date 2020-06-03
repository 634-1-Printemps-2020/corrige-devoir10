package ch.hesge.cours634.security;

import ch.hesge.cours634.security.exceptions.UnknownUser;

import java.sql.SQLException;

public interface Authenticator {

    void authenticate (String login, String password) throws AuthenticationException, UnknownUser;
}

