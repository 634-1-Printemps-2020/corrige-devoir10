package ch.hesge.cours634.security.db;


import ch.hesge.cours634.security.exceptions.UnknownUser;
import ch.hesge.cours634.security.UserAccount;
import org.apache.log4j.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class UserAccountDAO {

    private static Logger logger = Logger.getLogger(UserAccountDAO.class);

    public void addUsers(List<UserAccount> users) throws SQLException {
		EntityManager em = JPAHelper.em();
		em.getTransaction().begin();
		try {
			for (UserAccount user : users) {
				logger.debug("importing user " + user.getName());
				em.persist(user);
			}
			em.getTransaction().commit();
			logger.info("end of importing,  inserted data has been commited ");
		} catch (Exception e) {
			em.getTransaction().rollback();
            logger.error("end of importing, inserted data has been rolledback  because of an error  ", e);
        }
    }


    public void addUser(UserAccount user)  {
		JPAHelper.persist(user);
    }

    public void updateUser(UserAccount user)  {
    	JPAHelper.merge(user);
    }

    public void deleteUser(String name)  {
       JPAHelper.remove(UserAccount.class, name);
    }


    public UserAccount findUserByName(String name)  throws UnknownUser {
		UserAccount userAccount = JPAHelper.em().find(UserAccount.class, name);
		if(userAccount==null){
			throw new UnknownUser(" User not found " + name);
		}
		return userAccount;
	}

    public List<UserAccount> findUsersBetweenDates(LocalDate start, LocalDate finish) throws SQLException, UnknownUser {
	//TODO adapter apres la mise en place des relations
		EntityManager em = JPAHelper.em();
		Query query = em.createQuery("select uac from UserAccount uac where  uac.expirationDate between :date1 and :date2");
		query.setParameter("date1", start);
		query.setParameter("date2", finish);
		List<UserAccount> resultList = query.getResultList();
		return resultList;
	}



}
