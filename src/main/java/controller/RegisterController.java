package controller;

import com.example.menu1.ConfigUserDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class RegisterController {

    @FXML public TextField nameRegister, surnameRegister, nickRegister;
    @FXML public PasswordField passRegister, ageinpassRegister;
    @FXML public TextField passRegisterText, ageinpassRegisterText; // NOWE POLA
    @FXML public CheckBox checkBoxv1Register, checkBoxv2Register;
    @FXML public Label message;

    private ConfigUserDatabase db = new ConfigUserDatabase();

    @FXML
    public void initialize() {
        db.setUpDatabase();
    }

    // TA FUNKCJA OBSŁUGUJE POKAZYWANIE HASŁA
    @FXML
    public void togglePassword() {
        // Pierwsze hasło
        if (checkBoxv1Register.isSelected()) {
            passRegisterText.setText(passRegister.getText());
            passRegisterText.setVisible(true);
            passRegister.setVisible(false);
        } else {
            passRegister.setText(passRegisterText.getText());
            passRegister.setVisible(true);
            passRegisterText.setVisible(false);
        }

        // Drugie hasło
        if (checkBoxv2Register.isSelected()) {
            ageinpassRegisterText.setText(ageinpassRegister.getText());
            ageinpassRegisterText.setVisible(true);
            ageinpassRegister.setVisible(false);
        } else {
            ageinpassRegister.setText(ageinpassRegisterText.getText());
            ageinpassRegister.setVisible(true);
            ageinpassRegisterText.setVisible(false);
        }
    }

    @FXML
    public void handleRegister() {
        // Pobieramy tekst z aktywnego pola (ukrytego lub jawnego)
        String pass = checkBoxv1Register.isSelected() ? passRegisterText.getText() : passRegister.getText();
        String confirm = checkBoxv2Register.isSelected() ? ageinpassRegisterText.getText() : ageinpassRegister.getText();

        if (pass.equals(confirm) && !nickRegister.getText().isEmpty()) {
            boolean success = db.registerUser(nameRegister.getText(), surnameRegister.getText(), nickRegister.getText(), pass);
            message.setText(success ? "Zarejestrowano pomyślnie!" : "Błąd: Nick zajęty.");
        } else {
            message.setText("Hasła nie są zgodne lub brak nicku!");
        }
    }

    @FXML
    public void backToMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/menu1/view-hello.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}