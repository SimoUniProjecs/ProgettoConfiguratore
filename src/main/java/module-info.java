module com.example.configuratoreautoonline {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires kernel;
    requires layout;


    opens com.example.configuratoreautoonline to javafx.fxml;
    exports com.example.configuratoreautoonline;
}