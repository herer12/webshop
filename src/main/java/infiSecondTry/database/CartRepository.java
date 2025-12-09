package infiSecondTry.database;


import infiSecondTry.model.Cart;

public interface CartRepository {

    /**Gets the cart associated with the Id of the User thats being send
     * If no cart is associated with the id, it will return null
     * @param userID ID of the User to get the cart for
     * @return Cart Object associated with the User
     */
    Cart getCartForSpecifiedUser(int userID);

    /**Add a specified Product to a certain cart
     * If the cart already contains the product, it will not be added again
     * Also adds the product to the objects Product List
     * @param userID ID of the user to add the product to
     * @param productID ID of the product to add to the cart
     * @return if the write was successful
     */
    boolean addProductToCart(int userID, int productID);

    /**Remove a specified Product from a certain cart
     * If the cart doesn't contain the product, it will not be removed
     * Also removes the product from the objects Product List
     * @param cartID ID of the cart to remove the product from
     * @param productID ID of the product to remove from the cart
     * @return true if the write was successful and if the product was in the cart
     */
    boolean removeProductFromCart(int cartID, int productID);
}
