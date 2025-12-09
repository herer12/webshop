package infiSecondTry.database;


import infiSecondTry.model.Product;

public interface ProductRepository {

    /**Reads all Products from the database and generates a Product Object for each Product in the database
     * Excludes Products that are marked as deleted
     * @return an Array of all Product Objects
     */
    Product[] getAllProducts();

    /**Reads all Products from the database and checking where the name is the of the input
     * and returns an Array of Product Objects with the matching name
     * @param productName Name of the Product to search for
     * @return Array of Product Objects with the matching name
     */
    Product[] getProductsWithName(String productName);

    /**Reads all Products from the database and checking where the id is the of the input
     * and returns an Product Object with the matching id
     * @param productId ID of the product to search for
     * @return Product Object with the matching id
     */
    Product getProductWithId(int productId);

    /**Add a Product to the data
     * @param product Product to add to the data
     * @return if the write was successful
     */
    boolean addProduct(Product product);

    /**Updates the Product with the same id in the data
     * @param product Product to update to the data
     * @return if the write was successful
     */
    boolean updateProduct(Product product);

    /**Changes a Attributes that indicates that a Product is Deleted
     * Changes the Product with the same id in the data
     * @param product Product to delete to the data
     * @return if the write was successful
     */
    boolean deleteProduct(Product product);
}
