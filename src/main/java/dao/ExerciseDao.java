package dao;

import model.Exercise;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExerciseDao {

    private String getDatabaseUrl() {
        String home = System.getProperty("user.home");
        String path = home + File.separator + ".gym-app" + File.separator + "exercise.db";
        return "jdbc:sqlite:" + path;
    }

    public List<Exercise> getAllExercises() {
        List<Exercise> exercises = new ArrayList<>();
        String sql = "SELECT * FROM exercise";

        try (Connection conn = DriverManager.getConnection(getDatabaseUrl());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Exercise exercise = new Exercise();

                // Mapowanie kolumn dokładnie według Twojego schema.sql
                exercise.setId(rs.getInt("id"));
                exercise.setName(rs.getString("name"));
                exercise.setIntensity(rs.getInt("intensity"));
                exercise.setInjuryRisk(rs.getInt("injury_risk"));
                exercise.setDifficulty(rs.getInt("difficulty"));
                exercise.setEquipmentLevel(rs.getInt("equipment_level"));

                exercises.add(exercise);
            }
            System.out.println("Pobrano " + exercises.size() + " ćwiczeń z bazy.");

        } catch (SQLException e) {
            System.err.println("Błąd SQL w ExerciseDao: " + e.getMessage());
        }
        return exercises;
    }
}