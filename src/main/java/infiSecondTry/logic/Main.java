package infiSecondTry.logic;

import infiSecondTry.database.CartRepository;
import infiSecondTry.database.ProductRepository;
import infiSecondTry.database.UserRepository;
import infiSecondTry.database.dummyData.DummyDataCartRepo;
import infiSecondTry.database.dummyData.DummyDataProductRepo;
import infiSecondTry.database.dummyData.DummyDataUserRepo;
import infiSecondTry.database.mySQL.MySqlCartRepo;
import infiSecondTry.database.mySQL.MySqlProductRepo;
import infiSecondTry.database.mySQL.MySqlUserRepo;
import infiSecondTry.service.CgiParameterController;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {

        UserRepository userRepository;
        ProductRepository productRepository;
        CartRepository cartRepository;

        CgiParameterController cgiParameterController = new CgiParameterController();

        switch (ConfigHandling.getDataBaseScheme().trim()) {
            case "MySQL":
                userRepository = new MySqlUserRepo();
                productRepository = new MySqlProductRepo();
                cartRepository = new MySqlCartRepo();
                break;
//            case "PostgreSQL":
//                break;
            default:
                userRepository = new DummyDataUserRepo();
                productRepository = new DummyDataProductRepo();
                cartRepository = new DummyDataCartRepo();
                break;
        }

        String route = cgiParameterController.getParam("route");
        if (route == null || route.isEmpty()) {
            route = "index";
        }

        WarenKorbPage warenKorbPage = new WarenKorbPage(userRepository, productRepository, cartRepository, cgiParameterController);

        switch (route) {
            case "logout":
                ProfilPage profilPage = new ProfilPage(userRepository,productRepository,cartRepository,cgiParameterController);
                profilPage.logout();
                break;
            case "buy":
                AddingProductToCart addingProductToCart= new AddingProductToCart(userRepository,productRepository,cartRepository,cgiParameterController);
                addingProductToCart.initatedAddingProductToCart();
                break;
            case "login":
                LoginPage loginPage = new LoginPage(userRepository, productRepository, cartRepository, cgiParameterController);
                loginPage.loginSite();
                break;
            case "cart":
                warenKorbPage.cartSite();
                break;
            case "payment":
                warenKorbPage.paymentSite();
                break;
            case "delete":
                warenKorbPage.deleteProduct();
                break;
            default:
                LandingPage landingPage = new LandingPage(userRepository,productRepository,cartRepository,cgiParameterController);
                landingPage.mainPage();
                break;
        }








    }
}
