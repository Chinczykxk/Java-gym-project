package model;

/**
 * Model ćwiczenia rozszerzony o parametry treningowe (Serie, Powtórzenia, Progresja).
 */
public class Exercise {
    private int id;
    private String name;
    private int intensity;
    private int injuryRisk;
    private int difficulty;
    private int equipmentLevel;

    // Nowe pola obliczane dynamicznie na podstawie ankiety
    private String sets;         // np. "3-4"
    private String reps;         // np. "5" lub "12-15"
    private String progression;  // np. "Rampa 5" lub "RIR 2"

    public Exercise() {}

    public Exercise(int id, String name, int intensity, int injuryRisk, int difficulty, int equipmentLevel) {
        this.id = id;
        this.name = name;
        this.intensity = intensity;
        this.injuryRisk = injuryRisk;
        this.difficulty = difficulty;
        this.equipmentLevel = equipmentLevel;
    }

    // --- Gettery dla TableView (Standardowe) ---
    public int getId() { return id; }
    public String getName() { return name; }
    public int getIntensity() { return intensity; }
    public int getInjuryRisk() { return injuryRisk; }
    public int getDifficulty() { return difficulty; }
    public int getEquipmentLevel() { return equipmentLevel; }

    // --- Gettery dla TableView (Nowe parametry) ---
    public String getSets() { return sets; }
    public String getReps() { return reps; }
    public String getProgression() { return progression; }

    // --- Settery ---
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setIntensity(int intensity) { this.intensity = intensity; }
    public void setInjuryRisk(int injuryRisk) { this.injuryRisk = injuryRisk; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }
    public void setEquipmentLevel(int equipmentLevel) { this.equipmentLevel = equipmentLevel; }

    public void setSets(String sets) { this.sets = sets; }
    public void setReps(String reps) { this.reps = reps; }
    public void setProgression(String progression) { this.progression = progression; }

    @Override
    public String toString() {
        return String.format("%s | %s x %s | %s", name, sets, reps, progression);
    }
}