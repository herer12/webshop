package infiSecondTry.logic;

import infiSecondTry.database.CartRepository;
import infiSecondTry.database.ProductRepository;
import infiSecondTry.database.UserRepository;
import infiSecondTry.service.CgiParameterController;
import infiSecondTry.service.HtmlTemplateHandler;
import infiSecondTry.service.SessionController;

import java.io.IOException;

public class ProfilPage {
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private CgiParameterController cgiParameterController;

    ProfilPage(UserRepository userRepository, ProductRepository productRepository, CartRepository cartRepository, CgiParameterController cgiParameterController) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cgiParameterController = cgiParameterController;
    }

    void profilSite() throws Exception {
        HtmlTemplateHandler htmlTemplateHandler = new HtmlTemplateHandler("profil.html");
        htmlTemplateHandler.replace("USERID",buildProfilHtml());
        htmlTemplateHandler.printHtml();
    }

    void logout() throws Exception {
        HtmlTemplateHandler htmlTemplateHandler = new HtmlTemplateHandler("logout.html");
        SessionController.delete( cgiParameterController.getSessionId(),"UserId");
        htmlTemplateHandler.printHtml();
    }

    private String buildProfilHtml() throws IOException {
        StringBuilder sb = new StringBuilder();
        String userID = SessionController.loadValue(cgiParameterController.getSessionId(),"UserId");
        sb.append("<p id='user-id'>").append(userID).append("</p>");
        return sb.toString();
    }

}
