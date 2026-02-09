package controller;

import dao.PlanyDao;
import dao.ExerciseDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Exercise;
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

    private List<List<Exercise>> weeklySchedule;

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colIntensity.setCellValueFactory(new PropertyValueFactory<>("intensity"));
        colDifficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));

        // Upewnij się, że te kolumny istnieją w Twoim pliku FXML
        if (colSets != null) colSets.setCellValueFactory(new PropertyValueFactory<>("sets"));
        if (colReps != null) colReps.setCellValueFactory(new PropertyValueFactory<>("reps"));
        if (colProgression != null) colProgression.setCellValueFactory(new PropertyValueFactory<>("progression"));

        tvExercises.setPlaceholder(new Label("Generowanie planu..."));
    }

    public void initData(int daysCount, String system, boolean knee, boolean back, boolean shoulder, String goal, int sleep, int equipmentLevel) {
        if (lblPlanName != null) {
            lblPlanName.setText("Twój plan: " + system + " (" + daysCount + " dni) | Cel: " + goal);
        }

        ExerciseDao dao = new ExerciseDao();
        // Pobieramy całe jednostki treningowe wygenerowane przez algorytm
        this.weeklySchedule = dao.generateMultiDayPlan(daysCount, system, knee, back, shoulder, goal, sleep, equipmentLevel);

        if (weeklySchedule == null || weeklySchedule.isEmpty()) {
            tvExercises.setPlaceholder(new Label("Błąd: Brak ćwiczeń spełniających kryteria."));
        } else {
            ObservableList<Exercise> displayList = FXCollections.observableArrayList();

            // Przechodzimy przez każdy DZIEŃ (lista list)
            for (int i = 0; i < weeklySchedule.size(); i++) {
                List<Exercise> dayExercises = weeklySchedule.get(i);
                int dayNum = i + 1;

                // Przechodzimy przez KAŻDE ćwiczenie w danym dniu
                for (Exercise originalEx : dayExercises) {
                    // Tworzymy kopię do wyświetlenia, aby uniknąć błędów kompilacji z konstruktorem
                    Exercise visualEx = new Exercise();

                    // Formatujemy nazwę, aby w tabeli było jasne, do którego dnia należy ćwiczenie
                    visualEx.setName("Dzień " + dayNum + ": " + originalEx.getName());

                    visualEx.setIntensity(originalEx.getIntensity());
                    visualEx.setDifficulty(originalEx.getDifficulty());

                    // Przepisujemy parametry serii/powtórzeń/progresji
                    visualEx.setSets(originalEx.getSets());
                    visualEx.setReps(originalEx.getReps());
                    visualEx.setProgression(originalEx.getProgression());

                    displayList.add(visualEx);
                }
            }
            // Teraz tabela pokaże pełną listę wszystkich ćwiczeń ze wszystkich dni
            tvExercises.setItems(displayList);
        }
    }

    @FXML
    private void handleSaveAction() {
        if (weeklySchedule == null || weeklySchedule.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Błąd", "Nie ma czego zapisać!");
            return;
        }

        StringBuilder dataBuilder = new StringBuilder();
        for (int i = 0; i < weeklySchedule.size(); i++) {
            dataBuilder.append("--- DZIEŃ ").append(i + 1).append(" ---\n");
            for (Exercise ex : weeklySchedule.get(i)) {
                // Przy zapisie używamy oryginalnych danych bez prefiksu "Dzień X"
                dataBuilder.append(ex.getName())
                        .append(" | ").append(ex.getSets()).append("x").append(ex.getReps())
                        .append(" | ").append(ex.getProgression()).append("\n");
            }
            dataBuilder.append("\n");
        }

        PlanyDao planyDao = new PlanyDao();
        planyDao.zapiszPlan(UserSession.getUserId(), lblPlanName.getText(), dataBuilder.toString());

        showAlert(Alert.AlertType.INFORMATION, "Sukces", "Pełny plan treningowy został zapisany!");
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
}