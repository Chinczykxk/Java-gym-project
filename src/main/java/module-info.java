module com.example.menu1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;
    requires javafx.graphics;
    requires jbcrypt;

    // Pozwala JavaFX na dostęp do klasy głównej (Application)
    opens com.example.menu1 to javafx.fxml;
    exports com.example.menu1;

    // Pozwala FXMLLoaderowi ładować Twoje kontrolery (KLUCZOWE)
    opens controller to javafx.fxml;
    exports controller;

    // Pozwala na dostęp do bazy i modeli (np. dla TableView w JavaFX)
    exports dao;
    opens dao to javafx.fxml;

    exports model;
    opens model to javafx.fxml, javafx.base;
}