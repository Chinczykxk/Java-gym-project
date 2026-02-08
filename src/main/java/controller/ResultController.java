package controller;

import dao.PlanyDao;
import dao.ConfigUserDatabase;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Exercise;
import dao.ExerciseDao;
import util.UserSession;

import java.io.IOException;
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

    // Pole przechowujące pełny plan tygodniowy (wszystkie dni)
    private List<List<Exercise>> weeklySchedule;

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

    /**
     * Zaktualizowana metoda initData - musi przyjąć dni i sprzęt
     */
    public void initData(int daysCount, String system, boolean knee, boolean back, boolean shoulder, String goal, int sleep, int equipmentLevel) {
        if (lblPlanName != null) {
            lblPlanName.setText("Twój plan: " + system + " (" + daysCount + " dni) | Cel: " + goal);
        }

        ExerciseDao dao = new ExerciseDao();

        // Wywołanie nowej metody, którą zaimplementowaliśmy w ExerciseDao
        this.weeklySchedule = dao.generateMultiDayPlan(daysCount, system, knee, back, shoulder, goal, sleep, equipmentLevel);

        if (weeklySchedule == null || weeklySchedule.isEmpty() || weeklySchedule.get(0).isEmpty()) {
            tvExercises.setPlaceholder(new Label("Błąd: Brak ćwiczeń dla wybranych parametrów."));
        } else {
            // Domyślnie pokazujemy pierwszy dzień w tabeli
            tvExercises.setItems(FXCollections.observableArrayList(weeklySchedule.get(0)));
        }
    }

    @FXML
    private void handleSaveAction() {
        if (weeklySchedule == null || weeklySchedule.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Błąd", "Nie ma żadnego planu do zapisania!");
            return;
        }

        ConfigUserDatabase configDb = new ConfigUserDatabase();
        configDb.setUpDatabase();
        int userId = UserSession.getUserId();

        // Budujemy tekstowy opis całego tygodnia (Dzień 1, Dzień 2 itd.)
        StringBuilder dataBuilder = new StringBuilder();
        for (int i = 0; i < weeklySchedule.size(); i++) {
            dataBuilder.append("--- DZIEŃ ").append(i + 1).append(" ---\n");
            for (Exercise ex : weeklySchedule.get(i)) {
                dataBuilder.append(ex.getName())
                        .append(" (").append(ex.getSets()).append("x").append(ex.getReps())
                        .append(" - ").append(ex.getProgression()).append(")\n");
            }
            dataBuilder.append("\n");
        }

        PlanyDao planyDao = new PlanyDao();
        String planTitle = (lblPlanName != null) ? lblPlanName.getText() : "Mój Plan Treningowy";

        planyDao.zapiszPlan(userId, planTitle, dataBuilder.toString());
        showAlert(Alert.AlertType.INFORMATION, "Sukces", "Zapisano cały plan (" + weeklySchedule.size() + " dni)!");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

        @FXML
        public void handleClose(ActionEvent event) {
            try {
                String path = "/com/example/menu1/view-user-panel.fxml";
                FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
                if (loader.getLocation() == null) {
                    System.err.println("BŁĄD: Nie znaleziono pliku FXML pod: " + path);
                    return;
                }
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.getScene().setRoot(root);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    public void setRewardPoints(int points) {
        System.out.println("DEBUG: Nagroda: +" + points + " XP.");
    }
}