module com.mycompany.personalfinanceapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.mycompany.personalfinanceapp to javafx.fxml;
    exports com.mycompany.personalfinanceapp;
}
