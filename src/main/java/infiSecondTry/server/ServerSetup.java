package infiSecondTry.server;

import com.sun.net.httpserver.HttpServer;
import infiSecondTry.database.CartRepository;
import infiSecondTry.database.ProductRepository;
import infiSecondTry.database.UserRepository;
import infiSecondTry.database.dummyData.DummyDataCartRepo;
import infiSecondTry.database.dummyData.DummyDataProductRepo;
import infiSecondTry.database.dummyData.DummyDataUserRepo;
import infiSecondTry.logic.ConfigHandling;
import infiSecondTry.service.CgiParameterController;

import java.net.InetSocketAddress;

public class ServerSetup {
    public static void main(String[] args) throws Exception {
        UserRepository userRepository;
        ProductRepository productRepository;
        CartRepository cartRepository;

        CgiParameterController cgiParameterController = new CgiParameterController();

        switch (ConfigHandling.getDataBaseScheme()) {
            case "MySQL":
                // TODO: MySQL-Implementierung
                throw new UnsupportedOperationException("MySQL noch nicht implementiert");
            case "PostgreSQL":
                // TODO: PostgreSQL-Implementierung
                throw new UnsupportedOperationException("PostgreSQL noch nicht implementiert");
            default:
                userRepository = new DummyDataUserRepo();
                productRepository = new DummyDataProductRepo();
                cartRepository = new DummyDataCartRepo();
                break;
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Router initialisieren (MIT productRepository!)
        Router.registerRoutes(server, productRepository);

        server.setExecutor(null);
        server.start();

        System.out.println("🚀 Shop-Server läuft auf http://localhost:8080");
        System.out.println("📄 Öffne im Browser: http://localhost:8080/");
    }
}