package ch.hesge.cours634.security;

import ch.hesge.cours634.security.exceptions.UnknownUser;

import java.util.List;

public interface Authorizer {

    public List<String>  getRoles(String user) throws UnknownUser;
    public boolean isCallerInRole(String user, String role) throws UnknownUser;
}
