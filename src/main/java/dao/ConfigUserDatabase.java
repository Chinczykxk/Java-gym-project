package dao;

import model.User;
import java.sql.*;

public class ConfigUserDatabase {

    private static final String URL = "jdbc:sqlite:src/main/resources/db/user_data.db";

    // Statyczny blok, który upewnia się, że sterownik SQLite jest dostępny
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Błąd sterownika JDBC: " + e.getMessage());
        }
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /**
     * Tworzy tabele. Musisz wywołać tę metodę w MeinApplication lub RegisterController!
     */
    public void setUpDatabase() {
        String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "surname TEXT," +
                "nick TEXT UNIQUE," +
                "password TEXT" +
                ");";

        // Upewniamy się, że kolumny zgadzają się z tym, co pobiera SavedPlansController
        String plansTable = "CREATE TABLE IF NOT EXISTS user_plans (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "plan_title TEXT, " +
                "plan_content TEXT, " +
                "FOREIGN KEY(user_id) REFERENCES users(id)" +
                ");";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(usersTable);
            stmt.execute(plansTable);
            System.out.println("DEBUG: Tabele users i user_plans są gotowe.");
        } catch (SQLException e) {
            System.err.println("Błąd inicjalizacji bazy: " + e.getMessage());
        }
    }

    public boolean registerUser(String name, String surname, String nick, String pass) {
        // Przy rejestracji warto wywołać setUpDatabase na wypadek, gdyby plik .db został usunięty
        setUpDatabase();
        String sql = "INSERT INTO users(name, surname, nick, password) VALUES(?,?,?,?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, surname);
            pstmt.setString(3, nick);
            pstmt.setString(4, pass);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Błąd rejestracji: " + e.getMessage());
            return false;
        }
    }

    public User loginUser(String nick, String password) {
        String sql = "SELECT id, name, surname, nick, password FROM users WHERE nick = ? AND password = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nick);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("nick"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}