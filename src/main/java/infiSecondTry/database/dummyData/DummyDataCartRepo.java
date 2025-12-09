package infiSecondTry.database.dummyData;

import infiSecondTry.database.CartRepository;
import infiSecondTry.model.Cart;
import infiSecondTry.model.Product;
import infiSecondTry.model.User;

import java.util.LinkedList;

public class DummyDataCartRepo implements CartRepository {

    private final String cartItemLocation = "carts.json";
    private final DummyDataConnection connection = new DummyDataConnection();
    private final DummyDataProductRepo dummyDataProductRepo = new DummyDataProductRepo();
    private final DummyDataUserRepo dummyDataUserRepo = new DummyDataUserRepo();


    @Override
    public Cart getCartForSpecifiedUser(int userID) {

        User[] allUsers = dummyDataUserRepo.getAllUsers();
        User userWithUserId = dummyDataUserRepo.getUserWithId(userID);
        LinkedList<Cart> allCarts = connection.getList(cartItemLocation,Cart.class);

        if (userWithUserId == null||allUsers == null) {
            return null;
        }

        for (Cart cart : allCarts){
            if (cart.getIdCart() == userID){
                return cart;
            }
        }

        return null;
    }

    @Override
    public boolean addProductToCart(int userID, int productID) {
        LinkedList <Cart> allCarts = connection.getList(cartItemLocation,Cart.class);
        Product productToAdd = dummyDataProductRepo.getProductWithId(productID);
        Cart cartToAddTo = getCartForSpecifiedUser(userID);

        if (cartToAddTo == null||productToAdd == null){
            return false;
        }

        if (!cartToAddTo.getProductsInShoppingCart().contains(productToAdd)){
            cartToAddTo.addProduct(productToAdd);
            allCarts.set(allCarts.indexOf(cartToAddTo), cartToAddTo);
        }

        try{
            DummyDataConnection.saveChanges(allCarts, cartItemLocation);
            return true;
        }catch (Exception e){
            return false;
        }

    }


    @Override
    public boolean removeProductFromCart(int cartID, int productID) {

        LinkedList <Cart> allCarts = connection.getList(cartItemLocation,Cart.class);
        Product productToRemove = dummyDataProductRepo.getProductWithId(productID);
        Cart cartToRemoveFrom = getCartForSpecifiedUser(cartID);

        if (cartToRemoveFrom == null||productToRemove == null|| !cartToRemoveFrom.getProductsInShoppingCart().contains(productToRemove)){
            return false;
        }

        cartToRemoveFrom.getProductsInShoppingCart().remove(productToRemove);
        allCarts.remove(cartToRemoveFrom);

        try{
            DummyDataConnection.saveChanges(allCarts, cartItemLocation);
            return true;
        }catch (Exception e){
            return false;
        }

    }


}
