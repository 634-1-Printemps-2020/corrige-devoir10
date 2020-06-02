package ch.hesge.cours634.security;

import ch.hesge.cours634.security.exceptions.UnknownUser;
import ch.hesge.cours634.security.exceptions.UserAccountException;
import org.junit.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AccessEventTest {
	AccountManager accountManager = AccountManager.getInstance();
	SecurityProvider securityProvider = SecurityProvider.getInstance();
	String testUserName="pierre";
	UserAccount userTest;
	//    @After
	//    public void teardown() throws UserAccountException {
	//        if( accountManager.isUserExist(testUserName)){
	//            accountManager.deleteUser(testUserName);
	//        }
	//    }

	@Before
	public  void insertTestUser() throws UserAccountException {
		userTest = buildTestUser(testUserName, "01.01.2025");
		if( accountManager.isUserExist(testUserName)){
			accountManager.deleteUser(testUserName);
		}
		accountManager.addUser(userTest);
	}
	@Test
	public void authenticateOk() throws UnknownUser, AuthenticationException {
		securityProvider.authenticate(testUserName, "Password123");
	}
	@Test (expected = AuthenticationException.class)
	public void authenticateFailBLock() throws AuthenticationException, UnknownUser, UserAccountException {
		accountManager.disableUser(userTest);
		securityProvider.authenticate(testUserName, "Password123");
	}
	@Test (expected = UnknownUser.class)
	public void authentificateFailUnknowUser() throws AuthenticationException, UnknownUser {
		securityProvider.authenticate("aa", "");
	}
	@Test
	public void checkInset() throws UnknownUser, AuthenticationException {
		securityProvider.authenticate(testUserName, "Password123");
		securityProvider.authenticate(testUserName, "Password123");
		UserAccount u = accountManager.getUser(testUserName);
		System.out.println(u);
		for (AccessEvent evt : u.getAccessEvents()){
			System.out.println(evt);
		}
		Assert.assertTrue(u.getAccessEvents().size() > 0);
	}

	private LocalDate buildLocalDate(String s) {
		return LocalDate.parse(s, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	}
	private UserAccount buildTestUser(String testUserName, String s) {
		return accountManager.createAccount(testUserName, "Password123", buildLocalDate(s));
	}

}
