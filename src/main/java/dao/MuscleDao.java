package dao;

import model.MuscleGroup;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MuscleDao {
    // Ścieżka do Twojej bazy danych - sprawdź czy nazwa pliku .db się zgadza!
    private static final String URL = "jdbc:sqlite:src/main/resources/db/user_data.db";

    public static List<MuscleGroup> getAllMuscles() {
        List<MuscleGroup> muscles = new ArrayList<>();
        String sql = "SELECT name FROM muscles"; // Zakładamy, że masz taką tabelę

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Zamieniamy tekst z bazy na Enuma MuscleGroup
                String name = rs.getString("name").toUpperCase();
                muscles.add(MuscleGroup.valueOf(name));
            }
        } catch (SQLException e) {
            System.err.println("Błąd MuscleDao: " + e.getMessage());
        }
        return muscles;
    }
}