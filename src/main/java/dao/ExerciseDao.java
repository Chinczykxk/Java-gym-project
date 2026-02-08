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

    // Dodaliśmy parametry: goal (cel) i sleep (ilość snu)
    public List<Exercise> generatePlan(String system, boolean knee, boolean back, boolean shoulder, String goal, int sleep) {
        List<Exercise> finalPlan = new ArrayList<>();

        // Mapowanie na ID z Twojego seeda
        int[] muscleIds;
        if (system.contains("FBW")) {
            muscleIds = new int[]{1, 6, 14, 13, 18, 20};
        } else {
            muscleIds = new int[]{1, 6, 14, 13, 20};
        }

        try (Connection conn = DriverManager.getConnection(getDatabaseUrl())) {
            for (int muscleId : muscleIds) {
                Exercise best = fetchBestForMuscle(conn, muscleId, knee, back, shoulder);
                if (best != null) {
                    // Aplikujemy parametry serii, powtórzeń i RIR/Rampy
                    applyTrainingLogic(best, goal, sleep);
                    finalPlan.add(best);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return finalPlan;
    }

    private void applyTrainingLogic(Exercise ex, String goal, int sleep) {
        int baseSets = 3;
        String reps = "8-10";
        String progression = "RIR 2";

        // 1. Dobór pod cel (zgodnie z systemem Rampa/RIR)
        if (goal != null) {
            switch (goal.toUpperCase()) {
                case "SIŁA":
                    baseSets = 4;
                    reps = "5";
                    progression = "Rampa 5";
                    break;
                case "MASA":
                    baseSets = 3;
                    reps = "8-12";
                    progression = "RIR 1-2";
                    break;
                case "REDUKCJA":
                    baseSets = 3;
                    reps = "12-15";
                    progression = "RIR 3";
                    break;
            }
        }

        // 2. Korekta o sen (Regeneracja)
        if (sleep < 6) {
            baseSets = Math.max(2, baseSets - 1); // Mniej serii przy braku snu
            progression += " (Deload)"; // Sygnał dla użytkownika
        } else if (sleep >= 8) {
            // Można opcjonalnie dodać bonusową serię
        }

        ex.setSets(String.valueOf(baseSets));
        ex.setReps(reps);
        ex.setProgression(progression);
    }

    public void savePlanToDb(String planName, List<Exercise> exercises) {
        String sql = "INSERT INTO saved_plans(plan_name, exercises_data) VALUES(?, ?)";

        // Zamieniamy listę ćwiczeń na prosty ciąg znaków, by łatwo go zapisać
        StringBuilder sb = new StringBuilder();
        for (Exercise e : exercises) {
            sb.append(e.getName()).append(" (").append(e.getSets()).append("x").append(e.getReps()).append("), ");
        }

        try (Connection conn = DriverManager.getConnection(getDatabaseUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, planName);
            pstmt.setString(2, sb.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Exercise fetchBestForMuscle(Connection conn, int muscleId, boolean knee, boolean back, boolean shoulder) throws SQLException {
        String sql = "SELECT e.*, (e.intensity - (e.injury_risk * ?)) as dynamic_score " +
                "FROM exercise e " +
                "JOIN exercise_muscle em ON e.id = em.exercise_id " +
                "WHERE em.muscle_id = ? " +
                "ORDER BY dynamic_score DESC LIMIT 1";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Kara za ryzyko kontuzji (penalty = 4 jeśli boli)
            int penalty = 0;
            if (knee && (muscleId == 1 || muscleId == 2)) penalty = 4;
            if (back && (muscleId >= 6 && muscleId <= 10)) penalty = 4;
            if (shoulder && (muscleId == 13 || muscleId == 14)) penalty = 4; // Barki i Klatka wpływają na bark

            pstmt.setInt(1, penalty);
            pstmt.setInt(2, muscleId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Exercise(
                        rs.getInt("id"), rs.getString("name"),
                        rs.getInt("intensity"), rs.getInt("injury_risk"),
                        rs.getInt("difficulty"), rs.getInt("equipment_level")
                );
            }
        }
        return null;
    }
}