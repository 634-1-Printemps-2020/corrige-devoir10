package ch.hesge.cours634.security;

import ch.hesge.cours634.security.db.JPAHelper;
import ch.hesge.cours634.security.exceptions.UnknownUser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;

public class SecurityProviderTest {

    @Before
    public void init(){
        UserAccount.Builder builder = new UserAccount.Builder();
        UserAccount user = JPAHelper.em().find(UserAccount.class, "margot");
        if(user==null) {
            user = builder.name("margot").password("Vacances2020").expirationDate(LocalDate.now().plusDays(50)).isActif(true).build();
            JPAHelper.persist(user);
        }
    }


    @Test
    public void authenticateOK() throws AuthenticationException, UnknownUser {
        SecurityProvider securityProvider = SecurityProvider.getInstance();
        Assert.assertNotNull(securityProvider);
        securityProvider.authenticate("margot", "Vacances2020");

    }

    @Test(expected = AuthenticationException.class)
    public void authenticateKO() throws AuthenticationException, UnknownUser {
        SecurityProvider securityProvider = SecurityProvider.getInstance();
        Assert.assertNotNull(securityProvider);
        securityProvider.authenticate("margot", "Vacances1919");

    }

    @Test(expected = UnknownUser.class)
    public void authenticateUnknownUser() throws AuthenticationException, UnknownUser, SQLException {
        SecurityProvider securityProvider = SecurityProvider.getInstance();
        Assert.assertNotNull(securityProvider);
        securityProvider.authenticate("jerome", "Ski@Chamonix2020");
    }
}
