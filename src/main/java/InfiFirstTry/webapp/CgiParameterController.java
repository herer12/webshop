package InfiFirstTry.webapp;

import InfiFirstTry.service.SessionController;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static InfiFirstTry.service.SessionController.getSessionIdFromCookie;

public class CgiParameterController {

    private String qryString;
    private String remoteAddr;
    private String userAgent;
    private String referer;
    private String host;
    private String requestMethod;
    private String contentType;
    private String contentLength;
    private String pathInfo;

    public String getSessionId() {
        return sessionId;
    }

    private String sessionId;

    private Map<String, String> params = new HashMap<>();
    private char[] contentFromBody = null;

    private String getEnvironment(String key) {
        return System.getenv(key);
    }

    public CgiParameterController() throws IOException {

        qryString = null;
        String qryStringEncoded = getEnvironment("QUERY_STRING");
        if (qryStringEncoded != null) {
            try {
                qryString = URLDecoder.decode(qryStringEncoded, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                qryString = qryStringEncoded;
            }
            parseParameters(qryString); // GET-Parameter einlesen
        }

        String cookieHeader = System.getenv("HTTP_COOKIE");

        sessionId = getSessionIdFromCookie(cookieHeader);
        if (sessionId == null) {
            sessionId = SessionController.generateSessionId();
            System.out.println("Set-Cookie: SESSIONID=" + sessionId + "; Path=/; HttpOnly");
            SessionController.save(sessionId, "route=index");
        }

        remoteAddr = getEnvironment("REMOTE_ADDR");
        userAgent = getEnvironment("HTTP_USER_AGENT");
        referer = getEnvironment("HTTP_REFERER");
        host = getEnvironment("HTTP_HOST");
        requestMethod = getEnvironment("REQUEST_METHOD");
        contentType = getEnvironment("CONTENT_TYPE");
        contentLength = getEnvironment("CONTENT_LENGTH");
        pathInfo = getEnvironment("PATH_INFO");

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


    // ========= ALLE GETTER BLEIBEN ==========

    public String getQryString() {
        return qryString;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getReferer() {
        return referer;
    }

    public String getHost() {
        return host;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContentLength() {
        return contentLength;
    }

    public String getPathInfo() { return pathInfo; }
}
