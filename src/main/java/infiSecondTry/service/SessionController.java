package infiSecondTry.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.SecureRandom;
import java.util.*;

public class SessionController {

    private static final String SESSION_DIR = "Data/SessionIDS/";
    private static final SecureRandom RANDOM = new SecureRandom();


    static {
        try {
            Files.createDirectories(Paths.get(SESSION_DIR));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateSessionId() {
        String id;
        do {
            id = Long.toHexString(RANDOM.nextLong());
        } while (Files.exists(Paths.get(SESSION_DIR, id + ".txt")));
        return id;
    }

    public static String getSessionIdFromCookie(String cookieHeader) {
        if (cookieHeader == null) return null;

        for (String c : cookieHeader.split(";")) {
            String[] pair = c.trim().split("=");
            if (pair.length == 2 && pair[0].equals("SESSIONID")) {
                return pair[1].trim();
            }
        }
        return null;
    }

    public static Map<String, String> loadMap(String sessionId) throws IOException {
        Path file = Paths.get(SESSION_DIR, sessionId + ".txt");
        if (!Files.exists(file)) return new HashMap<>();

        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        Map<String, String> map = new HashMap<>();

        for (String line : lines) {
            String[] parts = line.split("=", 2);
            if (parts.length == 2) {
                map.put(parts[0], parts[1]);
            }
        }
        return map;
    }

    private static void saveMap(String sessionId, Map<String, String> map) throws IOException {
        Path file = Paths.get(SESSION_DIR, sessionId + ".txt");
        List<String> data = new ArrayList<>();

        for (Map.Entry<String, String> e : map.entrySet()) {
            data.add(e.getKey() + "=" + e.getValue());
        }

        Files.write(file, data, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static void save(String sessionId, String key, String value) throws IOException {
        Map<String, String> map = loadMap(sessionId);
        map.put(key, value);
        saveMap(sessionId, map);
    }

    public static String loadValue(String sessionId, String key) throws IOException {
        return loadMap(sessionId).get(key);
    }

    public static void delete(String sessionId, String key) throws IOException {
        Map<String, String> map = loadMap(sessionId);
        map.remove(key);
        saveMap(sessionId, map);
    }
}
