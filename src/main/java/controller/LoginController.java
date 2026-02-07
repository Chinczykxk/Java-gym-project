package controller;

import com.example.menu1.ConfigUserDatabase;
import model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML public TextField nickLogin;
    @FXML public PasswordField passwordLogin;
    @FXML public Label logmessage;

    private ConfigUserDatabase db = new ConfigUserDatabase();

    @FXML
    public void handleLogin(ActionEvent event) {
        String nick = nickLogin.getText();
        String pass = passwordLogin.getText();

        try {
            User user = db.loginUser(nick, pass);

            if (user != null) {
                System.out.println("Zalogowano pomyślnie: " + user.getNickname());

                // KLUCZOWA ZMIANA: Ścieżka musi zaczynać się od "/" i iść przez foldery w resources
                // Upewnij się, czy plik nazywa się 'user-panel.fxml' czy 'view-user-panel.fxml'!
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/menu1/view-user-panel.fxml"));

                // Sprawdzenie zapobiegawcze, czy loader w ogóle widzi plik
                if (loader.getLocation() == null) {
                    throw new IOException("Nie znaleziono pliku FXML! Sprawdź nazwę w resources.");
                }

                Parent root = loader.load();

                // Przekazanie danych do panelu
                UserPanelController controller = loader.getController();
                if (controller != null) {
                    controller.setInfo(user);
                }

                // PODMIANA OKNA: Pobieramy aktualny Stage i ustawiamy nową scenę
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Panel Użytkownika - " + user.getNickname());
                stage.show();

            } else {
                logmessage.setText("Błędne dane logowania!");
            }
        } catch (IOException e) {
            System.err.println("Błąd ładowania pliku FXML: " + e.getMessage());
            logmessage.setText("Błąd systemu: brak widoku panelu.");
            e.printStackTrace();
        }
    }

    @FXML
    public void backToMenu(ActionEvent event) {
        try {
            // Tutaj to samo - pełna ścieżka od resources
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/menu1/view-hello.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}