package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import com.example.menu1.ConfigUserDatabase;
public class SurveyController {

    @FXML private CheckBox cbGoalMass, cbGoalReduction, cbGoalStrength;
    @FXML private CheckBox cbStazPoczatkujacy, cbStazSredni, cbStazZaawansowany;
    @FXML private CheckBox cbBólBarki, cbBólPlecy, cbBólKolana, cbBólNadgarstki;
    @FXML private Slider sliderRegeneracja;
    @FXML private Spinner<Integer> spinnerDni;

    @FXML
    public void handleGeneratePlan(ActionEvent event) {
        try {
            // 1. Wybór systemu na podstawie stażu i dni
            String system;
            int dni = (spinnerDni.getValueFactory() != null) ? spinnerDni.getValue() : 3;

            if (cbStazPoczatkujacy != null && cbStazPoczatkujacy.isSelected() || dni <= 3) {
                system = "FBW";
            } else if (cbStazSredni != null && cbStazSredni.isSelected() || dni == 4) {
                system = "UPPER/LOWER";
            } else {
                system = "SPLIT";
            }

            // 2. Określenie celu
            String goal = "MASA";
            if (cbGoalStrength != null && cbGoalStrength.isSelected()) goal = "SIŁA";
            else if (cbGoalReduction != null && cbGoalReduction.isSelected()) goal = "REDUKCJA";

            // 3. Ładowanie widoku wynikowego
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/menu1/result-view.fxml"));
            Parent root = loader.load();

            // 4. PRZEKAZANIE DANYCH - tutaj najczęściej występują błędy
            ResultController resultController = loader.getController();

            // Inicjalizacja danych treningowych
            resultController.initData(
                    system,
                    cbBólKolana != null && cbBólKolana.isSelected(),
                    cbBólPlecy != null && cbBólPlecy.isSelected(),
                    cbBólBarki != null && cbBólBarki.isSelected(),
                    goal,
                    (int) sliderRegeneracja.getValue()
            );

            // Wywołanie metody nagrody (musisz ją mieć w ResultController!)
            resultController.setRewardPoints(100);

            // 5. Zmiana sceny
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.show();

        } catch (IOException e) {
            showError("Błąd widoku", "Nie znaleziono pliku FXML.");
            e.printStackTrace();
        } catch (Exception e) {
            showError("Błąd", "Wystąpił nieoczekiwany problem: " + e.getMessage());
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}