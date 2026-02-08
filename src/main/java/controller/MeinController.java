package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.IOException;
import dao.ConfigUserDatabase;
public class  MeinController {

    @FXML
    public void onClickButtonLogin(ActionEvent event) {
        // Musisz podać pełną ścieżkę od folderu resources
        loadScene(event, "/com/example/menu1/view-loginPanel.fxml", "Logowanie");
    }

    @FXML
    public void onClickButtonRegister(ActionEvent event) {
        // Sprawdź dokładnie czy nazwa pliku to view-registerController.fxml
        loadScene(event, "/com/example/menu1/view-registerController.fxml", "Rejestracja");
    }

    @FXML
    public void onClickButtonExit() {
        System.exit(0);
    }

    private void loadScene(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println("BŁĄD: Nie można załadować " + fxmlPath);
            e.printStackTrace();
        }
    }
}