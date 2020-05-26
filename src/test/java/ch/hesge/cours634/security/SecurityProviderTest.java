package ch.hesge.cours634.security;

import ch.hesge.cours634.security.exceptions.UnknownUser;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class SecurityProviderTest {

    @Test
    public void authenticateOK() throws AuthenticationException, UnknownUser, SQLException {
        SecurityProvider securityProvider = SecurityProvider.getInstance();
        Assert.assertNotNull(securityProvider);
        securityProvider.authenticate("margot", "Vacances2020");

    }

    @Test(expected = AuthenticationException.class)
    public void authenticateKO() throws AuthenticationException, UnknownUser, SQLException {
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
