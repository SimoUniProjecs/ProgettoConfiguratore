package com.example.configuratoreautoonline;

import Enums.Concessionari;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class UserDetailsController {

    @FXML
    private Label emailLabel;
    @FXML
    private Label nomeLabel;
    @FXML
    private Label cognomeLabel;
    @FXML
    private Label numeroLabel;
    @FXML
    private Label codiceFiscaleLabel;
    @FXML
    private Label cittaLabel;
    @FXML
    private Label viaLabel;
    @FXML
    private Label provinciaLabel;
    @FXML
    private Label civicoLabel;
    @FXML
    private Label permessiLabel;
    @FXML
    private ComboBox<String> concessionarioComboBox;

    @FXML
    public void initialize() {
        // Carica i dati dell'utente dalla sessione
        UserSession session = UserSession.getInstance();
        emailLabel.setText(session.getEmail());
        nomeLabel.setText(session.getNome());
        cognomeLabel.setText(session.getCognome());
        numeroLabel.setText(session.getNumero());
        codiceFiscaleLabel.setText(session.getCodiceFiscale());
        cittaLabel.setText(session.getCitta());
        viaLabel.setText(session.getVia());
        provinciaLabel.setText(session.getProvincia());
        civicoLabel.setText(String.valueOf(session.getCivico()));
        permessiLabel.setText(String.valueOf(session.getPermessi()));

        // Popola la ComboBox con i nomi dei concessionari
        for (Concessionari concessionario : Concessionari.values()) {
            concessionarioComboBox.getItems().add(concessionario.getNome());
        }

        // Imposta il concessionario corrente come selezionato
        concessionarioComboBox.setValue(session.getConcessionario().getNome());
    }

    @FXML
    private void handleSave() {
        String selectedNome = concessionarioComboBox.getValue();
        for (Concessionari concessionario : Concessionari.values()) {
            if (concessionario.getNome().equals(selectedNome)) {
                UserSession session = UserSession.getInstance();
                session.setConcessionario(concessionario);
                break;
            }
        }
        Stage stage = (Stage) concessionarioComboBox.getScene().getWindow();
        stage.close();
    }
}
