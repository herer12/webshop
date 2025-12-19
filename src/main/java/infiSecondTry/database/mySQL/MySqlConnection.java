package infiSecondTry.database.mySQL;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class MySqlConnection {

    private static final String JDBC_URL ="jdbc:mysql://127.0.0.1:3306/amazon_bestell_system";
    private static final String USER="cgiuser";
    private static final String PASSWORD="1234";

    static {
//        Properties props = new Properties();
//        try (FileInputStream fis = new FileInputStream("Config/config.properties")) {
//            props.load(fis);
//        } catch (Exception e) {
//            throw new ExceptionInInitializerError("DB config load failed: " + e.getMessage());
//        }
//
//        JDBC_URL = props.getProperty("mysql.admin.db.url");
//        USER     = props.getProperty("mysql.admin.db.user");
//        PASSWORD = props.getProperty("mysql.admin.db.password");

        // Treiber explizit laden → spart Zeit bei erstem Connect
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            throw new ExceptionInInitializerError("MySQL Driver not found");
//        }
    }

    private MySqlConnection() {
        // Utility class
    }

    public static Connection getConnectionAdmin()  {

        try {
            return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
