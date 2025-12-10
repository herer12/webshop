package infiSecondTry.logic;

import infiSecondTry.database.CartRepository;
import infiSecondTry.database.ProductRepository;
import infiSecondTry.database.UserRepository;
import infiSecondTry.database.dummyData.DummyDataCartRepo;
import infiSecondTry.database.dummyData.DummyDataProductRepo;
import infiSecondTry.database.dummyData.DummyDataUserRepo;
import infiSecondTry.service.CgiParameterController;

public class Main {
    public static void main(String[] args) throws Exception {

        UserRepository userRepository;
        ProductRepository productRepository;
        CartRepository cartRepository;

        CgiParameterController cgiParameterController = new CgiParameterController();

        switch (ConfigHandling.getDataBaseScheme()) {
//            case "MySQL":
//                break;
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

        switch (route) {
            case "buy":
                AddingProductToCart addingProductToCart= new AddingProductToCart(userRepository,productRepository,cartRepository,cgiParameterController);
                addingProductToCart.initatedAddingProductToCart();
                break;
            case "login":
                LoginPage loginPage = new LoginPage(userRepository, productRepository, cartRepository, cgiParameterController);
                loginPage.loginSite();
                break;
            default:
                LandingPage landingPage = new LandingPage(userRepository,productRepository,cartRepository,cgiParameterController);
                landingPage.mainPage();
                break;
        }








    }
}
