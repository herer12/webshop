package infiSecondTry.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import infiSecondTry.database.ProductRepository;
import infiSecondTry.model.Product;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ProductHandler implements HttpHandler {

    private final ProductRepository productRepository;
    private final Gson gson;

    public ProductHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.gson = new Gson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // CORS-Header für Browser
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");

        String method = exchange.getRequestMethod();

        // OPTIONS für CORS Preflight
        if (method.equals("OPTIONS")) {
            exchange.sendResponseHeaders(200, -1);
            return;
        }

        // GET /api/products - Alle Produkte abrufen
        if (method.equals("GET")) {
            handleGetProducts(exchange);
        }
        // POST /api/products - Neues Produkt hinzufügen
        else if (method.equals("POST")) {
            handleAddProduct(exchange);
        }
        // Andere Methoden
        else {
            sendJsonResponse(exchange, 405, "{\"error\": \"Methode nicht erlaubt\"}");
        }
    }

    /**
     * GET /api/products - Alle Produkte abrufen
     */
    private void handleGetProducts(HttpExchange exchange) throws IOException {
        try {
            Product[] products = productRepository.getAllProducts();
            String json = gson.toJson(products);
            sendJsonResponse(exchange, 200, json);
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(exchange, 500,
                    "{\"error\": \"Serverfehler beim Laden der Produkte\"}");
        }
    }

    /**
     * POST /api/products - Neues Produkt hinzufügen
     */
    private void handleAddProduct(HttpExchange exchange) throws IOException {
        try {
            // Request-Body lesen
            String body = exchange.getRequestBody().toString();

            // JSON zu Product-Objekt konvertieren
            Product product = gson.fromJson(body, Product.class);

            // Produkt hinzufügen
            boolean success = productRepository.addProduct(product);

            if (success) {
                sendJsonResponse(exchange, 201,
                        "{\"message\": \"Produkt erfolgreich hinzugefügt\"}");
            } else {
                sendJsonResponse(exchange, 400,
                        "{\"error\": \"Produkt konnte nicht hinzugefügt werden\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(exchange, 500,
                    "{\"error\": \"Serverfehler beim Hinzufügen des Produkts\"}");
        }
    }

    /**
     * Hilfsmethode zum Senden von JSON-Responses
     */
    private void sendJsonResponse(HttpExchange exchange, int statusCode, String json)
            throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}