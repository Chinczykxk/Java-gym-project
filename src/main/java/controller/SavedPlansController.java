package controller;

import dao.PlanyDao;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text; // Wymagany do zawijania tekstu
import javafx.stage.Stage;
import util.UserSession;

import java.io.IOException;
import java.util.List;

public class SavedPlansController {

    @FXML private TableView<SavedPlanRow> tvSavedPlans;
    @FXML private TableColumn<SavedPlanRow, Integer> colId;
    @FXML private TableColumn<SavedPlanRow, String> colTitle;
    @FXML private TableColumn<SavedPlanRow, String> colContent;

    @FXML
    public void initialize() {
        // Standardowe fabryki wartości
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colContent.setCellValueFactory(new PropertyValueFactory<>("content"));

        // --- NAPRAWA WYŚWIETLANIA: Każde ćwiczenie pod spodem ---
        colContent.setCellFactory(tc -> {
            TableCell<SavedPlanRow, String> cell = new TableCell<>() {
                private final Text text = new Text();
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                    } else {
                        text.setText(item);
                        // Automatyczne zawijanie do szerokości kolumny
                        text.wrappingWidthProperty().bind(tc.widthProperty().subtract(10));
                        setGraphic(text);
                    }
                }
            };
            return cell;
        });

        loadUserPlans();
    }

    private void loadUserPlans() {
        System.out.println("Otwieram zapisane plany...");
        PlanyDao dao = new PlanyDao();
        int userId = UserSession.getUserId();

        List<SavedPlanRow> plans = dao.pobierzPlanyUzytkownika(userId);
        tvSavedPlans.setItems(FXCollections.observableArrayList(plans));
    }

    @FXML
    private void handleDeletePlan() {
        SavedPlanRow selected = tvSavedPlans.getSelectionModel().getSelectedItem();
        if (selected != null) {
            PlanyDao dao = new PlanyDao();
            dao.usunPlan(selected.getId());
            loadUserPlans(); // Odśwież listę po usunięciu
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Wybierz plan z tabeli, aby go usunąć.");
            alert.show();
        }
    }

        @FXML
        public void handleBack(ActionEvent event) {
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

    public static class SavedPlanRow {
        private int id;
        private String title;
        private String content;

        public SavedPlanRow(int id, String title, String content) {
            this.id = id;
            this.title = title;
            this.content = content;
        }
        public int getId() { return id; }
        public String getTitle() { return title; }
        public String getContent() { return content; }
    }
}