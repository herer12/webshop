package infiSecondTry.logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

public class ConfigHandling {

    static File databaseFilePfad = new File("Config/database_configuration.txt");

    public static String getDataBaseScheme(){
        if(!databaseFilePfad.exists()){
            return "";
        }
        try {
            return (getValueAfterKeyword("DatabaseType"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getValueAfterKeyword(String keyword) throws IOException {
        String fileContent = String.valueOf(Files.readAllLines(databaseFilePfad.toPath()));
        String result;
        if (fileContent == null) {
            return  null;
        }
        fileContent = fileContent.toLowerCase();
        keyword = keyword.toLowerCase()+":";
        int startIndex = fileContent.indexOf(keyword);

        if (startIndex != -1) {
            startIndex += keyword.length();
            int endIndex = fileContent.indexOf(";", startIndex);
            if (endIndex != -1) {
                result = fileContent.substring(startIndex, endIndex);
            } else {
                result = fileContent.substring(startIndex);
            }
        }else {
            return null;
        }
        return result;
    }
}
