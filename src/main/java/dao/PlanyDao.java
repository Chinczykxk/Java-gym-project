package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlanyDao {
    // Ścieżka do bazy - sprawdź czy folder i nazwa pliku są identyczne jak u Ciebie!
    private static final String URL = "jdbc:sqlite:src/main/resources/db/user_data.db";

    /**
     * Zapisuje tag wybranego planu dla konkretnego użytkownika.
     */
    public static void saveUserPlan(int userId, String planTag) {
        // Zakładam, że w Twoim pliku scheam.sql masz tabelę user_plans lub kolumnę w users
        String sql = "UPDATE users SET current_plan_tag = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, planTag);
            pstmt.setInt(2, userId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Plan '" + planTag + "' został zapisany dla użytkownika ID: " + userId);
            }

        } catch (SQLException e) {
            System.err.println("Błąd podczas zapisywania planu w PlanyDao: " + e.getMessage());
            e.printStackTrace();
        }
    }
}