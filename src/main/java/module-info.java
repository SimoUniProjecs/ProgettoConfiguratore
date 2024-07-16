module com.example.configuratoreautoonline {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires kernel;
    requires layout;
    requires io;


    opens com.example.configuratoreautoonline to javafx.fxml;
    exports com.example.configuratoreautoonline;
    exports Classi;
    exports Enums;
    opens Classi to javafx.fxml;
}