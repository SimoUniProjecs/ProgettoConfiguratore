package com.example.configuratoreautoonline;

import Classi.Configurazione;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.time.LocalDate;

public class EditDateController {

    @FXML
    private DatePicker datePicker;

    private Configurazione configurazione;

    public void setConfigurazione(Configurazione configurazione) {
        this.configurazione = configurazione;
        datePicker.setValue(LocalDate.parse(configurazione.getDataPreventivo()));
    }

    @FXML
    private void handleSaveButtonAction() {
        LocalDate selectedDate = datePicker.getValue();
        configurazione.setDataPreventivo(selectedDate.toString());
        closeStage();
    }

    @FXML
    private void handleCancelButtonAction() {
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) datePicker.getScene().getWindow();
        stage.close();
    }
}
