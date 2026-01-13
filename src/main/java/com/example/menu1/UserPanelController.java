package com.example.menu1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

//view od user main panel
public class UserPanelController {
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label dataLabel;
    @FXML
    private Label hash;



    // method to show the basic data after login to the account
    public void setInfo(User user) {
        welcomeLabel.setText("Witaj, " + user.getNickname() + "!");
        dataLabel.setText("Imię i nazwisko: " + user.getName() + " " + user.getSurname());
    }



    // functionality - action on button back to menu and log out the account
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
