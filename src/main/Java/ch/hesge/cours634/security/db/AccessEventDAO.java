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


    public List<AccessEvent> findEventsByUserName(String userName) {
        Query query = JPAHelper.em().createQuery("select ae from AccessEvent ae where ae.user.name = :name");
        query.setParameter("name", userName);
        return query.getResultList();
    }

    public List<AccessEvent> findEventsByUserName2(String userName) {
        UserAccount userAccount = JPAHelper.em().find(UserAccount.class, userName);
        return userAccount.getAccessEvents();
    }

    public void insert(AccessEvent accessEvent, String userName) {
        UserAccount userAccount = JPAHelper.em().find(UserAccount.class, userName);
        accessEvent.setUser(userAccount);
        JPAHelper.persist(accessEvent);
    }

}
