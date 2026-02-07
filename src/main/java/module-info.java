module com.example.menu1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;
    requires javafx.graphics;
    requires jbcrypt;

    opens com.example.menu1 to javafx.fxml;
    exports com.example.menu1;
    exports dao;
    opens dao to javafx.fxml;
    exports controller;
    opens controller to javafx.fxml;
    exports model;
    opens model to javafx.fxml, javafx.base;
}
