package ch.hesge.cours634.security.db;

import ch.hesge.cours634.security.AccessEvent;
import ch.hesge.cours634.security.Status;
import ch.hesge.cours634.security.UserAccount;
import ch.hesge.cours634.security.exceptions.UnknownUser;
import org.h2.engine.User;

import javax.persistence.Query;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccessEventDAO {


    public List<AccessEvent> findEventsByUserName(String userName){
        Query query = JPAHelper.em().createQuery("select ae from AccessEvent  ae where ae.user.name = :user");
        query.setParameter("user",userName);
        return query.getResultList();
    }

    public List<AccessEvent> findEventsByUserName2(String userName){
        UserAccount userAccount = JPAHelper.em().find(UserAccount.class, userName);
        return userAccount.getAccessEvents();
    }

    public void insert(AccessEvent accessEvent, String user) throws UnknownUser {
        UserAccountDAO dao = new UserAccountDAO();
        accessEvent.setUser(dao.findUserByName(user));
        JPAHelper.persist(accessEvent);
    }


}
