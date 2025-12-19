package infiSecondTry.database.mySQL;

import infiSecondTry.database.UserRepository;
import infiSecondTry.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MySqlUserRepo implements UserRepository {

    // SQL-Statements als Konstanten (wiederverwendbar)
    private static final String SQL_GET_ALL =
            "SELECT iduser, first_name, last_name, birthday, money_spent, deleted " +
                    "FROM user WHERE deleted = 0";

    private static final String SQL_GET_BY_NAME =
            "SELECT iduser, first_name, last_name, birthday, money_spent, deleted " +
                    "FROM user WHERE (LOWER(first_name) = ? OR LOWER(last_name) = ?) AND deleted = 0";

    private static final String SQL_GET_BY_ID =
            "SELECT iduser, first_name, last_name, birthday, money_spent, deleted " +
                    "FROM user WHERE iduser = ? AND deleted = 0";

    private User mapUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("iduser");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        Date birthdayDate = rs.getDate("birthday");
        LocalDate birthDate = birthdayDate != null ? birthdayDate.toLocalDate() : null;
        double moneySpent = rs.getDouble("money_spent");
        int deleted = rs.getInt("deleted");

        return new User(id, firstName, lastName, birthDate, moneySpent, deleted != 0);
    }

    @Override
    public User[] getAllUsers() {
        try (Connection conn = MySqlConnection.getConnectionAdmin();
             PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL);
             ResultSet rs = pstmt.executeQuery()) {

            return mapUsers(rs);

        } catch (SQLException e) {
            e.printStackTrace();
            return new User[0];
        }
    }

    @Override
    public User[] getUsersWithName(String userName) {
        if (userName == null || userName.trim().isEmpty()) {
            return new User[0];
        }

        try (Connection conn = MySqlConnection.getConnectionAdmin();
             PreparedStatement pstmt = conn.prepareStatement(SQL_GET_BY_NAME)) {

            String lowerName = userName.toLowerCase().trim();
            pstmt.setString(1, lowerName);
            pstmt.setString(2, lowerName);

            try (ResultSet rs = pstmt.executeQuery()) {
                return mapUsers(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new User[0];
        }
    }

    @Override
    public User getUserWithId(int userId) {
        try (Connection conn = MySqlConnection.getConnectionAdmin();
             PreparedStatement pstmt = conn.prepareStatement(SQL_GET_BY_ID)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private User[] mapUsers(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();

        while (rs.next()) {
            users.add(mapUser(rs));
        }

        return users.toArray(new User[0]);
    }

    @Override
    public boolean addUser(User user) {
        String sql = "INSERT INTO user (first_name, last_name, birthday, money_spent, deleted) " +
                "VALUES (?, ?, ?, ?, 0)";

        try (Connection conn = MySqlConnection.getConnectionAdmin();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setDate(3, user.getBirthday() != null ? Date.valueOf(user.getBirthday()) : null);
            pstmt.setDouble(4, user.getMoneySpent());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateUser(User user) {
        String sql = "UPDATE user SET first_name = ?, last_name = ?, birthday = ?, " +
                "money_spent = ? WHERE iduser = ?";

        try (Connection conn = MySqlConnection.getConnectionAdmin();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setDate(3, user.getBirthday() != null ? Date.valueOf(user.getBirthday()) : null);
            pstmt.setDouble(4, user.getMoneySpent());
            pstmt.setInt(5, user.getIdUser());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUser(User user) {
        // Soft delete
        String sql = "UPDATE user SET deleted = 1 WHERE iduser = ?";

        try (Connection conn = MySqlConnection.getConnectionAdmin();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, user.getIdUser());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}