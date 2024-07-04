package com.example.configuratoreautoonline;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GestisciDipendentiController {

    @FXML
    private TextField emailFieldGD;

    @FXML
    private TextField numberFieldGD;

    @FXML
    private void handleSubmit() {
        String email = emailFieldGD.getText();
        String number = numberFieldGD.getText();
    }
}