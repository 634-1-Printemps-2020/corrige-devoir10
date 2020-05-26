package ch.hesge.cours634.security;

import ch.hesge.cours634.security.exceptions.UnknownUser;
import ch.hesge.cours634.security.exceptions.UserAccountException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class AccountManagerTest {

    AccountManager accountManager = AccountManager.getInstance();

    String testUserName="pierre";

    @After
    public void teardown() throws UserAccountException {
       if( accountManager.isUserExist(testUserName)){
           accountManager.deleteUser(buildTestUser(testUserName, "01.01.2025"));
       }
    }

    @Test
    public void createAccountOK() {
        buildTestUser(testUserName, "01.01.2025");
    }

    @Test(expected = IllegalArgumentException.class )
    public void createAccountNOKInvalidExpirationDateInThePast() {
        buildTestUser(testUserName, "01.01.2018");
    }


    @Test
    public void addUserOK() throws UnknownUser, UserAccountException {
        UserAccount account = buildTestUser(testUserName, "01.01.2025");
        accountManager.addUser(account);
        UserAccount pierre = accountManager.getUser(testUserName);
        Assert.assertEquals(account,pierre);

    }

    @Test(expected =UserAccountException.class )
    public void addUserNOKAlreadyExist() throws UserAccountException {
        UserAccount account = buildTestUser(testUserName, "01.01.2025");
        accountManager.addUser(account);
        accountManager.addUser(account);

    }

    @Test
    public void disableUser() throws UnknownUser, UserAccountException {
        UserAccount account = buildTestUser(testUserName, "01.01.2025");
        accountManager.addUser(account);
        accountManager.disableUser(account);
        Assert.assertFalse(accountManager.getUser(account.getName()).isActif());

    }

    @Test
    public void updateUser() throws UnknownUser, UserAccountException {
        UserAccount account = buildTestUser(testUserName, "01.01.2025");
        accountManager.addUser(account);
        account.setPassword("Toto");
        accountManager.updateUser(account);
        Assert.assertEquals("Toto", accountManager.getUser(account.getName()).getPassword());

    }

    @Test (expected = UnknownUser.class)
    public void deleteUser() throws UnknownUser, UserAccountException {
        UserAccount account = buildTestUser(testUserName, "01.01.2025");
        accountManager.addUser(account);
        accountManager.deleteUser(account);
        accountManager.getUser(account.getName());
    }

    private LocalDate buildLocalDate(String s) {
        return LocalDate.parse(s, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    private UserAccount buildTestUser(String testUserName, String s) {
        return accountManager.createAccount(testUserName, "Password123", Arrays.asList("Student", "Assistant"), buildLocalDate(s));
    }

}
