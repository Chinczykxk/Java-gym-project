package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;

public class UserPanelController {

    @FXML private Label welcomeLabel;
    @FXML private Label dataLabel;
    @FXML private Label finishWorkoutNumber; // Licznik streaka
    @FXML private AreaChart<?, ?> progressChart;

    private User currentUser;

    @FXML
    public void initialize() {
        // Poprawka rozmiaru, żeby przy 100+ nie robiło się "..."
        if (finishWorkoutNumber != null) {
            finishWorkoutNumber.setMinWidth(Region.USE_PREF_SIZE);
            finishWorkoutNumber.setMaxWidth(Double.MAX_VALUE);
        }
    }

    public void setInfo(User user) {
        this.currentUser = user;
        if (dataLabel != null) {
            dataLabel.setText(user.getNickname());
        }
    }

    @FXML
    public void handleDoneTraining(ActionEvent event) {
        try {
            String currentText = finishWorkoutNumber.getText().trim();
            // Obsługa przypadku, gdyby w labelu było coś innego niż cyfry
            int currentStreak = currentText.matches("\\d+") ? Integer.parseInt(currentText) : 0;

            int newStreak = currentStreak + 1;
            finishWorkoutNumber.setText(String.valueOf(newStreak));

            // Wizualny bajer po wbiciu setki
            if (newStreak >= 100) {
                finishWorkoutNumber.setStyle("-fx-text-fill: #ff4500; -fx-font-weight: bold;");
            }

            System.out.println("Streak zaktualizowany: " + newStreak);
        } catch (Exception e) {
            System.err.println("Błąd podczas aktualizacji streaka: " + e.getMessage());
            finishWorkoutNumber.setText("1");
        }
    }

    @FXML
    public void handleCreatePlan(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/menu1/view-surey.fxml"));
            Parent root = loader.load();
            Stage surveyStage = new Stage();
            surveyStage.setTitle("Nowa Ankieta Treningowa");
            surveyStage.setScene(new Scene(root));
            surveyStage.show();
        } catch (IOException e) {
            System.err.println("Błąd: Nie można otworzyć okna ankiety!");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSavedPlans(ActionEvent event) {
        switchScene(event, "/com/example/menu1/saved-plans-view.fxml", "Twoje Zapisane Plany");
    }

    @FXML
    public void backToMenu(ActionEvent event) {
        switchScene(event, "/com/example/menu1/view-hello.fxml", "Menu Główne");
    }

    private void switchScene(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            System.err.println("Błąd podczas ładowania: " + fxmlPath);
            e.printStackTrace();
        }
    }
}