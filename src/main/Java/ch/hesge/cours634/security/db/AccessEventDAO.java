package ch.hesge.cours634.security.db;

import ch.hesge.cours634.security.AccessEvent;
import ch.hesge.cours634.security.Status;
import ch.hesge.cours634.security.UserAccount;

import javax.persistence.Query;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccessEventDAO {

    public List<AccessEvent> findByUserName(String userName) {
        List<AccessEvent> accessEvents = new ArrayList();
		Query query = JPAHelper.em().createQuery("select e from AccessEvent e where e.user.name = :toto");
		query.setParameter("toto", userName);
		return query.getResultList();
    }

	public List<AccessEvent> findByUserName2(String userName) {
		UserAccount userAccount = JPAHelper.em().find(UserAccount.class, userName);
		return userAccount.getAccessEvents();
	}

    public void insert(AccessEvent accessEvent, String user) {
		UserAccount userAccount = JPAHelper.em().find(UserAccount.class, user);
		accessEvent.setUser(userAccount);
		JPAHelper.persist(accessEvent);
    }

}
