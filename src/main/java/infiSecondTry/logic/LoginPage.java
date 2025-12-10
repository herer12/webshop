package infiSecondTry.logic;

import infiSecondTry.database.CartRepository;
import infiSecondTry.database.ProductRepository;
import infiSecondTry.database.UserRepository;
import infiSecondTry.model.User;
import infiSecondTry.service.CgiParameterController;
import infiSecondTry.service.HtmlTemplateHandler;
import infiSecondTry.service.SessionController;

public class LoginPage {
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private CgiParameterController cgiParameterController;

    LoginPage(UserRepository userRepository, ProductRepository productRepository, CartRepository cartRepository, CgiParameterController cgiParameterController) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cgiParameterController = cgiParameterController;
    }

    private void checkUserId(){

    }

    void loginSite() throws Exception {
        if (SessionController.getValueAfterKeyword("UserId", cgiParameterController.getSessionId()) == null){
            String userId = cgiParameterController.getParam("userId");
            if (userId == null || userId.isEmpty()||userId.equals("0")) {
                HtmlTemplateHandler htmlTemplateHandler = new HtmlTemplateHandler("login.html");
                htmlTemplateHandler.printHtml();
                return;
            }
            User user = userRepository.getUserWithId(Integer.parseInt(userId));
            if (user == null) {
                HtmlTemplateHandler htmlTemplateHandler = new HtmlTemplateHandler("loginWrong.html");
                htmlTemplateHandler.printHtml();
                return;
            }
            SessionController.save(cgiParameterController.getSessionId(),"UserId:"+userId);
            LandingPage landingPage = new LandingPage(userRepository,productRepository,cartRepository,cgiParameterController);
            landingPage.mainPage();
            return;
        }
        //Todo: Userverwaltung(Konto,Ausloggen usw.)
        LandingPage landingPage = new LandingPage(userRepository,productRepository,cartRepository,cgiParameterController);
        landingPage.mainPage();

    }

    private String buildProductHtml() {
        StringBuilder sb = new StringBuilder();
        return null;

    }
}
