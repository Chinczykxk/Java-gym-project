package com.example.menu1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {


    @FXML
    public TextField nickLogin;
    @FXML
    public TextField passwordLogin;
    @FXML
    public Label logmessage;

    private ConfigUserDatabase db = new ConfigUserDatabase();

    @FXML
    public void handleLogin(ActionEvent event) throws IOException {


                User user = db.loginUser(nickInput.getText(), passInput.getText());

                if (user != null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("user-panel.fxml"));
                    Parent root = loader.load();

                    // Przekazujemy dane do nowego kontrolera
                    UserPanelController controller = loader.getController();
                    controller.setInfo(user);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.getScene().setRoot(root);
                } else {
                    logmessage.setText("Błędne dane logowania!");
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




