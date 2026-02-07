package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Exercise;
import java.util.List;

public class ResultController {
    @FXML private Label lblPlanName;
    @FXML private TableView<Exercise> tvExercises;

    // Zmieniono String na Integer dla intensywności i trudności
    @FXML private TableColumn<Exercise, String> colName;
    @FXML private TableColumn<Exercise, Integer> colIntensity;
    @FXML private TableColumn<Exercise, Integer> colDifficulty;

    @FXML
    public void initialize() {
        // Mapowanie pól z klasy Exercise
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colIntensity.setCellValueFactory(new PropertyValueFactory<>("intensity"));
        colDifficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));

        // Opcjonalne: informacja gdy tabela jest pusta
        tvExercises.setPlaceholder(new Label("Brak ćwiczeń spełniających wybrane kryteria."));
    }

    public void setPlanData(String planName, List<Exercise> exercises) {
        if (lblPlanName != null) {
            lblPlanName.setText("Profil: " + planName);
        }

        if (exercises == null || exercises.isEmpty()) {
            System.out.println("DEBUG: Lista ćwiczeń wysłana do widoku jest pusta!");
        } else {
            System.out.println("DEBUG: Wyświetlam " + exercises.size() + " ćwiczeń w tabeli.");
        }

        tvExercises.setItems(FXCollections.observableArrayList(exercises));
    }

    @FXML
    private void handleClose() {
        tvExercises.getScene().getWindow().hide();
    }
}