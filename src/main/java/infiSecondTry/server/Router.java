package infiSecondTry.server;

import com.sun.net.httpserver.HttpServer;
import infiSecondTry.controller.ProductHandler;
import infiSecondTry.controller.HTMLSiteController;
import infiSecondTry.database.CartRepository;
import infiSecondTry.database.ProductRepository;
import infiSecondTry.database.UserRepository;
import infiSecondTry.service.CgiParameterController;

public class Router {

    public static void registerRoutes(HttpServer server, ProductRepository productRepository, UserRepository userRepository, CartRepository cartRepository, CgiParameterController cgiParameterController) {

        // Statische HTML-Dateien (index.html, CSS, JS)
        server.createContext("/", new HTMLSiteController());

        // Produkt-API
        server.createContext("/api/products", new ProductHandler(productRepository));

        // Health-Check
        server.createContext("/api/health", exchange -> {
            String msg = "OK";
            exchange.sendResponseHeaders(200, msg.length());
            exchange.getResponseBody().write(msg.getBytes());
            exchange.close();
        });

        System.out.println("Routen registriert:");
        System.out.println("GET  /               -> HTML-Seite");
        System.out.println("GET  /api/products   -> Alle Produkte");
        System.out.println("GET  /api/health     -> Health-Check");
    }
}