PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS muscle (
                                      id INTEGER PRIMARY KEY AUTOINCREMENT,
                                      name TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS exercise (
                                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                                        name TEXT NOT NULL UNIQUE,
                                        intensity INTEGER NOT NULL,
                                        injury_risk INTEGER NOT NULL,
                                        mobility_requirement INTEGER NOT NULL,
                                        difficulty INTEGER NOT NULL,
                                        equipment_level INTEGER NOT NULL,
                                        base_time INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS exercise_muscle (
                                               exercise_id INTEGER NOT NULL,
                                               muscle_id INTEGER NOT NULL,
                                               load INTEGER NOT NULL,
                                               PRIMARY KEY (exercise_id, muscle_id),
    FOREIGN KEY (exercise_id) REFERENCES exercise(id) ON DELETE CASCADE,
    FOREIGN KEY (muscle_id) REFERENCES muscle(id) ON DELETE CASCADE
    );
