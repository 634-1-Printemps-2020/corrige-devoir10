package ch.hesge.cours634.security.db.magic;

import ch.hesge.cours634.security.AccountManager;
import ch.hesge.cours634.security.db.DBHelper;
import ch.hesge.cours634.security.exceptions.PersistenceException;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MagicPersister {

    private static Logger logger = Logger.getLogger(MagicPersister.class);

    public static void persist(Object object) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");

        String tableName = getTableName(object);

        sb.append(tableName).append(" (");

        Field[] declaredFields = object.getClass().getDeclaredFields();

        List<Field> annotatedFields = new ArrayList<>();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(DBCol.class)) {
                annotatedFields.add(field);
                String column = field.getAnnotation(DBCol.class).column();
                sb.append(column).append(",");
            }
        }

        sb.deleteCharAt(sb.length() - 1).append(") VALUES (?"); //  deleting last char because it has a ,

        for (int i = 0; i < annotatedFields.size() - 1; i++) {
            sb.append(", ?");
        }

        sb.append(")");

        logger.info("Generated SQL query : " + sb.toString());

        try {
            prepareAndExecuteQuery(object, annotatedFields, sb.toString());
        } catch (IllegalAccessException e) {
        }

    }

    private static String getTableName(Object object) {
        return object.getClass().getAnnotation(DBTable.class).table();
    }

    private static void prepareAndExecuteQuery(Object object, List<Field> annotatedFields, String query) throws SQLException, IllegalAccessException {
        Connection con = null;
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        try {
            con = DBHelper.getConnection();
            pStmt = con.prepareStatement(query);

            for (int i = 1; i <= annotatedFields.size(); i++) {
                setValueQuery(object, i, annotatedFields.get(i - 1), pStmt);
            }

            pStmt.executeUpdate();
        } finally {
            DBHelper.closeAll(con, pStmt, rs);
        }
    }

    private static void setValueQuery(Object object, int index, Field field, PreparedStatement pStmt) throws IllegalAccessException, SQLException {
        Class<?> type = field.getType();
        field.setAccessible(true);
        if (type == String.class) {
            pStmt.setString(index, String.valueOf(field.get(object)));
        } else if (type == boolean.class || type == Boolean.class) {
            pStmt.setBoolean(index, field.getBoolean(object));
        } else if (type == LocalDate.class) {
            LocalDate localDate = (LocalDate) field.get(object);
            pStmt.setDate(index, Date.valueOf(localDate));
        } else if (type == List.class) {
            List<String> list = (List<String>) field.get(object); // List<Object> aurait été plus correct mais il aurait fallu modifier la construction du String
            pStmt.setString(index, String.join(",", list));
        } else {
            throw new PersistenceException("unsupported type");
        }
    }


}
