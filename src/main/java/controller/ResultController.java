package controller;

import dao.PlanyDao;
import com.example.menu1.ConfigUserDatabase;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Exercise;
import dao.ExerciseDao;
import util.UserSession;
import java.util.List;

public class ResultController {
    @FXML private Label lblPlanName;
    @FXML private TableView<Exercise> tvExercises;

    @FXML private TableColumn<Exercise, String> colName;
    @FXML private TableColumn<Exercise, Integer> colIntensity;
    @FXML private TableColumn<Exercise, Integer> colDifficulty;
    @FXML private TableColumn<Exercise, String> colSets;
    @FXML private TableColumn<Exercise, String> colReps;
    @FXML private TableColumn<Exercise, String> colProgression;

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colIntensity.setCellValueFactory(new PropertyValueFactory<>("intensity"));
        colDifficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));

        if (colSets != null) colSets.setCellValueFactory(new PropertyValueFactory<>("sets"));
        if (colReps != null) colReps.setCellValueFactory(new PropertyValueFactory<>("reps"));
        if (colProgression != null) colProgression.setCellValueFactory(new PropertyValueFactory<>("progression"));

        tvExercises.setPlaceholder(new Label("Trwa dobieranie najlepszych ćwiczeń..."));
    }

    public void initData(String system, boolean knee, boolean back, boolean shoulder, String goal, int sleep) {
        if (lblPlanName != null) {
            lblPlanName.setText("Twój dedykowany plan: " + system + " | Cel: " + goal);
        }

        ExerciseDao dao = new ExerciseDao();
        List<Exercise> plan = dao.generatePlan(system, knee, back, shoulder, goal, sleep);

        if (plan == null || plan.isEmpty()) {
            tvExercises.setPlaceholder(new Label("Błąd: Nie znaleziono ćwiczeń w bazie."));
        } else {
            tvExercises.setItems(FXCollections.observableArrayList(plan));
        }
    }

    @FXML
    private void handleClose() {
        if (tvExercises.getScene() != null && tvExercises.getScene().getWindow() != null) {
            tvExercises.getScene().getWindow().hide();
        }
    }

    @FXML
    private void handleSaveAction() {
        List<Exercise> currentExercises = tvExercises.getItems();

        if (currentExercises.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Tabela jest pusta, nie ma czego zapisać!");
            alert.show();
            return;
        }

        ConfigUserDatabase configDb = new ConfigUserDatabase();
        configDb.setUpDatabase();

        int userId = UserSession.getUserId();

        // NAPRAWIONE: Użycie \n zamiast | dla efektu listy pionowej
        StringBuilder dataBuilder = new StringBuilder();
        for (Exercise ex : currentExercises) {
            dataBuilder.append(ex.getName())
                    .append(" (").append(ex.getSets()).append("x").append(ex.getReps())
                    .append(" - ").append(ex.getProgression()).append(")\n"); // ZMIANA TUTAJ
        }

        PlanyDao planyDao = new PlanyDao();
        String planTitle = (lblPlanName != null) ? lblPlanName.getText() : "Mój Plan";

        planyDao.zapiszPlan(userId, planTitle, dataBuilder.toString());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukces");
        alert.setHeaderText(null);
        alert.setContentText("Plan został zapisany! Ćwiczenia będą teraz widoczne jedno pod drugim.");
        alert.showAndWait();
    }

    public void setRewardPoints(int points) {
        System.out.println("DEBUG: Przyznano nagrodę: +" + points + " XP za wypełnienie ankiety.");
    }
}