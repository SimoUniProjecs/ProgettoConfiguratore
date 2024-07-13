package com.example.configuratoreautoonline;

import Classi.Comunicazione;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

// controllore utilizzato per il pop-up fxml con cui si può rispondere ad un certo messaggio
public class ReplyController {
    @FXML
    private TextField titoloField;

    @FXML
    private TextArea testoField;

    private Comunicazione comunicazione;
    private boolean replySent = false;
    private Stage parentStage;

    // funzioni per inizializzare il messaggio
    public void setComunicazione(Comunicazione comunicazione) {
        this.comunicazione = comunicazione;
        this.titoloField.setText("Re: " + comunicazione.getTitolo());
    }

    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }

    // funzione per creare il messaggio e gestire gli errori
    @FXML
    private void handleSendReply() {
        String titolo = titoloField.getText();
        String testo = testoField.getText();

        if (titolo.isEmpty() || testo.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campi vuoti");
            alert.setHeaderText(null);
            alert.setContentText("Compila tutti i campi per inviare un messaggio.");
            alert.showAndWait();
            return;
        }

        Comunicazione nuovaComunicazione = new Comunicazione();
        nuovaComunicazione.setMittente(UserSession.getInstance().getEmail());
        nuovaComunicazione.setDestinatario(comunicazione.getMittente());
        nuovaComunicazione.setTitolo(titolo);
        nuovaComunicazione.setTesto(testo);

        replySent = true;
        comunicazione = nuovaComunicazione;
        Stage stage = (Stage) titoloField.getScene().getWindow();
        stage.close();

        if (parentStage != null) {
            parentStage.close();
        }
    }

    public boolean isReplySent() {
        return replySent;
    }

    public Comunicazione getReplyComunicazione() {
        return comunicazione;
    }
}
