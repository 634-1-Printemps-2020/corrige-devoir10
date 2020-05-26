package ch.hesge.cours634.security.ch.hesge.utils;

import ch.hesge.cours634.security.SecurityConfigurationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class PropertiesManager {

   private static Map<String, String> properties = new HashMap();

   private static PropertiesManager  INSTANCE= new PropertiesManager();

   private PropertiesManager(){
       loadProperties();
    }

    public static PropertiesManager getInstance(){
       return INSTANCE;
    }

    private void loadProperties() {
        File file = new File("application.properties");
        try (FileReader fr = new FileReader(file);
			 BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().length() == 0 || line.startsWith("#"))
                    continue;
                properties.put(extractKey(line), extractValue(line));
            }
        } catch (Exception exp) {
            throw new SecurityConfigurationException("Failed to read securoty store", exp);
        }
    }

     private String extractKey(String line) {
        return line.substring(0, line.indexOf("="));

    }

     private String extractValue(String line) {
        return line.substring(line.indexOf("=") + 1);
    }

    public  String getProperty(String key){
        return properties.get(key);
    }

}
