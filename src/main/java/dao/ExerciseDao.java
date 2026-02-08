package dao;

import model.Exercise;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExerciseDao {

    private String getDatabaseUrl() {
        String home = System.getProperty("user.home");
        String path = home + File.separator + ".gym-app" + File.separator + "exercise.db";
        return "jdbc:sqlite:" + path;
    }

    public List<List<Exercise>> generateMultiDayPlan(int daysCount, String system, boolean knee, boolean back,
                                                     boolean shoulder, String goal, int sleep, int userEquipmentLevel) {

        List<List<Exercise>> fullWeeklySchedule = new ArrayList<>();
        List<Integer> usedExerciseIds = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(getDatabaseUrl())) {
            for (int dayIndex = 0; dayIndex < daysCount; dayIndex++) {
                List<Exercise> dailyPlan = new ArrayList<>();

                // DYNAMICZNY DOBÓR PARTII ZALEŻNIE OD SYSTEMU I DNIA
                int[] muscleIds = getMusclesForDay(system, daysCount, dayIndex);

                for (int muscleId : muscleIds) {
                    // Szukamy najlepszego ćwiczenia, którego jeszcze NIE MA na blackliście (usedExerciseIds)
                    Exercise best = fetchBestForMuscle(conn, muscleId, knee, back, shoulder, userEquipmentLevel, usedExerciseIds);

                    if (best != null) {
                        applyTrainingLogic(best, goal, sleep);
                        dailyPlan.add(best);
                        usedExerciseIds.add(best.getId());
                    }
                }
                fullWeeklySchedule.add(dailyPlan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fullWeeklySchedule;
    }

    private int[] getMusclesForDay(String system, int totalDays, int currentDay) {
        String sys = system.toUpperCase();

        if (sys.contains("FBW")) {
            return new int[]{1, 6, 14, 13, 20, 18}; // Całe ciało
        }

        if (sys.contains("UPPER") || sys.contains("LOWER")) {
            if (currentDay % 2 == 0) {
                return new int[]{14, 6, 13, 18}; // Góra
            } else {
                return new int[]{1, 2, 3, 4, 20}; // Dół
            }
        }

        if (sys.contains("SPLIT")) {
            if (totalDays <= 3) {
                int dayMod = currentDay % 3;
                if (dayMod == 0) return new int[]{14, 13, 18}; // Push
                if (dayMod == 1) return new int[]{6, 7, 9, 20};  // Pull
                return new int[]{1, 2, 3, 4};                  // Legs
            } else {
                int dayMod = currentDay % 4;
                if (dayMod == 0) return new int[]{14, 20};     // Klatka + Brzuch
                if (dayMod == 1) return new int[]{6, 7};       // Plecy
                if (dayMod == 2) return new int[]{13, 18};     // Barki + Triceps
                return new int[]{1, 2, 3, 4};                  // Nogi
            }
        }
        return new int[]{5};
    }

    private Exercise fetchBestForMuscle(Connection conn, int muscleId, boolean knee, boolean back,
                                        boolean shoulder, int userEquip, List<Integer> excludeIds) throws SQLException {

        String excludeStr = excludeIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        String notInClause = excludeIds.isEmpty() ? "" : " AND e.id NOT IN (" + excludeStr + ") ";

        // Kluczowe: sprawdzamy sprzęt (<=) i odrzucamy użyte wcześniej ćwiczenia
        String sql = "SELECT e.*, (e.intensity - (e.injury_risk * ?)) as dynamic_score " +
                "FROM exercise e " +
                "JOIN exercise_muscle em ON e.id = em.exercise_id " +
                "WHERE em.muscle_id = ? " +
                "AND e.equipment_level <= ? " +
                notInClause +
                "ORDER BY dynamic_score DESC, e.equipment_level DESC LIMIT 1";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int penalty = calculatePenalty(muscleId, knee, back, shoulder);
            pstmt.setInt(1, penalty);
            pstmt.setInt(2, muscleId);
            pstmt.setInt(3, userEquip);

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

    private int calculatePenalty(int muscleId, boolean knee, boolean back, boolean shoulder) {
        int penalty = 0;
        if (knee && (muscleId == 1 || muscleId == 2 || muscleId == 3)) penalty = 4;
        if (back && (muscleId >= 6 && muscleId <= 11)) penalty = 5;
        if (shoulder && (muscleId == 13 || muscleId == 14)) penalty = 3;
        return penalty;
    }

    private void applyTrainingLogic(Exercise ex, String goal, int sleep) {
        int baseSets = 3;
        String reps = "8-12";
        String progression = "RIR 2";

        if (goal != null) {
            switch (goal.toUpperCase()) {
                case "SIŁA": baseSets = 4; reps = "5-6"; progression = "Rampa"; break;
                case "MASA": baseSets = 3; reps = "8-12"; progression = "RIR 1"; break;
                case "REDUKCJA": baseSets = 3; reps = "12-15"; progression = "RIR 3"; break;
            }
        }

        if (sleep < 6) {
            baseSets = Math.max(2, baseSets - 1);
            progression = "DELOAD: " + progression;
        }

        ex.setSets(String.valueOf(baseSets));
        ex.setReps(reps);
        ex.setProgression(progression);
    }
}