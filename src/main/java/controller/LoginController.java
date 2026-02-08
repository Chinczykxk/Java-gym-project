package controller;

import com.example.menu1.ConfigUserDatabase;
import model.User;
import util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
public class LoginController {

    @FXML public TextField nickLogin;
    @FXML public TextField passwordLogin;
    @FXML public Label logmessage;

    // Pole db może być final, bo inicjalizujemy je raz
    private final ConfigUserDatabase db = new ConfigUserDatabase();

    @FXML
    public void handleLogin(ActionEvent event) throws IOException {
        // Pobieramy tekst i usuwamy zbędne spacje (trim)
        String login = nickLogin.getText().trim();
        String pass = passwordLogin.getText();

        if (login.isEmpty() || pass.isEmpty()) {
            logmessage.setText("Wypełnij wszystkie pola!");
            return;
        }

        User user = db.loginUser(login, pass);

        if (user != null) {
            // Inicjalizujemy sesję - używamy getNick(), które dodaliśmy do modelu User
            UserSession.init(user.getId(), user.getNick());

            // Pamiętaj, że ścieżka musi być poprawna względem folderu resources
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/menu1/view-user-panel.fxml"));
            Parent root = loader.load();

            // Przekazujemy dane usera do kontrolera panelu
            UserPanelController controller = loader.getController();
            controller.setInfo(user);

            // Zmiana sceny
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } else {
            logmessage.setText("Błędne dane logowania!");
        }
    }

    @FXML
    public void backToMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/menu1/view-hello.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }
}