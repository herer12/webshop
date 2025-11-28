package InfiFirstTry.logic;

import InfiFirstTry.database.CartRepository;
import InfiFirstTry.database.ProductRepository;
import InfiFirstTry.database.UserRepository;
import InfiFirstTry.database.dummyDataRepo.DummyDataCartRepo;
import InfiFirstTry.model.Product;
import InfiFirstTry.model.User;
import InfiFirstTry.service.AddingProductInCartController;
import InfiFirstTry.webapp.CgiParameterController;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class AddingProductToCart {
    private ProductRepository productRepository ;
    private UserRepository userRepository;
    private CgiParameterController cgiParameterController;
    AddingProductToCart(CgiParameterController controller,  ProductRepository productRepository, UserRepository userRepository) {
        cgiParameterController = controller;
        this.productRepository =productRepository;
        this.userRepository = userRepository;
    }
    public void start() throws IOException {
        AddingProductInCartController addingProductInCartController = new AddingProductInCartController(cgiParameterController);
        String id =addingProductInCartController.getIdProduct();



        Product p = productRepository.getProductWithId(Integer.parseInt(id));
        User u =userRepository.getUserWithId(1);

        CartRepository cartRepository = new DummyDataCartRepo();

        cartRepository.addProductToCart(1, Integer.parseInt(id));

        System.out.println("Content-Type: application/json; charset=UTF-8");
        System.out.println();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(cartRepository.getCartForSpecifiedUser(1));
        System.out.println(json);



    }
}
