package com.example.menu1;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

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
