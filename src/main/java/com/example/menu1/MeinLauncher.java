package com.example.menu1;

import javafx.application.Application;

public class
MeinLauncher {
    public static void main(String[] args) {
        // 1. NAJPIERW inicjalizacja bazy
        DatabaseMenager dbManager = new DatabaseMenager();
        dbManager.init();

        // 2. POTEM uruchomienie interfejsu
        Application.launch(MeinApplication.class, args);
    }
}