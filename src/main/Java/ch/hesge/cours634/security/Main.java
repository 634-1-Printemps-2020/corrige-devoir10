package ch.hesge.cours634.security;

import ch.hesge.cours634.security.db.magic.MagicPersister;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws SQLException, IllegalAccessException {
        AccountManager accountManager = new AccountManager();
        UserAccount account = accountManager.createAccount("Pierre", "Password123", Arrays.asList("Student", "Assistant"), buildLocalDate("01.01.2025"));

        MagicPersister.persist(account);
    }

    private static LocalDate buildLocalDate(String s) {
        return LocalDate.parse(s, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}
