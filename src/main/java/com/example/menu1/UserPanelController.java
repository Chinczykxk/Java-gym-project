package com.example.menu1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserPanelController {
    @FXML private Label welcomeLabel;
    @FXML private Label dataLabel;

    public void setInfo(User user) {
        welcomeLabel.setText("Witaj, " + user.getNickname() + "!");
        dataLabel.setText("Imię i nazwisko: " + user.getName() + " " + user.getSurname());
    }
}
