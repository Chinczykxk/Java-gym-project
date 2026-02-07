package model;

import com.example.menu1.*;

import java.util.List;

public class SurveyResult {

    private List<Goal> goals;                     // wiele celów
    private Level level;                          // jeden poziom
    private List<Equipment> equipment;            // wiele opcji sprzętu
    private List<Limitation> limitations;         // wiele ograniczeń
    private List<MuscleGroup> preferredMuscles;   // priorytety mięśni

    public SurveyResult(
            List<Goal> goals,
            Level level,
            List<Equipment> equipment,
            List<Limitation> limitations,
            List<MuscleGroup> preferredMuscles
    ) {
        this.goals = goals;
        this.level = level;
        this.equipment = equipment;
        this.limitations = limitations;
        this.preferredMuscles = preferredMuscles;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public Level getLevel() {
        return level;
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public List<Limitation> getLimitations() {
        return limitations;
    }

    public List<MuscleGroup> getPreferredMuscles() {
        return preferredMuscles;
    }
}
