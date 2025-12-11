package infiSecondTry.logic;

import infiSecondTry.database.CartRepository;
import infiSecondTry.database.ProductRepository;
import infiSecondTry.database.UserRepository;
import infiSecondTry.service.CgiParameterController;
import infiSecondTry.service.SessionController;

public class AddingProductToCart {
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private CgiParameterController cgiParameterController;
    AddingProductToCart(UserRepository userRepository, ProductRepository productRepository, CartRepository cartRepository, CgiParameterController cgiParameterController) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cgiParameterController = cgiParameterController;
    }

    void initatedAddingProductToCart() throws Exception {
        String sessionId = cgiParameterController.getSessionId();
        String userID = SessionController.loadValue(sessionId, "UserId" );

        if(userID==null||userID.isEmpty()){
            LoginPage loginPage = new LoginPage(userRepository,productRepository,cartRepository,cgiParameterController);
            loginPage.loginSite();
            return;
        }

        String prductID = cgiParameterController.getParam("id");

        if (prductID==null||prductID.isEmpty()){
            return;
        }

        cartRepository.addProductToCart( Integer.parseInt(userID) , Integer.parseInt(prductID) );

        LandingPage landingPage = new LandingPage(userRepository,productRepository,cartRepository,cgiParameterController);
        landingPage.mainPage();


    }
}
