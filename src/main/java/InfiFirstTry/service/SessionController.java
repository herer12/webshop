package InfiFirstTry.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Paths.get("DummyData/SessionIDS/" + SessionController.sessionIdSavingPlace + sessionId + ".txt"));
        }catch (Exception ignored){
            return null;
        }
        if (new String(bytes, StandardCharsets.UTF_8).isEmpty()) return null;
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String getValueAfterKeyword(String keyword,  String sessionId) throws IOException {
        String fileContent = SessionController.load(sessionId);
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
