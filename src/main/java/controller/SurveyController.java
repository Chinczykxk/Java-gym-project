package controller;

import dao.ExerciseDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import model.Exercise;
import model.PlanResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SurveyController {

    @FXML private CheckBox cbGoalMass, cbGoalReduction, cbGoalStrength;
    @FXML private CheckBox cbEquipGym, cbEquipDumbbells, cbEquipBands, cbEquipBodyweight, cbEquipMachines;
    @FXML private CheckBox cbLimitKnee, cbLimitBack, cbLimitTime, cbLimitShoulder, cbLimitMobility;
    @FXML private CheckBox cbMuscleChest, cbMuscleBack, cbMuscleLegs, cbMuscleShoulders, cbMuscleArms, cbMuscleCore;

    @FXML
    public void handleGeneratePlan(ActionEvent event) {
        System.out.println("DEBUG: Rozpoczynam generowanie planu...");

        List<Exercise> wygenerowaneCwiczenia = generujPlanNaPodstawieAnkiety();

        System.out.println("DEBUG: Znaleziono pasujących ćwiczeń: " + wygenerowaneCwiczenia.size());

        PlanResult finalPlan = new PlanResult("Personalny Plan Treningowy", wygenerowaneCwiczenia);
        otworzWidokWynikow(event, finalPlan);
    }

    private List<Exercise> generujPlanNaPodstawieAnkiety() {
        List<Exercise> pasujaceCwiczenia = new ArrayList<>();

        int userMaxInjuryRisk = 5;
        int userEquipmentLevel = 1;

        if (cbLimitKnee.isSelected() || cbLimitBack.isSelected() || cbLimitShoulder.isSelected()) {
            userMaxInjuryRisk = 3;
        }

        if (cbEquipGym.isSelected() || cbEquipMachines.isSelected()) {
            userEquipmentLevel = 5; // Dostęp do wszystkiego
        } else if (cbEquipDumbbells.isSelected()) {
            userEquipmentLevel = 3;
        }

        ExerciseDao dao = new ExerciseDao();
        List<Exercise> wszystkieZBazy = dao.getAllExercises();

        for (Exercise e : wszystkieZBazy) {
            boolean sprzetOk = e.getEquipmentLevel() <= userEquipmentLevel;
            boolean ryzykoOk = e.getInjuryRisk() <= userMaxInjuryRisk;

            if (sprzetOk && ryzykoOk) {
                // Filtracja po nazwach (upewnij się, że w bazie masz polskie nazwy!)
                String name = e.getName().toLowerCase();

                boolean dodaj = false;
                if (cbMuscleChest.isSelected() && (name.contains("klatka") || name.contains("pompki"))) dodaj = true;
                if (cbMuscleLegs.isSelected() && (name.contains("nogi") || name.contains("przysiad"))) dodaj = true;
                if (cbMuscleBack.isSelected() && (name.contains("plecy") || name.contains("podciąganie"))) dodaj = true;

                // Jeśli użytkownik nie wybrał partii, ale pasuje do celu (np. redukcja)
                if (!dodaj && cbGoalReduction.isSelected() && e.getIntensity() >= 5) dodaj = true;

                // DODAJEMY: Jeśli nic nie zaznaczono, po prostu dodaj parę pasujących sprzętowo
                if (!cbMuscleChest.isSelected() && !cbMuscleLegs.isSelected() && !cbMuscleBack.isSelected()) {
                    dodaj = true;
                }

                if (dodaj && !pasujaceCwiczenia.contains(e)) {
                    pasujaceCwiczenia.add(e);
                }
            }
        }
        return pasujaceCwiczenia;
    }

    private void otworzWidokWynikow(ActionEvent event, PlanResult plan) {
        try {
            // Upewnij się, że ścieżka do FXML jest poprawna!
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/menu1/result-view.fxml"));
            Parent root = loader.load();

            ResultController resultController = loader.getController();
            resultController.setPlanData(plan.getPlanName(), plan.getExercises());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}