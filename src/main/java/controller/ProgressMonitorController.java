package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.UserSession;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProgressMonitorController {

    @FXML private LineChart<String, Number> weightChart;
    @FXML private TextField weightInput;

    private final String DB_URL = "jdbc:sqlite:" + System.getProperty("user.home") + "/.gym-app/exercise.db";

    // Formater, który pokaże datę i godzinę (żeby punkty szły w prawo)
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    public void initialize() {
        ensureTableExists();
        loadChartData();
    }

    private void ensureTableExists() {
        String sql = "CREATE TABLE IF NOT EXISTS weight_history (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "weight DOUBLE, " +
                "date TEXT)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadChartData() {
        weightChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Postęp wagi");

        // Sortujemy po ID lub dacie, aby punkty układały się chronologicznie od lewej do prawej
        String sql = "SELECT date, weight FROM weight_history WHERE user_id = ? ORDER BY date ASC";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, UserSession.getUserId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String fullDate = rs.getString("date");
                double weight = rs.getDouble("weight");

                // Każdy wpis ma teraz unikalną etykietę (data + czas), więc punkty idą w prawo
                series.getData().add(new XYChart.Data<>(fullDate, weight));
            }

            if (!series.getData().isEmpty()) {
                weightChart.getData().add(series);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddWeight() {
        String rawInput = weightInput.getText().trim().replace(",", ".");

        if (rawInput.isEmpty()) return;

        try {
            double weight = Double.parseDouble(rawInput);
            // Zmieniamy na LocalDateTime, żeby każdy pomiar miał unikalny czas (punkt w prawo)
            String timestamp = LocalDateTime.now().format(formatter);

            String sql = "INSERT INTO weight_history(user_id, weight, date) VALUES(?,?,?)";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, UserSession.getUserId());
                pstmt.setDouble(2, weight);
                pstmt.setString(3, timestamp);
                pstmt.executeUpdate();

                loadChartData();
                weightInput.clear();
            }
        } catch (NumberFormatException | SQLException e) {
            System.err.println("Błąd zapisu: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackToPanel(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/menu1/view-user-panel.fxml"));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}