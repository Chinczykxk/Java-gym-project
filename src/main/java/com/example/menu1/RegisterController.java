package com.example.menu1;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML
    public TextField nameRegister;
    @FXML
    public TextField surnameRegister;
    @FXML
    public TextField nickRegister;
    @FXML
    public PasswordField passRegister;
    @FXML
    public PasswordField ageinpassRegister;
    @FXML
    public Label message;
//TODO mozna dodac jeszcze ukrywanie hasel zeby byly ukryte ale je odkrywaczakrywac a nie ze po kliknieciu guziku sie usuwaja i od nowa trzeba wpisywac
    // Decalrate new database which we use for information about user
    private ConfigUserDatabase db = new ConfigUserDatabase();

    // Creating method initialize which allows us to use setupDatabse method there
    @FXML
    public void initialize() {
        db.setUpDatabase();
    }
    //insert the information from the registration panel to the database with check if the user is egzist
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
                System.out.println("Zarejestrowano pomyślnie!");
                message.setText("Zarejestrowano pomyślnie!");
            } else {
                System.out.println("Błąd: Nick prawdopodobnie zajęty.");
                message.setText("Błąd: Nick prawdopodobnie zajęty.");
            }
        } else {
            System.out.println("Hasła nie są zgodne!");
            message.setText("Hasła nie są zgodne!");
        }


    }

    // functionality - action on button back to menu
    @FXML
    public void backToMenu(ActionEvent event) throws IOException {
        // Loading a file from the menu
        Parent root = FXMLLoader.load(getClass().getResource("view-hello.fxml"));
        // Bring Stage from the button
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Replacement the scene
        stage.getScene().setRoot(root);
    }
}
