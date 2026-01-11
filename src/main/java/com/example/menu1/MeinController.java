package com.example.menu1;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.io.IOException;

public class MeinController {

    @FXML
    public Button login;
    @FXML
    public Button register;
    @FXML
    public Button exit;

    // Open new windows method
    public void openNewWindow(String fxmlFile, String title) {
        try {
            // Loading file fxml
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) login.getScene().getWindow();
            stage.setScene(scene);

        } catch (IOException e) {
            System.err.println("Błąd podczas otwierania okna: " + fxmlFile);
            e.printStackTrace();
        }
    }
    // function on click is calling function with the appropriate parameter to open loginPanel window
    @FXML
    public void onClickButtonLogin() {
        openNewWindow("view-loginPanel.fxml", "Logowanie");
    }
    // function on click is calling function with the appropriate parameter to open registerPanel window
    @FXML
    public void onClickButtonRegister() {
        openNewWindow("view-registerController.fxml", "Rejestracja");
    }
    // close app function
    @FXML
    public void onClickButtonExit() {

        Platform.exit();
        System.exit(0);
    }
}
