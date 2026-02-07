package com.example.menu1;

import model.User;

import java.sql.*;

public class ConfigUserDatabase {

    //Declaration database path
    private static final String URL = "jdbc:sqlite:src/main/resources/db/user_data.db";
    // Create a connection to database
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }
    // method with query which create a database or check if the database egzist we use it to creaty only in first lunch of application
    public void setUpDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, surname TEXT, nick TEXT UNIQUE, password TEXT);";
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // method with query which insert infomration to the table, we use it in class RegisterController. Using this method
    public boolean registerUser(String name, String surname, String nick, String password) {
        String sql = "INSERT INTO users(name, surname, nick, password) VALUES(?,?,?,?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, surname);
            pstmt.setString(3, nick);
            pstmt.setString(4, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Błąd rejestracji: " + e.getMessage());
            return false;
        }
    }

// We use this method to login to user account. We send sql query to database with all of this information but we don't use all off the information right now

    public User loginUser(String nick) {
        String sql = "SELECT name, password, nick, surname FROM users WHERE nick = ? ";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nick);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("nick"),
                        rs.getString("surname")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

