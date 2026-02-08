package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.User;
import java.io.IOException;

public class UserPanelController {
    @FXML private Label welcomeLabel;
    @FXML private Label dataLabel;
    @FXML private Label finishWorkoutNumber;

    public void setInfo(User user) {
        if (user != null) {
            welcomeLabel.setText("Witaj " + user.getName() + "!");
            // Używamy getNick() zgodnie z Twoim modelem
            dataLabel.setText(user.getNick() + " (" + user.getSurname() + ")");
        }
    }

    /**
     * NOWA METODA: To ona naprawia błąd logowania.
     * FXML szuka tego ID, by otworzyć okno z wykresem wagi.
     */
    @FXML
    private void handleMonitorProgress(ActionEvent event) {
        System.out.println("Otwieram monitorowanie postępów...");
        changeScene(event, "progress-monitor.fxml");
    }

    // Metoda pomocnicza do zmiany scen
    private void changeScene(ActionEvent event, String fxmlFile) {
        // Poprawiona ścieżka - upewnij się, że pliki FXML są w tym folderze
        String path = "/com/example/menu1/" + fxmlFile;
        var resource = getClass().getResource(path);

        if (resource == null) {
            System.err.println("BŁĄD: Nie znaleziono pliku: " + path);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("Błąd podczas ładowania sceny: " + fxmlFile);
            e.printStackTrace();
        }
    }

    @FXML
    public void backToMenu(ActionEvent event) {
        System.out.println("Wylogowywanie...");
        changeScene(event, "view-hello.fxml");
    }

    @FXML
    public void handleCreatePlan(ActionEvent event) {
        System.out.println("Otwieram ankietę...");
        // Sprawdź czy nazwa to na pewno view-surey.fxml czy view-survey.fxml (częsty czeski błąd!)
        changeScene(event, "view-surey.fxml");
    }

    @FXML
    public void handleSavedPlans(ActionEvent event) {
        System.out.println("Otwieram zapisane plany...");
        changeScene(event, "saved-plans-view.fxml");
    }

    @FXML
    public void handleDoneTraining() {
        System.out.println("Zaliczono trening!");
        try {
            int currentStreak = Integer.parseInt(finishWorkoutNumber.getText());
            finishWorkoutNumber.setText(String.valueOf(currentStreak + 1));
        } catch (NumberFormatException e) {
            finishWorkoutNumber.setText("1");
        }
    }
}