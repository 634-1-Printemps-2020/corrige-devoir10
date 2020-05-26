package ch.hesge.cours634.security;

import ch.hesge.cours634.security.db.UserAccountDAO;
import ch.hesge.cours634.security.exceptions.UnknownUser;
import ch.hesge.cours634.security.exceptions.UserAccountException;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AccountManager {
    private static Logger logger = Logger.getLogger(AccountManager.class);

    UserAccountDAO dao = new UserAccountDAO();

    static private AccountManager INSTANCE = new AccountManager();

    static public AccountManager getInstance(){
        return INSTANCE;
    }

    public UserAccount createAccount(String user, String password, List<String> roles, LocalDate expirationDate){
        UserAccount userAccount = new UserAccount(user, password, roles, expirationDate);
        validate(userAccount);
        return userAccount;
    }

    private void validate(UserAccount userAccount) {

        if(userAccount.getName()==null){
            throw new IllegalArgumentException("name can not be null");
        }

        if(userAccount.getExpirationDate().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("expiration date can not be in the past");
        }
    }

    public void addUser(UserAccount user ) throws UserAccountException {
        try {
            dao.addUser(user);
        } catch (SQLException e) {
                logger.error(e);
                throw new UserAccountException("Failed to add user "+user, e);
            }
    }

    public UserAccount getUser(String name) throws UnknownUser {
        UserAccount account = null;
        try {
            account = dao.findUserByName(name);
        } catch (SQLException e) {
            logger.error(e);
            throw new UnknownUser("Failed to find user "+name, e);
        }
        return account;
    }

    public void disableUser(UserAccount user) throws UnknownUser, UserAccountException {
        UserAccount account = getUser(user.getName());
        if(account==null) {
            throw new UnknownError("unknown user "+user);
        }
        account.setActif(false);
        try {
            dao.updateUser(account);
        } catch (SQLException e) {
            logger.error(e);
            throw new UserAccountException("Failed to disable user "+user, e);
        }
    }

    public void updateUser(UserAccount user) throws UserAccountException {
        try {
            dao.updateUser(user);
        } catch (SQLException e) {
            logger.error(e);
            throw new UserAccountException("Failed to update user "+user, e);
        }
    }

    public void deleteUser(UserAccount user) throws UserAccountException {
        try {
            dao.deleteUser(user);
        } catch (SQLException e) {
            logger.error(e);
            throw new UserAccountException("Failed to delete user "+user, e);
        }
    }

    public boolean isUserExist(String login) {
        try {
			UserAccount user = dao.findUserByName(login);
			return user!=null;
        } catch (SQLException | UnknownUser e) {
            return false;
        }
    }

    public boolean isActif(String login) throws UnknownUser {

        try {
            return dao.findUserByName(login).isActif();
        } catch (SQLException e) {
            throw new UnknownUser("Utilisateur " + login + " inconnu" );
        }
    }

    public boolean isExpired(String login) throws UnknownUser {
        try {
            return dao.findUserByName(login).getExpirationDate().isBefore(LocalDate.now());
        } catch (SQLException e) {
            throw new UnknownUser("Utilisateur " + login + " inconnu" );
        }
    }
}
