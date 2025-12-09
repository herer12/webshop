package infiSecondTry.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class SessionController {

    private static final String sessionIdSavingPlace = "Data/SessionIDS/SessionIDS";

    /** Generates a number that is put behind sessionIDSavingPlace to create a txt File
     * */
    public static String generateSessionId() {

        Random random = new Random();
        long randomBytes;

        do {
            randomBytes = random.nextInt(1000000);
        } while (!Files.notExists(Paths.get( sessionIdSavingPlace + randomBytes + ".txt")));
        return Long.toString(randomBytes);
    }

    /**Gets the Session id from the Cookie Header
      * @param cookieHeader from the cgi parameter
     * @return die Session Id or null if there is no Cookie Header
     */
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

    /**Saves the Data in the Session File of the Session Id
     * @param sessionId Session to save the Data to
     * @param data What should be saved
     */
    public static void save(String sessionId, String data) throws IOException {
        String textInFile = load(sessionId);
        if (textInFile != null){
            data = data + textInFile;
        }
        Files.write(
                Paths.get( sessionIdSavingPlace + sessionId+".txt"),
                data.getBytes(StandardCharsets.UTF_8)
        );
    }

    /**Loads the Content From the session File
     * @param sessionId Session Data to get the Content from
     * @return Content of session
     */
    public static String load(String sessionId) {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Paths.get( sessionIdSavingPlace + sessionId + ".txt"));
        }catch (Exception ignored){
            return null;
        }
        if (new String(bytes, StandardCharsets.UTF_8).isEmpty()) return null;
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String getValueAfterKeyword(String keyword,  String sessionId) {
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
