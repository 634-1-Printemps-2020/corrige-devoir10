package ch.hesge.cours634.security.ch.hesge.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileHelper {


    static public void writeToFile(String fileName, String data) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(data);
        printWriter.close();

    }

}
