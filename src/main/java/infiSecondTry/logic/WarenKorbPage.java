package infiSecondTry.logic;

import infiSecondTry.database.CartRepository;
import infiSecondTry.database.ProductRepository;
import infiSecondTry.database.UserRepository;
import infiSecondTry.model.Cart;
import infiSecondTry.model.CartItem;
import infiSecondTry.model.Product;
import infiSecondTry.model.User;
import infiSecondTry.service.CgiParameterController;
import infiSecondTry.service.HtmlTemplateHandler;
import infiSecondTry.service.SessionController;

import java.sql.SQLException;
import java.util.LinkedList;

public class WarenKorbPage {
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private CgiParameterController cgiParameterController;

    WarenKorbPage(UserRepository userRepository, ProductRepository productRepository, CartRepository cartRepository, CgiParameterController cgiParameterController) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cgiParameterController = cgiParameterController;
    }

    void paymentSite() throws Exception {
        HtmlTemplateHandler template = new HtmlTemplateHandler("paymentPage.html");
        String userId = SessionController.loadValue(cgiParameterController.getSessionId(), "UserId");

        if (userId == null) {
            LoginPage loginPage = new LoginPage(userRepository, productRepository, cartRepository, cgiParameterController);
            loginPage.loginSite();
            return;
        }

        Cart cart = cartRepository.getCartForSpecifiedUser(Integer.parseInt(userId));

        String productHtml = buildPaymentInfo(cart);
        template.replace("PRODUCTS", productHtml);
        template.printHtml();

        User user = userRepository.getUserWithId(Integer.parseInt(userId));
        user.addMoneyToMoneySpent(cart.calcTotalPrice());
        userRepository.updateUser(user);
        try {
            for (CartItem item : cart.getProductsInShoppingCart()) {
                for (int i = 0; i < item.getQuantity(); i++){
                    cartRepository.removeProductFromCart(cart.getIdCart(),item.getProduct().getIdProduct());
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

    private String buildPaymentInfo(Cart cart) {
        StringBuilder paymentInfo = new StringBuilder();
        paymentInfo.append("<p>Vielen Dank</p>");
        paymentInfo.append("<p>Ihre Bestellung wurde erfolgreich abgeschlossen.</p>");
        paymentInfo.append("<p>Es hat</p>").append(cart.calcTotalPrice()).append("<p>€</p>");

        return paymentInfo.toString();
    }

    void deleteProduct() throws Exception {
        String userId = SessionController.loadValue(cgiParameterController.getSessionId(), "UserId");

        cartRepository.removeProductFromCart(Integer.parseInt(userId), Integer.parseInt(cgiParameterController.getParam("id")));

        cartSite();
    }


    public void cartSite() throws Exception {
        HtmlTemplateHandler template = new HtmlTemplateHandler("cartPage.html");
        String userId = SessionController.loadValue(cgiParameterController.getSessionId(), "UserId");

        if (userId == null) {
            LoginPage loginPage = new LoginPage(userRepository, productRepository, cartRepository, cgiParameterController);
            loginPage.loginSite();
        }

        String productHtml = buildProductHtml(cartRepository.getCartForSpecifiedUser(Integer.parseInt(userId)));
        template.replace("PRODUCTS", productHtml);
        template.printHtml();

    }

    private String buildProductHtml(Cart cart) {
        LinkedList<CartItem> items = cart.getProductsInShoppingCart();
        StringBuilder sb = new StringBuilder();

        for (CartItem item : items) {
            Product p = item.getProduct();   // Produkt aus CartItem holen
            int quantity = item.getQuantity(); // Anzahl holen

            sb.append("<div class='product-card'>")
                    .append("<div class='product-name'>").append(p.getName()).append("</div>")
                    .append("<div class='product-price'>").append(p.getPrice()*quantity).append(" €</div>")
                    .append("<div class='product-quantity'>Menge: ").append(quantity).append("</div>")

                    .append("<form action='/cgi-bin/Amazon.bat?route=delete' method='POST'>")
                    .append("<input type='hidden' name='id' value='").append(p.getIdProduct()).append("'>")
                    .append("<button class='product-button' type='submit'>Entfernen</button>")
                    .append("</form>")

                    .append("</div>");
        }
        sb.append(cart.calcTotalPrice()).append("€");
        sb.append("<br>");

        return sb.toString();


    }
}
