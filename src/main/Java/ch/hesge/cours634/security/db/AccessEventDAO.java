package ch.hesge.cours634.security.db;

import ch.hesge.cours634.security.AccessEvent;
import ch.hesge.cours634.security.Status;
import ch.hesge.cours634.security.UserAccount;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccessEventDAO {

    protected static final String ACCESS_EVENT_TABLE = "access_event";
    protected static final String ID = "event_id";
    protected static final String USER_LOGIN = "user_login";
    protected static final String EVENT_ETAT = "event_etat";
    protected static final String EVENT_DATE = "event_date";
    protected static final String EVENT_MESSAGE = "event_message";

    public List<AccessEvent> findByUserName(String userName) throws SQLException {
        List<AccessEvent> accessEvents = new ArrayList();

        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            con = DBHelper.getConnection();
            pStmt = con.prepareStatement("select * from " + ACCESS_EVENT_TABLE + " where " + USER_LOGIN + " = ?");
            pStmt.setString(1, userName);
            rs = pStmt.executeQuery();
            UserAccount.Builder builder = new UserAccount.Builder();
            while (rs.next()) {
                AccessEvent accessEvent = new AccessEvent(rs.getInt(ID), Status.valueOf(rs.getString(EVENT_ETAT)), rs.getDate(EVENT_DATE).toLocalDate(), rs.getString(EVENT_MESSAGE));
                accessEvents.add(accessEvent);
            }
        } finally {
            DBHelper.closeAll(con, pStmt, rs);
        }
        return accessEvents;
    }

    public void insert(AccessEvent accessEvent, String user) throws SQLException {
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            con = DBHelper.getConnection();
            pStmt = con.prepareStatement("INSERT INTO " + ACCESS_EVENT_TABLE + " (" + USER_LOGIN + ", " + EVENT_ETAT + ", " + EVENT_DATE + ", " + EVENT_MESSAGE + ") VALUES " +
                    "(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pStmt.setString(1, user);
            pStmt.setString(2, accessEvent.getStatus().toString());
            pStmt.setDate(3, Date.valueOf(accessEvent.getDate()));
            pStmt.setString(4, accessEvent.getMessage());
            pStmt.executeUpdate();

            try (ResultSet generatedKeys = pStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    accessEvent.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } finally {
            DBHelper.closeAll(con, pStmt, rs);
        }
    }

}
