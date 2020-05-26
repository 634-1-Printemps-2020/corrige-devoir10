package ch.hesge.cours634.security.db;

import ch.hesge.cours634.security.AccessEvent;
import ch.hesge.cours634.security.db.magic.MagicPersister;
import ch.hesge.cours634.security.exceptions.UnknownUser;
import ch.hesge.cours634.security.UserAccount;
import org.apache.log4j.Logger;
import org.h2.engine.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserAccountDAO {

    private static Logger logger = Logger.getLogger(UserAccountDAO.class);

    protected static final String USER_TABLE = "USER_ACCOUNT";

    protected static final String USER_NAME = "user_name";
    protected static final String USER_PASSWORD = "user_password";
    protected static final String USER_ROLES = "user_roles";
    protected static final String USER_EXPIRATION_DATE = "expiration_date";
    protected static final String USER_IS_ACTIVE = "is_active";


    public List<UserAccount> loadAccounts() throws SQLException {
        List<UserAccount> users = new ArrayList();

        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            con = DBHelper.getConnection();
            pStmt = con.prepareStatement("select * from " + USER_TABLE);
            rs = pStmt.executeQuery();
            UserAccount.Builder builder = new UserAccount.Builder();
            while (rs.next()) {
                UserAccount userAccount = builder.name((rs.getString(USER_NAME)))
                        .password(rs.getString(USER_PASSWORD))
                        .roles(Arrays.asList(rs.getString(USER_ROLES).split(",")))
                        .isActif(rs.getBoolean(USER_IS_ACTIVE))
                        .expirationDate(rs.getDate(USER_EXPIRATION_DATE).toLocalDate())
                        .build();
                AccessEventDAO accessEventDAO = new AccessEventDAO();
                List<AccessEvent> userEvents = accessEventDAO.findByUserName(userAccount.getName());
                userAccount.setAccessEvents(userEvents);
                users.add(userAccount);
            }
        } finally {
            DBHelper.closeAll(con, pStmt, rs);
        }
        return users;
    }

    public void addUsers(List<UserAccount> users) throws SQLException {
        Connection con = DBHelper.getConnection();
        ;
        con.setAutoCommit(false);
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            for (UserAccount user : users) {
                logger.debug("importing user " + user.getName());
                insertUser(user, con);
            }
            con.commit();
            logger.info("end of importing,  inserted data has been commited ");
        } catch (Exception e) {
            con.rollback();
            logger.error("end of importing, inserted data has been rolledback  because of an error  ", e);
        } finally {
            DBHelper.closeAll(con, pStmt, rs);
        }
    }


    public void addUser(UserAccount user) throws SQLException {
            MagicPersister.persist(user);
    }

    private void insertUser(UserAccount user, Connection con) throws SQLException {
        PreparedStatement pStmt = con.prepareStatement("INSERT INTO " + USER_TABLE + " (" + USER_NAME + ", " + USER_PASSWORD + ", " + USER_ROLES + ", " + USER_IS_ACTIVE + ", " + USER_EXPIRATION_DATE + " ) VALUES (?,?,?,?,?)");
        pStmt.setString(1, user.getName());
        pStmt.setString(2, user.getPassword());
        pStmt.setString(3, toCVFRoles(user.getRoles()));
        pStmt.setBoolean(4, user.isActif());
        pStmt.setDate(5, java.sql.Date.valueOf(user.getExpirationDate()));
        pStmt.executeUpdate();
    }

    public void updateUser(UserAccount user) throws SQLException {
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            con = DBHelper.getConnection();
            pStmt = con.prepareStatement("UPDATE " + USER_TABLE + " SET " + USER_PASSWORD + " = ?, " + USER_ROLES + " = ?, " + USER_IS_ACTIVE + " = ?, " + USER_EXPIRATION_DATE + " = ? WHERE " + USER_NAME + " = ?");
            pStmt.setString(1, user.getPassword());
            pStmt.setString(2, toCVFRoles(user.getRoles()));
            pStmt.setBoolean(3, user.isActif());
            pStmt.setDate(4, java.sql.Date.valueOf(user.getExpirationDate()));
            pStmt.setString(5, user.getName());
            pStmt.executeUpdate();
        } finally {
            DBHelper.closeAll(con, pStmt, rs);
        }
    }


    public void deleteUser(UserAccount user) throws SQLException {
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            con = DBHelper.getConnection();
            pStmt = con.prepareStatement("DELETE FROM " + USER_TABLE + " WHERE " + USER_NAME + " = ?");
            pStmt.setString(1, user.getName());
            pStmt.executeUpdate();
        } finally {
            DBHelper.closeAll(con, pStmt, rs);
        }
    }


    public UserAccount findUserByName(String name) throws SQLException, UnknownUser {
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            con = DBHelper.getConnection();
            pStmt = con.prepareStatement("select * from " + USER_TABLE + " WHERE " + USER_NAME + " = ?");
            pStmt.setString(1, name);
            rs = pStmt.executeQuery();
            UserAccount.Builder builder = new UserAccount.Builder();
            if (rs.next()) {
                UserAccount userAccount = builder.name((rs.getString(UserAccountDAO.USER_NAME)))
                        .password(rs.getString(USER_PASSWORD))
                        .roles(Arrays.asList(rs.getString(USER_ROLES).split(",")))
                        .isActif(rs.getBoolean(USER_IS_ACTIVE))
                        .expirationDate(rs.getDate(USER_EXPIRATION_DATE).toLocalDate())
                        .build();
                AccessEventDAO accessEventDAO = new AccessEventDAO();
                List<AccessEvent> userEvents = accessEventDAO.findByUserName(userAccount.getName());
                userAccount.setAccessEvents(userEvents);
                return userAccount;
            } else {
                throw new UnknownUser("Aucun utilisateur au nom de " + name);
            }
        } finally {
            DBHelper.closeAll(con, pStmt, rs);
        }

    }

    // SELECT DISTINCT user_name, user_password, is_active, expiration_date, user_roles FROM access_event JOIN user_account ON user_login = user_name WHERE event_date BETWEEN '2020-05-10' AND '2020-05-11'
    public List<UserAccount> findUsersBetweenDates(LocalDate start, LocalDate finish) throws SQLException, UnknownUser {
        List<UserAccount> users = new ArrayList();

        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            con = DBHelper.getConnection();
            pStmt = con.prepareStatement("SELECT DISTINCT " + USER_NAME + ", " + USER_PASSWORD + ", " + USER_IS_ACTIVE + ", " + USER_EXPIRATION_DATE + ", " +
                    USER_ROLES + " FROM " + AccessEventDAO.ACCESS_EVENT_TABLE + " JOIN " + USER_TABLE + " ON " + AccessEventDAO.USER_LOGIN + " = " + USER_NAME + " WHERE " +
                    AccessEventDAO.EVENT_DATE + " BETWEEN ? AND ?");
            pStmt.setDate(1, Date.valueOf(start));
            pStmt.setDate(2, Date.valueOf(finish));
            rs = pStmt.executeQuery();
            UserAccount.Builder builder = new UserAccount.Builder();
            while (rs.next()) {
                UserAccount userAccount = builder.name((rs.getString(USER_NAME)))
                        .password(rs.getString(USER_PASSWORD))
                        .roles(Arrays.asList(rs.getString(USER_ROLES).split(",")))
                        .isActif(rs.getBoolean(USER_IS_ACTIVE))
                        .expirationDate(rs.getDate(USER_EXPIRATION_DATE).toLocalDate())
                        .build();
                AccessEventDAO accessEventDAO = new AccessEventDAO();
                List<AccessEvent> userEvents = accessEventDAO.findByUserName(userAccount.getName());
                userAccount.setAccessEvents(userEvents);
                users.add(userAccount);
            }
        } finally {
            DBHelper.closeAll(con, pStmt, rs);
        }
        return users;
    }


    private static String toCVFRoles(List<String> roles) {
        return String.join(",", roles);
    }


}
