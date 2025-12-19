package infiSecondTry.database.mySQL;

import infiSecondTry.database.ProductRepository;
import infiSecondTry.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlProductRepo implements ProductRepository {

    // SQL-Statements als Konstanten (wiederverwendbar)
    private static final String SQL_GET_ALL =
            "SELECT * " +
                    "FROM product WHERE deleted = 0";

    private static final String SQL_GET_BY_NAME =
            "SELECT idproduct, name, price, deleted " +
                    "FROM product WHERE LOWER(name) LIKE ? AND deleted = 0";

    private static final String SQL_GET_BY_ID =
            "SELECT idproduct, name, price, deleted " +
                    "FROM product WHERE idproduct = ? AND deleted = 0";

    private static final String SQL_INSERT =
            "INSERT INTO product (name, price, deleted) VALUES (?, ?, 0)";

    private static final String SQL_UPDATE =
            "UPDATE product SET name = ?, price = ? WHERE idproduct = ?";

    private static final String SQL_DELETE =
            "UPDATE product SET deleted = 1 WHERE idproduct = ?";

    /**
     * Mappt einen ResultSet-Eintrag auf ein Product-Objekt
     */
    private Product mapProduct(ResultSet rs) throws SQLException {
        int id = rs.getInt("idproduct");
        String name = rs.getString("name");
        double price = rs.getDouble("price");
        int deleted = rs.getInt("deleted");

        return new Product(id, name, price, deleted != 0);
    }

    /**
     * Mappt mehrere ResultSet-Einträge auf ein Product-Array
     */
    private Product[] mapProducts(ResultSet rs) throws SQLException {
        List<Product> products = new ArrayList<>();

        while (rs.next()) {
            products.add(mapProduct(rs));
        }

        return products.toArray(new Product[0]);
    }

    @Override
    public Product[] getAllProducts() {
        try (Connection conn = MySqlConnection.getConnectionAdmin();
             PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL);
             ResultSet rs = pstmt.executeQuery()) {

            return mapProducts(rs);

        } catch (SQLException e) {
            e.printStackTrace();
            return new Product[0];
        }
    }

    @Override
    public Product[] getProductsWithName(String productName) {

        try (Connection conn = MySqlConnection.getConnectionAdmin();
             PreparedStatement pstmt = conn.prepareStatement(SQL_GET_BY_NAME)) {

            String lowerName = productName.toLowerCase().trim();
            pstmt.setString(1, "%" + lowerName + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                return mapProducts(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new Product[0];
        }
    }

    @Override
    public Product getProductWithId(int productId) {
        try (Connection conn = MySqlConnection.getConnectionAdmin();
             PreparedStatement pstmt = conn.prepareStatement(SQL_GET_BY_ID)) {

            pstmt.setInt(1, productId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapProduct(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addProduct(Product product) {
        try (Connection conn = MySqlConnection.getConnectionAdmin();
             PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT)) {

            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateProduct(Product product) {
        try (Connection conn = MySqlConnection.getConnectionAdmin();
             PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE)) {

            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setInt(3, product.getIdProduct());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteProduct(Product product) {
        // Soft delete
        try (Connection conn = MySqlConnection.getConnectionAdmin();
             PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE)) {

            pstmt.setInt(1, product.getIdProduct());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}