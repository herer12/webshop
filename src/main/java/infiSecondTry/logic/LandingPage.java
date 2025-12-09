package infiSecondTry.logic;
import infiSecondTry.database.CartRepository;
import infiSecondTry.database.ProductRepository;
import infiSecondTry.database.UserRepository;
import infiSecondTry.service.HtmlTemplateHandler;
import infiSecondTry.model.Product;

public class LandingPage {

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private CartRepository cartRepository;


    LandingPage(UserRepository userRepository, ProductRepository productRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    void mainPage() throws Exception {

        HtmlTemplateHandler template = new HtmlTemplateHandler("landingPage.html");

        String productHtml = buildProductHtml(productRepository.getAllProducts());

        template.replace("PRODUCTS", productHtml);
        template.printHtml();

    }

    public static String buildProductHtml(Product[] products) {
        StringBuilder sb = new StringBuilder();

        for (Product p : products) {
            sb.append("<div class='product-card'>")
                    .append("<div class='product-name'>").append(p.getName()).append("</div>")
                    .append("<div class='product-price'>").append(p.getPrice()).append(" €</div>")
                    .append("<form action='/cgi-bin/buy.class' method='POST'>")
                    .append("<input type='hidden' name='id' value='").append(p.getIdProduct()).append("'>")
                    .append("<button class='product-button' type='submit'>In den Warenkorb</button>")
                    .append("</form>")
                    .append("</div>");
        }

        return sb.toString();
    }


}
