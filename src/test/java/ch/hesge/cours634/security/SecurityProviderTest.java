package ch.hesge.cours634.security;

import ch.hesge.cours634.security.db.AccessEventDAO;
import ch.hesge.cours634.security.db.JPAHelper;
import ch.hesge.cours634.security.exceptions.UnknownUser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class SecurityProviderTest {

    @Before
    public void init(){
        UserAccount.Builder builder = new UserAccount.Builder();
        EntityManager em = JPAHelper.em();
        em.getTransaction().begin();
        UserAccount user = em.find(UserAccount.class, "margot");
        if(user!=null) {
            em.remove(user);
        }
        user = builder.name("margot").password("Vacances2020").expirationDate(LocalDate.now().plusDays(50)).isActif(true).build();
        em.persist(user);
        em.getTransaction().commit();
    }


    @Test
    public void authenticateOK() throws AuthenticationException, UnknownUser, SQLException {
        SecurityProvider securityProvider = SecurityProvider.getInstance();
        Assert.assertNotNull(securityProvider);
        securityProvider.authenticate("margot", "Vacances2020");
        securityProvider.authenticate("margot", "Vacances2020");
        securityProvider.authenticate("margot", "Vacances2020");
        AccessEventDAO dao = new AccessEventDAO();
        List<AccessEvent> events = dao.findEventsByUserName2("margot");
        System.out.println(events);

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
