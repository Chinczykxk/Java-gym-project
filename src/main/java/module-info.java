module com.example.menu1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.menu1 to javafx.fxml;
    exports com.example.menu1;
}
