package com.example.configuratoreautoonline;

import Classi.Configurazione;
import Enums.Concessionari;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

// controllore per la pagina fxml del cliente in cui può vedere i dettagli dell'ordine e modificare la sede del concessionario
// presso il quale vuole ricevere la vettura
public class ModificaOrdineController {

    @FXML
    private ComboBox<String> concessionarioComboBox;
    @FXML
    private Label idLabel;
    @FXML
    private Label marcaLabel;
    @FXML
    private Label modelloLabel;
    @FXML
    private Label coloreLabel;
    @FXML
    private Label motorizzazioneLabel;
    @FXML
    private Label optionalsLabel;
    @FXML
    private Label prezzoLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label dataArrivoLabel;

    private Configurazione configurazione;

    @FXML
    public void initialize() {
        for (Concessionari concessionario : Concessionari.values()) {
            concessionarioComboBox.getItems().add(concessionario.getNome());
        }
    }

    // inizializza i campi con i dettagli della configurazione
    public void setConfigurazione(Configurazione configurazione) {
        this.configurazione = configurazione;
        idLabel.setText(String.valueOf(configurazione.getIdConfigurazione()));
        marcaLabel.setText(configurazione.getMarcaAutomobile());
        modelloLabel.setText(configurazione.getModelloAutomobile());
        coloreLabel.setText(configurazione.getColore());
        motorizzazioneLabel.setText(configurazione.getMotorizzazione().getAlimentazione());
        optionalsLabel.setText(String.join(", ", configurazione.getOptionals()));
        prezzoLabel.setText(String.valueOf(configurazione.getPrezzo()) + " €");
        emailLabel.setText(configurazione.getEmailCliente());
        dataArrivoLabel.setText(configurazione.getDataArrivo());

        concessionarioComboBox.setValue(configurazione.getLuogoConcessionario().getNome());
    }

    // funzione di supporto per salvare le modifiche ( cambio sede )
    @FXML
    private void handleSave() {
        String selectedNome = concessionarioComboBox.getValue();
        for (Concessionari concessionario : Concessionari.values()) {
            if (concessionario.getNome().equals(selectedNome)) {
                configurazione.setLuogoConcessionario(concessionario);
                break;
            }
        }
        Stage stage = (Stage) concessionarioComboBox.getScene().getWindow();
        stage.close();
    }
}
