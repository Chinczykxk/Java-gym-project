package dao;

import controller.SavedPlansController.SavedPlanRow;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlanyDao {
    private static final String URL = "jdbc:sqlite:src/main/resources/db/user_data.db";

    // Upewniamy się, że sterownik jest załadowany przed próbą połączenia
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("PlanyDao: Nie znaleziono sterownika JDBC!");
        }
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public void zapiszPlan(int userId, String planTitle, String planContent) {
        String sql = "INSERT INTO user_plans(user_id, plan_title, plan_content) VALUES(?,?,?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, planTitle);
            pstmt.setString(3, planContent);
            pstmt.executeUpdate();
            System.out.println("DEBUG: Plan zapisany pomyślnie dla użytkownika " + userId);

        } catch (SQLException e) {
            System.err.println("PlanyDao błąd zapisu: " + e.getMessage());
        }
    }

    public List<SavedPlanRow> pobierzPlanyUzytkownika(int userId) {
        List<SavedPlanRow> plans = new ArrayList<>();
        String sql = "SELECT id, plan_title, plan_content FROM user_plans WHERE user_id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                plans.add(new SavedPlanRow(
                        rs.getInt("id"),
                        rs.getString("plan_title"),
                        rs.getString("plan_content")
                ));
            }
        } catch (SQLException e) {
            System.err.println("PlanyDao błąd pobierania: " + e.getMessage());
        }
        return plans;
    }

    public void usunPlan(int planId) {
        String sql = "DELETE FROM user_plans WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, planId);
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("DEBUG: Plan o ID " + planId + " usunięty z bazy.");
            }
        } catch (SQLException e) {
            System.err.println("PlanyDao błąd usuwania: " + e.getMessage());
        }
    }
}