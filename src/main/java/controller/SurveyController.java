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

    @FXML private CheckBox cbGoalMass, cbGoalReduction, cbGoalStrength;
    @FXML private CheckBox cbStazPoczatkujacy, cbStazSredni, cbStazZaawansowany;
    @FXML private CheckBox cbBólBarki, cbBólPlecy, cbBólKolana, cbBólNadgarstki;

    // DODAJ TE POLA W FXML (lub upewnij się, że ich nazwy się zgadzają)
    @FXML private RadioButton rbDom, rbHantle, rbSiłownia;

    @FXML private Slider sliderRegeneracja;
    @FXML private Spinner<Integer> spinnerDni;

    @FXML
    public void handleGeneratePlan(ActionEvent event) {
        try {
            // 1. Liczba dni
            int dni = (spinnerDni.getValueFactory() != null) ? spinnerDni.getValue() : 3;

            // 2. Wybór systemu
            String system;
            if (cbStazPoczatkujacy != null && cbStazPoczatkujacy.isSelected() || dni <= 2) {
                system = "FBW";
            } else if (dni == 4) {
                system = "UPPER/LOWER";
            } else {
                system = "SPLIT";
            }

            // 3. Określenie celu
            String goal = "MASA";
            if (cbGoalStrength != null && cbGoalStrength.isSelected()) goal = "SIŁA";
            else if (cbGoalReduction != null && cbGoalReduction.isSelected()) goal = "REDUKCJA";

            // 4. LOGIKA SPRZĘTU (Kluczowe dla filtra!)
            int equipmentLevel = 1; // Domyślnie dom (masa ciała)
            if (rbHantle != null && rbHantle.isSelected()) equipmentLevel = 2;
            else if (rbSiłownia != null && rbSiłownia.isSelected()) equipmentLevel = 3;

            // 5. Ładowanie widoku wynikowego
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/menu1/result-view.fxml"));
            Parent root = loader.load();

            ResultController resultController = loader.getController();

            // FIX: Przekazujemy teraz 8 parametrów zgodnie z nowym ResultControllerem
            resultController.initData(
                    dni,                // Parametr 1 (int)
                    system,             // Parametr 2 (String)
                    cbBólKolana != null && cbBólKolana.isSelected(), // 3 (boolean)
                    cbBólPlecy != null && cbBólPlecy.isSelected(),   // 4 (boolean)
                    cbBólBarki != null && cbBólBarki.isSelected(),   // 5 (boolean)
                    goal,               // 6 (String)
                    (int) sliderRegeneracja.getValue(), // 7 (int)
                    equipmentLevel      // Parametr 8 (int) - POZIOM SPRZĘTU
            );

            resultController.setRewardPoints(100);

            // 6. Zmiana sceny
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.show();

        } catch (IOException e) {
            showError("Błąd widoku", "Nie znaleziono pliku FXML: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showError("Błąd", "Wystąpił problem: " + e.getMessage());
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