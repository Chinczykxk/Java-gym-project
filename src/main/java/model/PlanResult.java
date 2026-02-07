package model;

import java.util.List;

public class PlanResult {
    private String planName;
    private List<Exercise> exercises;

    public PlanResult(String planName, List<Exercise> exercises) {
        this.planName = planName;
        this.exercises = exercises;
    }

    public String getPlanName() { return planName; }
    public List<Exercise> getExercises() { return exercises; }
}