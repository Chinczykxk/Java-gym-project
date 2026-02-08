package controller; // POPRAWIONE: Musi być 'controller', bo tam leży plik

import dao.ConfigUserDatabase; // DODANO: Import, bo baza jest teraz w innym folderze
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import dao.ConfigUserDatabase;
import java.io.IOException;

public class RegisterController {

    @FXML public TextField nameRegister;
    @FXML public TextField surnameRegister;
    @FXML public TextField nickRegister;
    @FXML public PasswordField passRegister;
    @FXML public PasswordField ageinpassRegister;
    @FXML public Label message;

    private ConfigUserDatabase db = new ConfigUserDatabase();

    @FXML
    public void initialize() {
        // Upewnij się, że ta metoda istnieje w ConfigUserDatabase w pakiecie dao
        db.setUpDatabase();
    }

    @FXML
    public void handleRegister() {
        String name = nameRegister.getText();
        String surname = surnameRegister.getText();
        String nick = nickRegister.getText();
        String pass = passRegister.getText();
        String confirm = ageinpassRegister.getText();

        if (pass.equals(confirm) && !nick.isEmpty()) {
            boolean success = db.registerUser(name, surname, nick, pass);
            if (success) {
                message.setText("Zarejestrowano pomyślnie!");
            } else {
                message.setText("Błąd: Nick prawdopodobnie zajęty.");
            }
        } else {
            message.setText("Hasła nie są zgodne!");
        }
    }

    @FXML
    public void backToMenu(ActionEvent event) throws IOException {
        // Poprawiona ścieżka do resources
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/menu1/view-hello.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }
}