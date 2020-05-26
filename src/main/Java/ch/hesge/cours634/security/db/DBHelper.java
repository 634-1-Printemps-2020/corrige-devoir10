package ch.hesge.cours634.security.db;

import ch.hesge.cours634.security.ch.hesge.utils.PropertiesManager;
import ch.hesge.cours634.security.exceptions.PersistenceException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DBHelper {

    private static Logger logger = Logger.getLogger(DBHelper.class);
    static boolean dbH2initialized=false;

    public static Connection getConnection() {
        Connection con = null;
        try {
            PropertiesManager propertiesManager = PropertiesManager.getInstance();
            String url= propertiesManager.getProperty("db.url");
            String login= propertiesManager.getProperty("db.login");
            String password= propertiesManager.getProperty("db.password");
            String driver= propertiesManager.getProperty("db.driver");
            if(url.contains("h2") && !dbH2initialized) {
                url = url + ";INIT=runscript from './userAccount_H2.sql'\\;runscript from './insertInitialData_H2.sql';DB_CLOSE_DELAY=-1";
                dbH2initialized=true;
            }
            Class.forName(driver);
            return DriverManager.getConnection(url, login,password);
        } catch (Exception e) {
           throw new PersistenceException("Failed to obtain connection ", e);
        }
    }
    public static void closeAll(Connection con, Statement stmt, ResultSet rs) {
        try {
            if (stmt != null) stmt.close();
            if (rs != null) rs.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            logger.error(e);
            throw new PersistenceException("Failed to close JDBC resources", e);
        }
    }

    public static java.util.Date toDate(String dateStr) throws ParseException {
        return new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);
    }

    public static String dateToString(java.util.Date date) throws ParseException {
        return new SimpleDateFormat("dd.MM.yyyy").format(date);
    }

    public static java.sql.Date toSqlDate(java.util.Date d){
        return new java.sql.Date(d.getTime());
    }

    public  static java.util.Date fromSqlDate(java.sql.Date d){
        return new java.util.Date(d.getTime());
    }
}
