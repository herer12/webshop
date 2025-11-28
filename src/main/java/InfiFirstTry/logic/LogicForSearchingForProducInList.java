package InfiFirstTry.logic;

import InfiFirstTry.database.ProductRepository;
import InfiFirstTry.database.UserRepository;
import InfiFirstTry.database.dummyDataRepo.DummyDataProductRepo;
import InfiFirstTry.database.dummyDataRepo.DummyDataUserRepo;
import InfiFirstTry.model.Product;
import InfiFirstTry.model.User;
import InfiFirstTry.service.LoginController;
import InfiFirstTry.service.ProductControllerOutput;
import InfiFirstTry.service.ProductControllerInput;
import InfiFirstTry.service.SessionController;
import InfiFirstTry.webapp.CgiParameterController;
import InfiFirstTry.webapp.HtmlTemplateHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class LogicForSearchingForProducInList {
    CgiParameterController cgiParameterController;
    ProductRepository productRepository ;
    UserRepository userRepository ;
    String configState ;

    public static String loadConfigFile() throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(System.getenv("CONFIG_PATH")+"/database_editing_file.txt"));
        if (new String(bytes, StandardCharsets.UTF_8).isEmpty()) return null;
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public String getDatabaseType(){
        assert configState != null;
        int startIndex = configState.indexOf("DatabaseType:");
        if (startIndex != -1) {
            startIndex += "DatabaseType:".length();
            int endIndex = configState.indexOf(";", startIndex);
            if (endIndex != -1) {
                return configState.substring(startIndex, endIndex);
            } else {
                return configState.substring(startIndex);
            }
        }else {
            return null;
        }
    }

    public LogicForSearchingForProducInList(CgiParameterController cgiParameterController ) throws IOException {
        this.cgiParameterController = cgiParameterController;
        if (Objects.equals(System.getenv("CONFIG_PATH"), null)) {
            return;
        }
        configState = loadConfigFile();
        switch (getDatabaseType()){
            case "MySQL":

                break;
            default:
                userRepository= new DummyDataUserRepo();
                productRepository= new DummyDataProductRepo();
                break;
        }

    }

    public void getAccessesReason() throws Exception {

        String route = cgiParameterController.getParam("route");

        if (route == null || route.isEmpty()) {
            route = "index"; // Standard route
        }

        switch (route) {
            case "Login":
                login();
            case "index":
                printHTML("Gast");
                break;
            case "products":
                sendingJSON(productRepository.getAllProducts());
                break;
            case "buy":
                AddingProductToCart addingProductToCart = new AddingProductToCart(cgiParameterController, productRepository, userRepository);
                addingProductToCart.start();
                break;
            case "Keyword":
                productSearch();
                break;
            default:
                printHTML("F");
        }
    }
    public void productSearch() throws Exception {
        ProductControllerInput productControllerInput = new ProductControllerInput(cgiParameterController);
        sendingJSON( productRepository.getProductsWithName( productControllerInput.getSearchKeyword() ));
    }

    public void sendingJSON(Product[] data) {
        System.out.println("Content-Type: application/json; charset=UTF-8");
        System.out.println();
        System.out.println(ProductControllerOutput.handleProducts(data));
    }

    public void printHTML( String i) throws Exception {
        System.out.println("Content-Type: text/html; charset=UTF-8");
        System.out.println();
        HtmlTemplateHandler tpl = new HtmlTemplateHandler("index.html");
        tpl.replace("USER", i);
        tpl.replace("MESSAGE", "Willkommen in der App!");
        tpl.replace("Session", cgiParameterController.getSessionId());
        tpl.printHtml();
    }

    void login() throws IOException {
        if (getUser() != null) {
            return;
        }
        LoginController loginController = new LoginController(cgiParameterController);
        User user = userRepository.getUserWithId(Integer.parseInt(loginController.getIdUser()));
        if (user == null){
            return;
        }
        SessionController.save(cgiParameterController.getSessionId(),"UserId:"+user.getIdUser() );
    }

    User getUser() throws IOException {
        String sessionId = SessionController.load(cgiParameterController.getSessionId());
        String result;
        if (sessionId == null) {
            return  null;
        }
        int startIndex = sessionId.indexOf("UserId:");

        if (startIndex != -1) {
            startIndex += "UserId:".length();
            int endIndex = sessionId.indexOf(";", startIndex);
            if (endIndex != -1) {
                result = sessionId.substring(startIndex, endIndex);
            } else {
                result = sessionId.substring(startIndex);
            }
        }else {
            return null;
        }
        return userRepository.getUserWithId(Integer.parseInt(result));
    }


}