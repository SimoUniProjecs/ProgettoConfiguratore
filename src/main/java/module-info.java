module com.example.configuratoreautoonline {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens com.example.configuratoreautoonline to javafx.fxml;
    exports com.example.configuratoreautoonline;
}