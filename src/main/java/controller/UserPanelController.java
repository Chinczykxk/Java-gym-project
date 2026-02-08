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
import dao.ConfigUserDatabase;
public class UserPanelController {
    @FXML private Label welcomeLabel;
    @FXML private Label dataLabel;
    @FXML private Label finishWorkoutNumber;

    public void setInfo(User user) {
        if (user != null) {
            welcomeLabel.setText("Witaj " + user.getName() + "!");
            // Zmień getNickname() na getNick()
            dataLabel.setText(user.getNick() + " (" + user.getSurname() + ")");
        }
    }

    // Metoda pomocnicza, żeby nie powtarzać kodu ładowania okien
    private void changeScene(ActionEvent event, String fxmlFile) {
        String path = "/com/example/menu1/" + fxmlFile;
        var resource = getClass().getResource(path);

        if (resource == null) {
            System.err.println("BŁĄD: Nie znaleziono pliku: " + path);
            System.err.println("Upewnij się, że plik leży w: src/main/resources/com/example/menu1/");
            return; // Zatrzymujemy, żeby nie wywaliło IllegalStateException
        }

        try {
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
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
        // Zakładam, że Twoja ankieta nazywa się view-ankieta.fxml (popraw nazwę jeśli jest inna!)
        changeScene(event, "view-surey.fxml");
    }

    @FXML
    public void handleSavedPlans(ActionEvent event) {
        System.out.println("Otwieram zapisane plany...");
        changeScene(event, "saved-plans-view.fxml"); // Musi być identyczna jak nazwa pliku!
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