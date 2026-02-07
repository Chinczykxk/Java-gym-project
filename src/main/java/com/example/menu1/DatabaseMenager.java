package com.example.menu1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DatabaseMenager {

    private final File appDirectory;
    private final File databaseFile;

    public DatabaseMenager() {
        String home = System.getProperty("user.home");
        this.appDirectory = new File(home, ".gym-app");
        this.databaseFile = new File(appDirectory, "exercise.db");
    }

    public void init() {
        createAppDirectoryIfNotExists();
        createDatabaseIfNotExists();
    }

    private void createAppDirectoryIfNotExists() {
        if (!appDirectory.exists()) {
            if (appDirectory.mkdirs()) {
                System.out.println("Folder .gym-app utworzony");
            }
        }
    }

    private void createDatabaseIfNotExists() {
        // Jeśli plik nie istnieje LUB jest pusty, wymuś tworzenie tabel
        if (!databaseFile.exists() || databaseFile.length() == 0) {
            try {
                if (!databaseFile.exists()) {
                    databaseFile.createNewFile();
                }

                System.out.println("Inicjalizacja bazy danych w: " + databaseFile.getAbsolutePath());

                // Próbujemy wczytać pliki (teraz z "/" na początku dla pewności)
                String schemaSQL = loadSqlFromFile("db/schema.sql");
                executeSql(schemaSQL);

                String seedSQL = loadSqlFromFile("db/seed.sql");
                executeSql(seedSQL);

                System.out.println("Baza danych została pomyślnie zainicjalizowana.");

            } catch (IOException e) {
                throw new RuntimeException("Nie udało się przygotować bazy", e);
            }
        } else {
            System.out.println("Baza danych już istnieje i ma zawartość.");
        }
    }

    private String loadSqlFromFile(String path) {
        // Dodajemy "/" na początku, aby szukać od korzenia folderu resources
        String resourcePath = path.startsWith("/") ? path : "/" + path;

        // Próba 1: Przez kontekst klasy (najpewniejsza w JavaFX)
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {

            InputStream finalStream = is;

            // Próba 2: Przez ClassLoader (jeśli Próba 1 zawiodła)
            if (finalStream == null) {
                finalStream = getClass().getClassLoader().getResourceAsStream(path);
            }

            if (finalStream == null) {
                throw new RuntimeException("BŁĄD: Nie znaleziono pliku w resources. " +
                        "Upewnij się, że plik jest w: src/main/resources/" + path);
            }

            return new String(finalStream.readAllBytes(), StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas czytania pliku SQL: " + path, e);
        }
    }

    private void executeSql(String sql) {
        String url = "jdbc:sqlite:" + databaseFile.getAbsolutePath();

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {

            // Dzielenie po średniku, żeby SQLite wykonał każdą komendę osobno
            String[] queries = sql.split(";");
            int count = 0;

            for (String query : queries) {
                String trimmed = query.trim();
                if (!trimmed.isEmpty()) {
                    statement.execute(trimmed);
                    count++;
                }
            }
            System.out.println("Wykonano " + count + " instrukcji SQL.");

        } catch (Exception e) {
            System.err.println("BŁĄD SQL: " + e.getMessage());
            throw new RuntimeException("Nie udało się wypełnić bazy", e);
        }
    }
}