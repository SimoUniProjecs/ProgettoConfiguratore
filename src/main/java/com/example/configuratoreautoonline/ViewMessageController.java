package com.example.configuratoreautoonline;

// Importazioni necessarie per la gestione delle GUI e delle operazioni di input/output
import Classi.Comunicazione;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ViewMessageController {

    @FXML
    private Label mittenteLabel; // Etichetta per visualizzare il mittente del messaggio

    @FXML
    private Label titoloLabel; // Etichetta per visualizzare il titolo del messaggio

    @FXML
    private TextArea testoArea; // Area di testo per visualizzare il contenuto del messaggio

    private Comunicazione comunicazione; // Variabile per memorizzare l'oggetto Comunicazione corrente
    private boolean messageDeleted = false; // Flag per indicare se il messaggio è stato cancellato

    // Metodo per impostare l'oggetto Comunicazione e aggiornare l'interfaccia utente
    public void setComunicazione(Comunicazione comunicazione) {
        this.comunicazione = comunicazione;
        this.mittenteLabel.setText("Mittente: " + comunicazione.getMittente());
        this.titoloLabel.setText("Titolo: " + comunicazione.getTitolo());
        this.testoArea.setText(comunicazione.getTesto());
    }

    // Metodo chiamato quando l'utente decide di cancellare il messaggio
    @FXML
    private void handleDeleteMessage() {
        messageDeleted = true; // Imposta il flag a true
        Stage stage = (Stage) mittenteLabel.getScene().getWindow(); // Ottiene la finestra corrente
        stage.close(); // Chiude la finestra
    }

    // Metodo chiamato quando l'utente decide di rispondere al messaggio
    @FXML
    private void handleReplyMessage() {
        try {
            // Carica il file FXML per la vista di risposta
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/configuratoreautoonline/reply-view.fxml"));
            Parent root = loader.load();

            // Ottiene il controller della vista di risposta
            ReplyController controller = loader.getController();
            controller.setComunicazione(comunicazione); // Imposta l'oggetto Comunicazione nel controller di risposta
            controller.setParentStage((Stage) mittenteLabel.getScene().getWindow()); // Imposta la finestra principale nel controller di risposta

            // Crea e configura una nuova finestra di dialogo per la risposta
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Imposta la modalità della finestra a modale
            stage.initStyle(StageStyle.UTILITY); // Imposta lo stile della finestra a utilità
            stage.setScene(new Scene(root)); // Imposta la scena della finestra
            stage.showAndWait(); // Mostra la finestra e aspetta che venga chiusa

            // Se la risposta è stata inviata, aggiunge la risposta alla lista delle comunicazioni
            if (controller.isReplySent()) {
                Comunicazione reply = controller.getReplyComunicazione();
                ComunicazioniController.getInstance().addComunicazione(reply);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Stampa lo stack trace in caso di eccezione
        }
    }

    // Metodo chiamato quando l'utente decide di chiudere la finestra senza cancellare il messaggio
    @FXML
    private void handleClose() {
        Stage stage = (Stage) mittenteLabel.getScene().getWindow(); // Ottiene la finestra corrente
        stage.close(); // Chiude la finestra
    }

    // Metodo per verificare se il messaggio è stato cancellato
    public boolean isMessageDeleted() {
        return messageDeleted;
    }
}
