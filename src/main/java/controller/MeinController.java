package controller;

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

    /**
     * Metoda do otwierania nowych okien.
     * fxmlFile musi zawierać pełną ścieżkę od folderu resources.
     */
    public void openNewWindow(String fxmlFile, String title) {
        try {
            // Ważne: "/" na początku oznacza start od głównego folderu resources
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));

            if (fxmlLoader.getLocation() == null) {
                throw new IOException("Nie znaleziono pliku FXML pod ścieżką: " + fxmlFile);
            }

            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) login.getScene().getWindow();

            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("FATALNY BŁĄD: Nie można załadować okna: " + fxmlFile);
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickButtonLogin() {
        // Poprawiona ścieżka uwzględniająca Twój pakiet w resources
        openNewWindow("/com/example/menu1/view-loginPanel.fxml", "Logowanie");
    }

    @FXML
    public void onClickButtonRegister() {
        // Poprawiona ścieżka uwzględniająca Twój pakiet w resources
        openNewWindow("/com/example/menu1/view-registerController.fxml", "Rejestracja");
    }

    @FXML
    public void onClickButtonExit() {
        Platform.exit();
        System.exit(0);
    }
}