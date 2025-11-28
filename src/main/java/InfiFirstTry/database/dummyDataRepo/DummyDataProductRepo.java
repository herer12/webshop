package InfiFirstTry.database.dummyDataRepo;

import InfiFirstTry.database.ProductRepository;
import InfiFirstTry.database.connectionClasses.DummyDataConnection;
import InfiFirstTry.model.Product;

import java.util.LinkedList;

public class DummyDataProductRepo implements ProductRepository {

    private static DummyDataConnection connection = new DummyDataConnection();
    private final String item = "products.json";

    @Override
    public Product[] getAllProducts() {
        LinkedList<Product> list = connection.getList(item, Product.class);
        LinkedList<Product> result = new LinkedList<>();

        for (Product product : list) {
            if (!product.isDeleted()) {
                result.add(product);
            }
        }

        return result.toArray(new Product[0]);
    }

    @Override
    public Product[] getProductsWithName(String productName) {
        productName = productName.toLowerCase();
        LinkedList<Product> result = new LinkedList<>();

        for (Product product : getAllProducts()) {
            if (product.getName().toLowerCase().contains(productName)) {
                result.add(product);
            }
        }

        return result.toArray(new Product[0]);
    }

    @Override
    public Product getProductWithId(int productId) {
        for (Product product : getAllProducts()) {
            if (product.getIdProduct() == productId) {
                return product;
            }
        }
        return null;
    }

    @Override
    public boolean addProduct(Product product) {
        try {
            LinkedList<Product> list = connection.getList(item, Product.class);
            list.add(product);
            DummyDataConnection.saveChanges(list, item);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateProduct(Product product) {
        try {
            LinkedList<Product> list = connection.getList(item, Product.class);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getIdProduct() == product.getIdProduct()) {
                    list.set(i, product);
                    DummyDataConnection.saveChanges(list, item);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteProduct(Product product) {
        try {
            LinkedList<Product> list = connection.getList(item, Product.class);
            for (Product p : list) {
                if (p.getIdProduct() == product.getIdProduct()) {
                    p.setDeleted(true);
                    DummyDataConnection.saveChanges(list, item);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
