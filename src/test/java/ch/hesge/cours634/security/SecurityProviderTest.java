package ch.hesge.cours634.security;

import ch.hesge.cours634.security.db.AccessEventDAO;
import ch.hesge.cours634.security.db.JPAHelper;
import ch.hesge.cours634.security.exceptions.UnknownUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class SecurityProviderTest {

    @Before
    public void init(){
        // supprimer puis recréer le user margot
        UserAccount.Builder builder = new UserAccount.Builder();
        EntityManager em = JPAHelper.em();
        em.getTransaction().begin();
        UserAccount user = em.find(UserAccount.class, "margot");
        if(user!=null){
            em.remove(user);
        }
        user = builder.name("margot").password("Vacances2020").expirationDate(LocalDate.now().plusDays(50)).isActif(true).build();
        em.persist(user);
        em.getTransaction().commit();
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
    public void authenticateUnknownUser() throws AuthenticationException, UnknownUser {
        SecurityProvider securityProvider = SecurityProvider.getInstance();
        Assert.assertNotNull(securityProvider);
        securityProvider.authenticate("jerome", "Ski@Chamonix2020");
    }

    @Test
    public void testEvents() throws UnknownUser {
        try {
            SecurityProvider.getInstance().authenticate("margot", "Vacances2020");
            SecurityProvider.getInstance().authenticate("margot", "Vacances2020");
            SecurityProvider.getInstance().authenticate("margot", "abcdVacancs2020");
        }catch (AuthenticationException e){
            // ne rien faire, c'est nécessaire pour les assertions plus bas
        }
        AccessEventDAO dao = new AccessEventDAO();
        List<AccessEvent> events = dao.findEventsByUserName("margot");
        Assert.assertEquals(3, events.size());
        int nbFailure=0;
        int nbSuccess=0;
        for (AccessEvent ae : events ){
            switch(ae.getStatus()){
                case FAILURE: nbFailure++; break;
                case SUCCESS: nbSuccess++; break;
            }
        }
        Assert.assertEquals(1, nbFailure);
        Assert.assertEquals(2, nbSuccess);
    }

    @Test
    public void testEvents2() throws UnknownUser {
        try {
            SecurityProvider.getInstance().authenticate("margot", "Vacances2020");
            SecurityProvider.getInstance().authenticate("margot", "Vacances2020");
            SecurityProvider.getInstance().authenticate("margot", "abcdVacancs2020");
        }catch (AuthenticationException e){
            // ne rien faire, c'est nécessaire pour les assertions plus bas
        }
        AccessEventDAO dao = new AccessEventDAO();
        List<AccessEvent> events = dao.findEventsByUserName2("margot");
        Assert.assertEquals(3, events.size());
        int nbFailure=0;
        int nbSuccess=0;
        for (AccessEvent ae : events ){
            switch(ae.getStatus()){
                case FAILURE: nbFailure++; break;
                case SUCCESS: nbSuccess++; break;
            }
        }
        Assert.assertEquals(1, nbFailure);
        Assert.assertEquals(2, nbSuccess);
    }


}
