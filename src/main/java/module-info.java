module com.example.reteasocialafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.jshell;


    opens com.example.reteasocialafx to javafx.fxml;
    exports com.example.reteasocialafx;
    exports com.example.reteasocialafx.controller;
    opens com.example.reteasocialafx.controller to javafx.fxml;
}