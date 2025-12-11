package infiSecondTry.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CgiParameterController {

    private final String contentLength;
    private String sessionId;

    private final Map<String, String> params = new HashMap<>();
    private char[] contentFromBody = null;

    private static final String CLEANUP_TIMESTAMP_FILE = "Data/SessionIDS/lastCleanup.txt";
    private static final long CLEANUP_INTERVAL = 30 * 60 * 1000; // 30 Minuten

    private String getEnvironment(String key) {
        return System.getenv(key);
    }

    public CgiParameterController() throws IOException {

        //Session
        String cookieHeader = getEnvironment("HTTP_COOKIE");
        sessionId = SessionController.getSessionIdFromCookie(cookieHeader);

        boolean newSession = false;

        if (sessionId == null) {
            sessionId = SessionController.generateSessionId();
            newSession = true;
        }

        if (newSession) {
            System.out.println("Set-Cookie: SESSIONID=" + sessionId + "; Path=/; HttpOnly");
        }

        triggerCleanupIfNeeded();


        //Get
        String qryString;
        String qryStringEncoded = getEnvironment("QUERY_STRING");
        if (qryStringEncoded != null) {
            try {
                qryString = URLDecoder.decode(qryStringEncoded, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                qryString = qryStringEncoded;
            }
            parseParameters(qryString); // GET-Parameter einlesen
        }



        //Post
        String requestMethod = getEnvironment("REQUEST_METHOD");
        String contentType = getEnvironment("CONTENT_TYPE");
        contentLength = getEnvironment("CONTENT_LENGTH");

        // POST-Parameter (application/x-www-form-urlencoded)
        if ("POST".equalsIgnoreCase(requestMethod) &&
                contentLength != null && contentType != null &&
                contentType.contains("application/x-www-form-urlencoded")) {

            String body = getContentFromBodyAsString();
            try {
                body = URLDecoder.decode(body, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // Ignorieren
            }
            parseParameters(body);
        }
    }

    private void parseParameters(String query) {
        if (query == null || query.isEmpty()) return;

        String[] pairs = query.split("&");
        for (String p : pairs) {
            int idx = p.indexOf('=');
            if (idx > 0) {
                String key = p.substring(0, idx);
                String value = p.substring(idx + 1);
                params.put(key, value);
            }
        }
    }

    private void getContentFromBody(int len) {
        if (contentFromBody == null) {
            char[] body = new char[len];
            InputStreamReader reader = new InputStreamReader(System.in);
            try {
                reader.read(body, 0, len);
            } catch (Exception e) {
                e.printStackTrace();
            }
            contentFromBody = body;
        }
    }

    public char[] getContentFromBody() {
        if (contentFromBody == null && contentLength != null) {
            int len = Integer.parseInt(contentLength);
            getContentFromBody(len);
        }
        return contentFromBody;
    }

    public String getContentFromBodyAsString() {
        char[] body = getContentFromBody();
        if (body != null) {
            return new String(body);
        } else {
            return "";
        }
    }

    public String getParam(String name) {
        return params.get(name);
    }

    public String getSessionId() {
        return sessionId;
    }

    private static void triggerCleanupIfNeeded() {
        try {
            File tsFile = new File(CLEANUP_TIMESTAMP_FILE);

            long now = System.currentTimeMillis();
            long last = 0;

            if (tsFile.exists()) {
                String content = String.valueOf((Files.readAllLines(tsFile.toPath()))).trim();
                if (!content.isEmpty()) {
                    last = Long.parseLong(content);
                }
            }

            // Wenn mehr als 30 Minuten seit dem letzten Cleanup vergangen sind
            if (now - last > CLEANUP_INTERVAL) {

                // Cleanup starten
                Runtime.getRuntime().exec("Data/SessionIDS/SessionCleaner.bat");

                // neuen Zeitstempel speichern
                Files.write(tsFile.toPath(), Long.toString(now).getBytes());
            }

        } catch (Exception ignored) {
        }
    }
}
