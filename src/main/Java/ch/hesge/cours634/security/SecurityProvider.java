package ch.hesge.cours634.security;

import ch.hesge.cours634.security.db.AccessEventDAO;
import ch.hesge.cours634.security.exceptions.UnknownUser;

import java.sql.SQLException;
import java.util.List;


public class SecurityProvider implements Authenticator, Authorizer {

    private static SecurityProvider INSTANCE = new SecurityProvider();
    AccountManager accMgr;

    private  SecurityProvider() {
        accMgr = AccountManager.getInstance();
    }

    public static SecurityProvider getInstance(){
        return INSTANCE;
    }

    @Override
    public void authenticate(String login, String password) throws AuthenticationException, UnknownUser, SQLException {
        AccessEventDAO accessEventDAO = new AccessEventDAO();


        if (!accMgr.isUserExist(login)) {
            accessEventDAO.insert(new AccessEvent(Status.FAILURE, "User " + login + " is not registered in our system"), null);
            throw new UnknownUser("User " + login + " is not registered in our system");
        }

        if (!accMgr.getUser(login).getPassword().equals(password)) {
            accessEventDAO.insert(new AccessEvent(Status.FAILURE, "Authentication failure. please check your password and retry again"), login);
            throw new AuthenticationException("Authentication failure. please check your password and retry again");
        }


        if(!accMgr.isActif(login)){
            accessEventDAO.insert(new AccessEvent(Status.FAILURE, "Authentication failure. account is blocked"), login);
            throw new AuthenticationException("Authentication failure. account is blocked");
        }

        if(accMgr.isExpired(login)){
            accessEventDAO.insert(new AccessEvent(Status.FAILURE, "Authentication failure. account expired"), login);
            throw new AuthenticationException("Authentication failure. account expired");
        }

        accessEventDAO.insert(new AccessEvent(Status.SUCCESS, "User authenticated successfully."), login);


    }

    @Override
    public List<String> getRoles(String user) throws UnknownUser {
        return accMgr.getUser(user).getRoles();
    }

    @Override
    public boolean isCallerInRole(String user, String role) throws UnknownUser {
        return accMgr.getUser(user).getRoles().contains(role);
    }
}
