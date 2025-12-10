package infiSecondTry.logic;
import infiSecondTry.database.CartRepository;
import infiSecondTry.database.ProductRepository;
import infiSecondTry.database.UserRepository;
import infiSecondTry.service.CgiParameterController;
import infiSecondTry.service.HtmlTemplateHandler;
import infiSecondTry.model.Product;

public class LandingPage {

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private CgiParameterController cgiParameterController;

    LandingPage(UserRepository userRepository, ProductRepository productRepository, CartRepository cartRepository, CgiParameterController  cgiParameterController) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cgiParameterController = cgiParameterController;
    }

    void mainPage() throws Exception {

        HtmlTemplateHandler template = new HtmlTemplateHandler("landingPage.html");

        String query = cgiParameterController.getParam("search");
        if (query==null||query.isEmpty()){
            query = "";
        }
        String productHtml = buildProductHtml(productRepository.getProductsWithName(query));
        template.replace("PRODUCTS", productHtml);
        template.printHtml();

    }

    private String buildProductHtml(Product[] products) {
        StringBuilder sb = new StringBuilder();

        for (Product p : products) {
            sb.append("<div class='product-card'>")
                    .append("<div class='product-name'>").append(p.getName()).append("</div>")
                    .append("<div class='product-price'>").append(p.getPrice()).append(" €</div>")
                    .append("<form action='/cgi-bin/Amazon.bat?route=buy' method='POST'>")
                    .append("<input type='hidden' name='id' value='").append(p.getIdProduct()).append("'>")
                    .append("<button class='product-button' type='submit'>In den Warenkorb</button>")
                    .append("</form>")
                    .append("</div>");
        }

        return sb.toString();
    }


}