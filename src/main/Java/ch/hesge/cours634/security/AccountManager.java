package ch.hesge.cours634.security;

import ch.hesge.cours634.security.db.UserAccountDAO;
import ch.hesge.cours634.security.exceptions.UnknownUser;
import ch.hesge.cours634.security.exceptions.UserAccountException;
import org.apache.log4j.Logger;

import javax.persistence.RollbackException;
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

    public UserAccount createAccount(String user, String password, LocalDate expirationDate){
        UserAccount userAccount = new UserAccount(user, password, expirationDate);
        userAccount.setActif(true);
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
        } catch (RollbackException e) {
                logger.error(e);
                throw new UserAccountException("Failed to add user "+user, e);
            }
    }

    public UserAccount getUser(String name) throws UnknownUser {
           return dao.findUserByName(name);
    }

    public void disableUser(UserAccount user) throws UnknownUser, UserAccountException {
        UserAccount account = getUser(user.getName());
        if(account==null) {
            throw new UnknownError("unknown user "+user);
        }
        account.setActif(false);
        dao.updateUser(account);
    }

    public void updateUser(UserAccount user) throws UserAccountException {
            dao.updateUser(user);
    }

    public void deleteUser(String name) throws UserAccountException {
            dao.deleteUser(name);
    }


    public boolean isUserExist(String login) {
        try {
			UserAccount user = dao.findUserByName(login);
        } catch (UnknownUser e) {
            return false;
        }
        return true;
    }

    public boolean isActif(String login) throws UnknownUser {
            return dao.findUserByName(login).isActif();
    }

    public boolean isExpired(String login) throws UnknownUser {
            return dao.findUserByName(login).getExpirationDate().isBefore(LocalDate.now());
    }
}
