package infiSecondTry.database.dummyData;

import infiSecondTry.database.CartRepository;
import infiSecondTry.model.Cart;
import infiSecondTry.model.CartItem;
import infiSecondTry.model.Product;
import infiSecondTry.model.User;
import infiSecondTry.service.SessionController;

import java.io.IOException;
import java.util.LinkedList;

public class DummyDataCartRepo implements CartRepository {

    private final String cartItemLocation = "carts.json";
    private final DummyDataConnection connection = new DummyDataConnection();
    private final DummyDataProductRepo dummyDataProductRepo = new DummyDataProductRepo();
    private final DummyDataUserRepo dummyDataUserRepo = new DummyDataUserRepo();


    @Override
    public Cart getCartForSpecifiedUser(int userID) {

        User user = dummyDataUserRepo.getUserWithId(userID);
        if (user == null) return null;

        LinkedList<Cart> allCarts = connection.getList(cartItemLocation,Cart.class);

        for (Cart cart : allCarts){
            if (cart.getIdCart() == userID) {
                return cart;
            }
        }
        return null;
    }


    @Override
    public boolean addProductToCart(int userID, int productID) {

        LinkedList<Cart> allCarts = connection.getList(cartItemLocation, Cart.class);
        Product productToAdd = dummyDataProductRepo.getProductWithId(productID);
        Cart cartToAddTo = getCartForSpecifiedUser(userID);

        if (cartToAddTo == null || productToAdd == null) return false;

        boolean found = false;

        for (CartItem item : cartToAddTo.getProductsInShoppingCart()) {
            if (item.getProduct().equals(productToAdd)) {
                item.increaseQuantity(1);
                found = true;
                break;
            }
        }

        if (!found) {
            cartToAddTo.getProductsInShoppingCart().add(new CartItem(productToAdd, 1));
        }

        allCarts.set(allCarts.indexOf(cartToAddTo), cartToAddTo);

        try {
            DummyDataConnection.saveChanges(allCarts, cartItemLocation);
            return true;
        } catch (Exception e) {
            return false;
        }
    }



    @Override
    public boolean removeProductFromCart(int cartID, int productID) throws IOException {

        LinkedList<Cart> allCarts = connection.getList(cartItemLocation, Cart.class);
        Product productToRemove = dummyDataProductRepo.getProductWithId(productID);
        Cart cart = getCartForSpecifiedUser(cartID);

        if (cart == null || productToRemove == null) return false;

        CartItem toRemove = null;

        for (CartItem item : cart.getProductsInShoppingCart()) {
            if (item.getProduct().equals(productToRemove)) {
                toRemove = item;
                break;
            }
        }

        if (toRemove == null) return false;

        // Menge reduzieren
        if (toRemove.getQuantity() > 1) {
            toRemove.decreaseQuantity(1);
        } else {
            cart.getProductsInShoppingCart().remove(toRemove);
        }

        int index = allCarts.indexOf(cart);
        allCarts.set(index, cart);

        try {
            DummyDataConnection.saveChanges(allCarts, cartItemLocation);
            return true;
        } catch (Exception e) {
            return false;
        }
    }




}
