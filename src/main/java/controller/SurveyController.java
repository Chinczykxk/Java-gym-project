package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class SurveyController {

    @FXML private RadioButton rbGoalMass, rbGoalReduction, rbGoalStrength;
    @FXML private RadioButton rbStazPoczatkujacy, rbStazSredni, rbStazZaawansowany;
    @FXML private RadioButton rbBrak, rbHantle, rbSiłownia;
    @FXML private CheckBox cbBólBarki, cbBólPlecy, cbBólKolana, cbBólNadgarstki;
    @FXML private Slider sliderRegeneracja;
    @FXML private Spinner<Integer> spinnerDni;

    @FXML
    public void handleGeneratePlan(ActionEvent event) {
        try {
            int dni = (spinnerDni.getValueFactory() != null) ? spinnerDni.getValue() : 3;

            // POPRAWIONA LOGIKA SYSTEMU:
            String system;
            if (dni <= 3) {
                system = "FBW"; // 1-3 dni
            } else if (dni == 4) {
                system = "UPPER/LOWER"; // 4 dni
            } else {
                system = "PPL"; // 5+ dni (Push/Pull/Legs)
            }

            // Nadpisanie stażem: Początkujący zawsze FBW (bezpieczeństwo)
            if (rbStazPoczatkujacy.isSelected()) {
                system = "FBW";
            }

            String goal = "MASA";
            if (rbGoalStrength.isSelected()) goal = "SIŁA";
            else if (rbGoalReduction.isSelected()) goal = "REDUKCJA";

            int equipmentLevel = 1;
            if (rbHantle.isSelected()) equipmentLevel = 2;
            else if (rbSiłownia.isSelected()) equipmentLevel = 3;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/menu1/result-view.fxml"));
            Parent root = loader.load();

            ResultController resultController = loader.getController();
            resultController.initData(
                    dni, system,
                    cbBólKolana.isSelected(), cbBólPlecy.isSelected(), cbBólBarki.isSelected(),
                    goal, (int) sliderRegeneracja.getValue(), equipmentLevel
            );

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.show();

        } catch (Exception e) {
            showError("Błąd", "Wystąpił błąd podczas generowania: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}