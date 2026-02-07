package com.example.menu1;

import model.SurveyResult;
import model.Equipment;
import model.Goal;

public class PlanSelector {
    public String decidePlanTag(SurveyResult result) {
        // Poprawione nazwy zgodne z Twoim nowym Enumem Equipment
        boolean wantsMass = result.getGoals().contains(Goal.MASS);
        boolean hasGym = result.getEquipment().contains(Equipment.FULL_GYM);

        if (hasGym && wantsMass) {
            return "GYM_MASS";
        } else if (!hasGym && wantsMass) {
            return "HOME_MASS";
        } else if (hasGym) {
            return "GYM_GENERAL";
        } else {
            return "HOME_GENERAL";
        }
    }
}