package InfiFirstTry.database.dummyDataRepo;

import InfiFirstTry.database.CartRepository;
import InfiFirstTry.database.connectionClasses.DummyDataConnection;
import InfiFirstTry.model.Cart;
import InfiFirstTry.model.Product;
import InfiFirstTry.model.User;

import java.util.LinkedList;

public class DummyDataCartRepo implements CartRepository {
    DummyDataConnection connection = new DummyDataConnection();
    String cartItem = "carts.json";


    @Override
    public Cart getCartForSpecifiedUser(int userID) {
        DummyDataUserRepo dummyDataUserRepo = new DummyDataUserRepo();
        User[] users =dummyDataUserRepo.getAllUsers();
        for (User user : users) {
            if (user.getIdUser() == userID){
                LinkedList<Cart> carts = connection.getList("carts.json",Cart.class);
                for (Cart cart : carts){
                    if (cart.getIdCart() == userID){
                        return cart;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean addProductToCart(int cartID, int productID) {
        DummyDataProductRepo dummyDataProductRepo = new DummyDataProductRepo();
        Product[] products = dummyDataProductRepo.getAllProducts();
        for (Product product : products) {
            if (product.getIdProduct() == productID){
                LinkedList <Cart> carts = connection.getList("carts.json",Cart.class);
                for (Cart cart : carts){
                    if (cart.getIdCart() == cartID){
                        cart.addProduct(product);
                        carts.set(carts.indexOf(cart), cart);
                    }
                }
                DummyDataConnection.saveChanges(carts, cartItem);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeProductFromCart(int cartID, int productID) {
        DummyDataProductRepo dummyDataProductRepo = new DummyDataProductRepo();
        int index = 0;
        Product[] products = dummyDataProductRepo.getAllProducts();
        for (Product product : products) {
            if (product.getIdProduct() == productID){
                LinkedList <Cart> carts = connection.getList("carts.json",Cart.class);
                for (Cart cart : carts){
                    if (cart.getIdCart() == cartID){
                        cart.getProductsInShoppingCart().remove(product);
                    }
                }

                DummyDataConnection.saveChanges(carts, cartItem);
                return true;
            }
        }
        return false;
    }

    @Deprecated
    @Override
    public Product[] getProductsInCart(int cartID) {
        LinkedList<Cart>carts =connection.getList("carts.json",Cart.class);
        LinkedList<Product>result = new LinkedList<>();
        for (Cart cart : carts){
            if (cart.getIdCart() == cartID){
                LinkedList<Product>products= cart.getProductsInShoppingCart();

                for (Product product : products){
                    if (!product.isDeleted()){
                        result.add(product);
                    }
                }
            }
        }
        return result.toArray(new Product[0]);


    }
}
