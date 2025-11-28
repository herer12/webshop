package InfiFirstTry.service;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

public class SessionController {

    private static final Random random = new Random();
    private static String sessionIdSavingPlace = "SessionId";

    public static String generateSessionId() {
        long randomBytes;
        do {
            randomBytes = random.nextInt(1000000);
        } while (!Files.notExists(Paths.get("DummyData/SessionIDS/" + sessionIdSavingPlace + randomBytes + ".txt")));
        return Long.toString(randomBytes);
    }

    public static String getSessionIdFromCookie(String cookieHeader) {
        if (cookieHeader == null) return null;

        String[] cookies = cookieHeader.split(";");
        for (String c : cookies) {
            String[] pair = c.trim().split("=");
            if (pair.length == 2 && pair[0].equals("SESSIONID")) {
                return pair[1];
            }
        }
        return null;
    }


    public static void save(String sessionId, String data) throws IOException {
        Files.write(
                Paths.get("DummyData/SessionIDS/" + sessionIdSavingPlace + sessionId+".txt"),
                data.getBytes(StandardCharsets.UTF_8)   // <-- Java 8 braucht byte[]
        );
    }

    public static String load(String sessionId) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("DummyData/SessionIDS/" + sessionIdSavingPlace + sessionId+ ".txt"));
        if (new String(bytes, StandardCharsets.UTF_8).isEmpty()) return null;
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
