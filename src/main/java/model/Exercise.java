package model;

public class Exercise {
    private int id;
    private String name;
    private int intensity;
    private int injuryRisk;
    private int difficulty;
    private int equipmentLevel;

    // Jeśli używasz Enumów, upewnij się, że te klasy istnieją w pakiecie model
    private Equipment equipment;
    private Level level;

    public Exercise() {
    }

    public Exercise(int id, String name, int intensity, int injuryRisk, int equipmentLevel) {
        this.id = id;
        this.name = name;
        this.intensity = intensity;
        this.injuryRisk = injuryRisk;
        this.equipmentLevel = equipmentLevel;
    }

    // Gettery i Settery (Muszą być PUBLICZNE)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getIntensity() { return intensity; }
    public void setIntensity(int intensity) { this.intensity = intensity; }

    public int getInjuryRisk() { return injuryRisk; }
    public void setInjuryRisk(int injuryRisk) { this.injuryRisk = injuryRisk; }

    public int getDifficulty() { return difficulty; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }

    public int getEquipmentLevel() { return equipmentLevel; }
    public void setEquipmentLevel(int equipmentLevel) { this.equipmentLevel = equipmentLevel; }

    public Equipment getEquipment() { return equipment; }
    public void setEquipment(Equipment equipment) { this.equipment = equipment; }

    public Level getLevel() { return level; }
    public void setLevel(Level level) { this.level = level; }
}