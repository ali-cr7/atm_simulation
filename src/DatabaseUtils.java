import java.sql.*;

public class DatabaseUtils {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/atm_simulation";
    private static final String DB_USER = "springstudent";
    private static final String DB_PASSWORD = "springstudent";

    public static boolean authenticateUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT password FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return EncryptionUtils.decrypt(storedPassword).equals(password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void updateBalance(String username, double amount) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "UPDATE users SET balance = balance + ? WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setDouble(1, amount);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean withdrawBalance(String username, double amount) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT balance FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                if (balance >= amount) {
                    query = "UPDATE users SET balance = balance - ? WHERE username = ?";
                    stmt = conn.prepareStatement(query);
                    stmt.setDouble(1, amount);
                    stmt.setString(2, username);
                    stmt.executeUpdate();
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static double getBalance(String username) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT balance FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
