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

                // Pobieramy zestaw mięśni do przetrenowania w tym dniu
                int[] muscleIds = getMusclesForDay(system, daysCount, dayIndex);

                for (int muscleId : muscleIds) {
                    // Pobieramy 1 lub 2 ćwiczenia na daną partię, żeby plan był pełny (6-8 ćwiczeń)
                    List<Exercise> bestExercises = fetchExercisesForMuscle(conn, muscleId, knee, back, shoulder, userEquipmentLevel, usedExerciseIds, 1);

                    for (Exercise ex : bestExercises) {
                        applyTrainingLogic(ex, goal, sleep);
                        dailyPlan.add(ex);
                        usedExerciseIds.add(ex.getId());
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

        // FBW: Pełna jednostka (Nogi, Plecy, Klatka, Barki, Brzuch, Biceps/Triceps)
        if (sys.contains("FBW")) {
            return new int[]{1, 6, 14, 13, 20, 18, 16};
        }

        // UPPER/LOWER: Rozbicie na dół i górę
        if (sys.contains("UPPER") || sys.contains("LOWER")) {
            if (currentDay % 2 == 0) {
                return new int[]{14, 6, 7, 13, 16, 18}; // Góra: Klatka, Plecy x2, Barki, Ramiona
            } else {
                return new int[]{1, 2, 3, 4, 20}; // Dół: Czwórki, Dwójki, Pośladki, Łydki, Brzuch
            }
        }

        // PPL lub SPLIT: Precyzyjne jednostki
        if (sys.contains("PPL") || sys.contains("SPLIT")) {
            int dayMod = (totalDays <= 3) ? currentDay % 3 : currentDay % 4;

            if (dayMod == 0) return new int[]{14, 15, 13, 18};     // PUSH: Klatka x2, Barki, Triceps
            if (dayMod == 1) return new int[]{6, 7, 9, 16, 20};    // PULL: Plecy x3, Biceps, Brzuch
            if (dayMod == 2) return new int[]{1, 2, 3, 4};         // LEGS: Nogi komplet
            return new int[]{14, 6, 13, 20};                       // Extra/Mix
        }

        return new int[]{1, 6, 14}; // Default
    }

    private List<Exercise> fetchExercisesForMuscle(Connection conn, int muscleId, boolean knee, boolean back,
                                                   boolean shoulder, int userEquip, List<Integer> excludeIds, int limit) throws SQLException {

        List<Exercise> found = new ArrayList<>();
        String excludeStr = excludeIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        String notInClause = excludeIds.isEmpty() ? "" : " AND e.id NOT IN (" + excludeStr + ") ";

        // Logika punktacji: preferujemy wysoką intensywność, ale karzemy za ryzyko kontuzji jeśli użytkownik ją zgłosił
        String sql = "SELECT e.*, (e.intensity - (e.injury_risk * ?)) as dynamic_score " +
                "FROM exercise e " +
                "JOIN exercise_muscle em ON e.id = em.exercise_id " +
                "WHERE em.muscle_id = ? " +
                "AND e.equipment_level <= ? " +
                notInClause +
                "ORDER BY dynamic_score DESC LIMIT " + limit;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int penalty = calculatePenalty(muscleId, knee, back, shoulder);
            pstmt.setInt(1, penalty);
            pstmt.setInt(2, muscleId);
            pstmt.setInt(3, userEquip);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Exercise ex = new Exercise(
                        rs.getInt("id"), rs.getString("name"),
                        rs.getInt("intensity"), rs.getInt("injury_risk"),
                        rs.getInt("difficulty"), rs.getInt("equipment_level")
                );
                found.add(ex);
            }
        }
        return found;
    }

    private int calculatePenalty(int muscleId, boolean knee, boolean back, boolean shoulder) {
        // Jeśli użytkownik ma kontuzję, drastycznie podnosimy karę dla ćwiczeń obciążających te stawy
        int penalty = 0;
        if (knee && (muscleId >= 1 && muscleId <= 4)) penalty = 10;
        if (back && (muscleId >= 6 && muscleId <= 11)) penalty = 10;
        if (shoulder && (muscleId == 13 || muscleId == 14)) penalty = 10;
        return penalty;
    }

    private void applyTrainingLogic(Exercise ex, String goal, int sleep) {
        int baseSets = 3;
        String reps = "8-12";
        String progression = "RIR 2";

        if (goal != null) {
            switch (goal.toUpperCase()) {
                case "SIŁA": baseSets = 4; reps = "5-6"; progression = "Rampa ciężaru"; break;
                case "MASA": baseSets = 3; reps = "8-12"; progression = "RIR 1"; break;
                case "REDUKCJA": baseSets = 3; reps = "12-15"; progression = "Krótkie przerwy"; break;
            }
        }

        // Jeśli regeneracja jest słaba (mało snu), system obcina objętość (serie)
        if (sleep < 5) {
            baseSets = 2;
            progression = "DELOAD (Regeneracja)";
        }

        ex.setSets(String.valueOf(baseSets));
        ex.setReps(reps);
        ex.setProgression(progression);
    }
}