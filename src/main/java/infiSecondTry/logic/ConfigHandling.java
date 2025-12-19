package infiSecondTry.logic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ConfigHandling {

    private static File DATABASE_FILE_PATH = new File("Config/database_configuration.txt");

    /**
     * Liest den DatabaseType aus der Konfigurationsdatei
     * @return DatabaseType (z.B. "mysql") oder leerer String wenn nicht gefunden
     */
    public static String getDataBaseScheme() {
        if (!DATABASE_FILE_PATH.exists()) {
            DATABASE_FILE_PATH = new File("database_configuration.txt");
        }

        try {
            String value = getValueAfterKeyword("DatabaseType");
            return value != null ? value.trim() : "";
        } catch (IOException e) {
            System.err.println("Fehler beim Lesen der Config-Datei: " + e.getMessage());
            return "";
        }
    }

    /**
     * Sucht nach einem Keyword in der Config-Datei und gibt den Wert zurück
     * Format: DatabaseType:MySQL
     *
     * @param keyword Der gesuchte Schlüssel (case-insensitive)
     * @return Der Wert oder null wenn nicht gefunden
     */
    private static String getValueAfterKeyword(String keyword) throws IOException {
        List<String> lines = Files.readAllLines(DATABASE_FILE_PATH.toPath());

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            // Suche nach "keyword:"
            String lowerLine = line.toLowerCase();
            String lowerKeyword = keyword.toLowerCase() + ":";

            if (lowerLine.startsWith(lowerKeyword)) {
                // Extrahiere Wert nach dem ":"
                int colonIndex = line.indexOf(":");
                if (colonIndex != -1 && colonIndex < line.length() - 1) {
                    return line.substring(colonIndex + 1).trim();
                }
            }
        }

        return null; // Keyword nicht gefunden
    }
}