package infiSecondTry.database.mySQL;

import infiSecondTry.database.CartRepository;
import infiSecondTry.model.Cart;
import infiSecondTry.model.Product;

import java.io.IOException;
import java.sql.*;

public class MySqlCartRepo implements CartRepository {


    @Override
    public Cart getCartForSpecifiedUser(int userID) throws SQLException {

        String sqlGetCartId = "SELECT idcart FROM user WHERE iduser = ?";
        String sqlGetCartProducts = """
            SELECT p.idproduct, p.name, p.price, cp.quantity
            FROM cart_product_help_tabel cp
            JOIN product p ON cp.idproduct = p.idproduct
            WHERE cp.idcart = ?
        """;

        try (Connection conn = MySqlConnection.getConnectionAdmin()) {

            // 1. Hole Cart-ID des Users
            int cartId = userID;
            /*try (PreparedStatement pstmt = conn.prepareStatement(sqlGetCartId)) {
                pstmt.setInt(1, userID);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    cartId = rs.getInt("idcart");
                    if (rs.wasNull()) {
                        // User hat noch keinen Cart - erstelle einen
                        cartId = createCartForUser(conn, userID);
                    }
                } else {
                    throw new SQLException("User mit ID " + userID + " nicht gefunden");
                }
            }

             */

            // 2. Erstelle Cart-Objekt
            Cart cart = new Cart(cartId);

            // 3. Lade alle Produkte im Cart
            try (PreparedStatement pstmt = conn.prepareStatement(sqlGetCartProducts)) {
                pstmt.setInt(1, cartId);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    int productId = rs.getInt("idproduct");
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    int quantity = rs.getInt("quantity");

                    Product product = new Product(productId, name, price, false);

                    // Füge Produkt mehrmals hinzu entsprechend der quantity
                    for (int i = 0; i < quantity; i++) {
                        cart.addProduct(product);
                    }
                }
            }

            return cart;
        }
    }

    private int createCartForUser(Connection conn, int userID) throws SQLException {
        String sqlCreateCart = "INSERT INTO cart () VALUES ()";
        String sqlUpdateUser = "UPDATE user SET idcart = ? WHERE iduser = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sqlCreateCart,
                Statement.RETURN_GENERATED_KEYS)) {

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int newCartId = rs.getInt(1);

                // Verknüpfe Cart mit User
                try (PreparedStatement updateStmt = conn.prepareStatement(sqlUpdateUser)) {
                    updateStmt.setInt(1, newCartId);
                    updateStmt.setInt(2, userID);
                    updateStmt.executeUpdate();
                }

                return newCartId;
            }

            throw new SQLException("Cart konnte nicht erstellt werden");
        }
    }

    @Override
    public boolean addProductToCart(int userID, int productID) {
        String sqlGetCartId = "SELECT idcart FROM user WHERE iduser = ?";
        String sqlCheckProduct = "SELECT quantity FROM cart_product_help_tabel WHERE idcart = ? AND idproduct = ?";
        String sqlInsertProduct = "INSERT INTO cart_product_help_tabel (idcart, idproduct, quantity) VALUES (?, ?, 1)";
        String sqlUpdateQuantity = "UPDATE cart_product_help_tabel SET quantity = quantity + 1 WHERE idcart = ? AND idproduct = ?";

        try (Connection conn = MySqlConnection.getConnectionAdmin()) {

            // 1. Hole Cart-ID
            int cartId = userID;
            /*try (PreparedStatement pstmt = conn.prepareStatement(sqlGetCartId)) {
                pstmt.setInt(1, userID);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    cartId = rs.getInt("idcart");
                    if (rs.wasNull()) {
                        cartId = createCartForUser(conn, userID);
                    }
                } else {
                    return false; // User existiert nicht
                }
            }

             */

            // 2. Prüfe ob Produkt bereits im Cart
            try (PreparedStatement pstmt = conn.prepareStatement(sqlCheckProduct)) {

                pstmt.setInt(1, cartId);
                pstmt.setInt(2, productID);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    // Produkt existiert bereits - erhöhe Quantity
                    try (PreparedStatement updateStmt = conn.prepareStatement(sqlUpdateQuantity)) {

                        updateStmt.setInt(1, cartId);
                        updateStmt.setInt(2, productID);
                        return updateStmt.executeUpdate() > 0;
                    }
                } else {
                    // Neues Produkt - füge hinzu
                    try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsertProduct)) {

                        insertStmt.setInt(1, cartId);
                        insertStmt.setInt(2, productID);
                        return insertStmt.executeUpdate() > 0;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeProductFromCart(int cartID, int productID) throws IOException {
        String sqlGetQuantity = "SELECT quantity FROM cart_product_help_tabel WHERE idcart = ? AND idproduct = ?";
        String sqlUpdateQuantity = "UPDATE cart_product_help_tabel SET quantity = quantity - 1 WHERE idcart = ? AND idproduct = ?";
        String sqlDeleteProduct = "DELETE FROM cart_product_help_tabel WHERE idcart = ? AND idproduct = ?";

        try (Connection conn = MySqlConnection.getConnectionAdmin()) {

            // 1. Prüfe aktuelle Quantity
            int currentQuantity = 0;
            try (PreparedStatement pstmt = conn.prepareStatement(sqlGetQuantity)) {
                pstmt.setInt(1, cartID);
                pstmt.setInt(2, productID);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    currentQuantity = rs.getInt("quantity");
                } else {
                    return false; // Produkt nicht im Cart
                }
            }

            // 2. Entscheide: Quantity reduzieren oder komplett löschen
            if (currentQuantity > 1) {
                // Reduziere Quantity um 1
                try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdateQuantity)) {
                    pstmt.setInt(1, cartID);
                    pstmt.setInt(2, productID);
                    return pstmt.executeUpdate() > 0;
                }
            } else {
                // Lösche Produkt komplett
                try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteProduct)) {
                    pstmt.setInt(1, cartID);
                    pstmt.setInt(2, productID);
                    return pstmt.executeUpdate() > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}