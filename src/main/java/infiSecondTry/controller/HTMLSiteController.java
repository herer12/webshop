package infiSecondTry.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HTMLSiteController implements HttpHandler {

    private static final String WEB_ROOT = "WebFiles";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        // Root -> index.html
        if (path.equals("/")) {
            path = "/index.html";
        }
        if (path.equals("/login")) {
            path = "/login.html";
        }


        try {
            // Datei laden
            Path filePath = Paths.get(WEB_ROOT + path);
            System.out.println(filePath);
            if (!Files.exists(filePath)) {
                System.out.println("Datei verhanden");
            }
            byte[] content = Files.readAllBytes(filePath);

            // Content-Type bestimmen
            String contentType = getContentType(path);
            exchange.getResponseHeaders().set("Content-Type", contentType);

            // Antwort senden
            exchange.sendResponseHeaders(200, content.length);
            OutputStream os = exchange.getResponseBody();
            os.write(content);
            os.close();

        } catch (Exception e) {
            // 404 - Datei nicht gefunden
            String response = "<!DOCTYPE html>" +
                    "<html><head><title>404</title></head>" +
                    "<body style='font-family: Arial; text-align: center; padding: 50px;'>" +
                    "<h1>404 - Seite nicht gefunden</h1>" +
                    "<p>Die Datei <code>" + path + "</code> existiert nicht.</p>" +
                    "<a href='/'>Zur Startseite</a>" +
                    "</body></html>";

            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(404, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    /**
     * Content-Type basierend auf Dateiendung bestimmen
     */
    private String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html; charset=UTF-8";
        if (path.endsWith(".css"))  return "text/css; charset=UTF-8";
        if (path.endsWith(".js"))   return "application/javascript; charset=UTF-8";
        if (path.endsWith(".json")) return "application/json; charset=UTF-8";
        if (path.endsWith(".png"))  return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".gif"))  return "image/gif";
        if (path.endsWith(".svg"))  return "image/svg+xml";
        return "text/plain; charset=UTF-8";
    }
}