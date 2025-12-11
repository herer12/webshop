package infiSecondTry.model;

import java.util.LinkedList;
import java.util.Objects;

public class Cart {

    // The Table Cart is possible not needed because I can use the User Table
    // I will leave it for now.
    // If I add more fields to the cart table



    // Found in the database
    /**Identifaction of the cart of a certain user*/
    private int idCart = 1;


    //Not in the database
    /// I use Linked List because i want to add and delete a lot and if I want the Data i mostly want the whole Data
    /// So the slower access doesn't matter to me
    /**All products saved in the cart*/
    private LinkedList<CartItem> productsInShoppingCart;


    @Override
    public String toString() {
        return "Cart{" +
                "idCart=" + idCart +
                ", productsInShoppingCart=" + productsInShoppingCart +
                '}' + calcTotalPrice();
    }

    /**Constructor to create a new cart with zero products and certain ID*/
    public Cart(int idCart) {
        this.idCart = idCart;
        this.productsInShoppingCart = new LinkedList<>();
    }
    public Cart() {
        this.productsInShoppingCart = new LinkedList<>();
    }

    /**Standard Getter for the ID of the cart*/
    public int getIdCart() {
        return idCart;
    }

    /**Returns all products in the cart in a List*/
    public LinkedList<CartItem> getProductsInShoppingCart() {
        return productsInShoppingCart;
    }

    /**Takes the price of all products in the cart and adds it to the total price
     * @return the total price of all products in the cart*/
    public double calcTotalPrice() {
        double totalPrice = 0;
        for (CartItem product : productsInShoppingCart) {
            totalPrice += product.getProduct().getPrice();
        }
        return totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cart)) return false;
        Cart c = (Cart) o;
        return this.idCart == c.idCart;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCart);
    }



    /**Adds a product to the List*/
    public void addProduct(Product product) {
        for (CartItem item : productsInShoppingCart) {
            if (item.getProduct().equals(product)) {
                item.increaseQuantity(1);
                return;
            }
        }

        productsInShoppingCart.add(new CartItem(product, 1));
    }

}
