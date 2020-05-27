package ch.hesge.cours634.security.db;

import ch.hesge.cours634.security.UserAccount;
import ch.hesge.cours634.security.ch.hesge.utils.MyCSVReader;
import ch.hesge.cours634.security.exceptions.PersistenceException;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVImporter {

	Logger  logger = Logger.getLogger(CSVImporter.class);
	public void importCSV(String fileName) {
		logger.info("importing file " +fileName);
		UserAccountDAO dao = new UserAccountDAO();
		List<UserAccount>  users = new ArrayList<>();
		try {
			List<List<String>> lignes = MyCSVReader.readCSVFile(fileName);
			for ( List<String> ligne : lignes){
				users.add(buildUser(ligne));
			}
			dao.addUsers(users);
			logger.info("File "+ fileName +" has been imported, didn't encounter any technical issue during importing ");
			checkDataQuaulity(users);
		} catch ( SQLException | FileNotFoundException e) {
			throw new PersistenceException("Failed to import file  "+ fileName,e );
		}
}

	private void checkDataQuaulity(List<UserAccount> users) {
       // TODO
	}

	private UserAccount buildUser(List<String> line) {
		UserAccount user = new UserAccount();
		//name;password;roles;isActif;expirationDate
		user.setName(line.get(0));
		user.setPassword(line.get(1));
		user.setActif(Boolean.valueOf(line.get(3)));
		user.setExpirationDate(LocalDate.parse(line.get(4), DateTimeFormatter.ofPattern("dd.MM.yyyy")));
		return user;
	}

	public static void main(String[] args) {
		CSVImporter csvImporter = new CSVImporter();
		String fileName = "src/test/resources/users_bad.csv";
		//String fileName = "src/test/resources/users.csv";
		csvImporter.importCSV(fileName);
	}
}
