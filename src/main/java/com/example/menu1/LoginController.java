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
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

public class LoginController {


    @FXML
    public TextField nickLogin;
    @FXML
    public TextField passwordLogin;
    @FXML
    public Label logmessage;

    //there we don't create new database but we initialize this class because we will work on elements from this class
    private ConfigUserDatabase db = new ConfigUserDatabase();

    //function on button click what will happen when we click login
    @FXML
    public void handleLogin(ActionEvent event) throws IOException {



                //We use method from user class so we need a nickname from the login panel
                User user = db.loginUser(nickLogin.getText());
                //We don't need a password over here because we will check it in the next condition

                //condition if user is not empty  and the hashed password form database is correct with password from login panel we go to new stage with main user panel
                //
                if (user != null && BCrypt.checkpw(passwordLogin.getText(), user.getPassword())) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("view-user-panel.fxml"));
                    Parent root = loader.load();


                    UserPanelController controller = loader.getController();
                    controller.setInfo(user);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.getScene().setRoot(root);
                } else {
                    //there we give information if some of data are not correct
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

    //TODO kurde tą funkcje używam kilka razy to backToMenu nie wiem jak to zastąpić w jedną w tym XFML bo mi nie wychodzi jak próbowałem



