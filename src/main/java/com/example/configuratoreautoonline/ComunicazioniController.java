package com.example.configuratoreautoonline;

import Classi.Comunicazione;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ComunicazioniController {
    @FXML
    private ListView<Label> comunicazioniListView;

    @FXML
    private Button homeButton;

    @FXML
    private TextField emailField;

    @FXML
    private TextField titoloField;

    @FXML
    private TextArea testoField;

    private List<Comunicazione> comunicazioni;

    private static ComunicazioniController instance;

    public ComunicazioniController() {
        instance = this;
    }

    public static ComunicazioniController getInstance() {
        return instance;
    }

    public void initialize() {
        ObjectMapper objectMapper = new ObjectMapper();
        String userEmail = UserSession.getInstance().getEmail();

        if ("segretaria@example.com".equals(userEmail)) {
            emailField.setVisible(true);
        }

        try {
            File file = new File("public/res/data/comunicazioni.json");
            if (file.length() != 0) {
                comunicazioni = objectMapper.readValue(file, new TypeReference<List<Comunicazione>>() {});
            } else {
                comunicazioni = new ArrayList<>();
            }
        } catch (IOException e) {
            comunicazioni = new ArrayList<>();
            e.printStackTrace();
        }

        showInbox();
    }

    private void showInbox() {
        // Ottiene l'email dell'utente corrente dalla sessione utente singleton
        String userEmail = UserSession.getInstance().getEmail();

        // Pulisce la lista delle comunicazioni nella ListView
        comunicazioniListView.getItems().clear();

        // Filtra le comunicazioni per l'email dell'utente corrente
        List<Comunicazione> filteredComunicazioni = comunicazioni.stream()
                .filter(comunicazione -> comunicazione.getDestinatario().equals(userEmail))
                .collect(Collectors.toList());

        // Itera sulle comunicazioni filtrate e aggiunge etichette alla ListView
        for (Comunicazione comunicazione : filteredComunicazioni) {
            // Crea una Label che visualizza il mittente, il titolo e il testo della comunicazione
            Label label = new Label(comunicazione.getMittente() + " - " + comunicazione.getTitolo() + ": " + comunicazione.getTesto());
            // Imposta la comunicazione come dati utente della Label
            label.setUserData(comunicazione);
            // Aggiunge la Label alla ListView delle comunicazioni
            comunicazioniListView.getItems().add(label);
        }
    }

    private void showSent() {
        // Ottiene l'email dell'utente corrente dalla sessione utente singleton
        String userEmail = UserSession.getInstance().getEmail();

        // Pulisce la lista delle comunicazioni nella ListView
        comunicazioniListView.getItems().clear();

        // Filtra le comunicazioni per l'email dell'utente corrente come mittente
        List<Comunicazione> filteredComunicazioni = comunicazioni.stream()
                .filter(comunicazione -> comunicazione.getMittente().equals(userEmail))
                .collect(Collectors.toList());

        // Itera sulle comunicazioni filtrate e aggiunge etichette alla ListView
        for (Comunicazione comunicazione : filteredComunicazioni) {
            // Crea una Label che visualizza il destinatario, il titolo e il testo della comunicazione
            Label label = new Label(comunicazione.getDestinatario() + " - " + comunicazione.getTitolo() + ": " + comunicazione.getTesto());
            // Imposta la comunicazione come dati utente della Label
            label.setUserData(comunicazione);
            // Aggiunge la Label alla ListView delle comunicazioni
            comunicazioniListView.getItems().add(label);
        }
    }

    @FXML
    private void handleDeleteMessage() {
        Label selectedLabel = comunicazioniListView.getSelectionModel().getSelectedItem();
        if (selectedLabel != null) {
            Comunicazione selectedComunicazione = (Comunicazione) selectedLabel.getUserData();
            comunicazioniListView.getItems().remove(selectedLabel);

            if (comunicazioni.remove(selectedComunicazione)) {
                saveComunicazioni();
            }
        } else { // Avvisa in caso di errori
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nessuna selezione");
            alert.setHeaderText(null);
            alert.setContentText("Seleziona un messaggio da eliminare.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleSendMessage() {
        // Ottiene il titolo e il testo del messaggio dalle relative caselle di testo
        String titolo = titoloField.getText();
        String testo = testoField.getText();

        // Imposta un destinatario predefinito (segretaria@example.com) se l'utente corrente è la segretaria
        String destinatario = "segretaria@example.com";

        // Se l'utente corrente non è la segretaria, prende l'email del destinatario dalla casella di testo
        if ("segretaria@example.com".equals(UserSession.getInstance().getEmail())) {
            destinatario = emailField.getText();
            // Controlla se il campo dell'email è vuoto e mostra un avviso se necessario
            if (destinatario.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Campo vuoto");
                alert.setHeaderText(null);
                alert.setContentText("Inserisci l'email del destinatario.");
                alert.showAndWait();
                return; // Esce dal metodo se il campo è vuoto
            }
        }

        // Controlla se il titolo o il testo del messaggio sono vuoti e mostra un avviso se necessario
        if (titolo.isEmpty() || testo.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campi vuoti");
            alert.setHeaderText(null);
            alert.setContentText("Compila tutti i campi per inviare un messaggio.");
            alert.showAndWait();
            return; // Esce dal metodo se uno dei campi è vuoto
        }

        // Crea una nuova istanza di Comunicazione e imposta mittente, destinatario, titolo e testo
        Comunicazione nuovaComunicazione = new Comunicazione();
        nuovaComunicazione.setMittente(UserSession.getInstance().getEmail());
        nuovaComunicazione.setDestinatario(destinatario);
        nuovaComunicazione.setTitolo(titolo);
        nuovaComunicazione.setTesto(testo);

        // Se la lista di comunicazioni è nulla, crea una nuova lista
        if (comunicazioni == null) {
            comunicazioni = new ArrayList<>();
        }

        // Aggiunge la nuova comunicazione alla lista di comunicazioni
        comunicazioni.add(nuovaComunicazione);

        // Salva le comunicazioni (presumibilmente su file o su un repository)
        saveComunicazioni();

        // Pulisce i campi del titolo e del testo dopo l'invio del messaggio
        titoloField.clear();
        testoField.clear();

        // Se l'utente corrente è la segretaria, pulisce anche il campo dell'email
        if ("segretaria@example.com".equals(UserSession.getInstance().getEmail())) {
            emailField.clear();
        }

        // Mostra le comunicazioni inviate aggiornate nella ListView
        showSent();
    }

    public void addComunicazione(Comunicazione comunicazione) {
        comunicazioni.add(comunicazione);
        saveComunicazioni();
    }

    private void saveComunicazioni() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("public/res/data/comunicazioni.json"), comunicazioni);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleShowInbox() {
        showInbox();
    }

    @FXML
    private void handleShowSent() {
        showSent();
    }

    @FXML
    private void handleGoHome(ActionEvent event) {
        try {
            Parent homeView = FXMLLoader.load(getClass().getResource("/com/example/configuratoreautoonline/Home-view.fxml"));
            Scene homeScene = new Scene(homeView);
            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.setScene(homeScene);

            stage.show();
            stage.setFullScreen(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReplyMessage() {
        // Ottiene l'etichetta selezionata dalla ListView delle comunicazioni
        Label selectedLabel = comunicazioniListView.getSelectionModel().getSelectedItem();

        // Controlla se è stata effettuata una selezione
        if (selectedLabel != null) {
            // Ottiene l'oggetto Comunicazione associato ai dati utente dell'etichetta selezionata
            Comunicazione selectedComunicazione = (Comunicazione) selectedLabel.getUserData();

            try {
                // Carica la vista di risposta (reply-view.fxml) utilizzando un FXMLLoader
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/configuratoreautoonline/reply-view.fxml"));
                Parent root = loader.load();

                // Ottiene il controller della vista di risposta
                ReplyController controller = loader.getController();
                // Imposta la comunicazione da rispondere nel controller
                controller.setComunicazione(selectedComunicazione);

                // Crea una nuova finestra per la vista di risposta
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL); // Modalità di finestra modale
                stage.initStyle(StageStyle.UTILITY); // Stile della finestra
                stage.setScene(new Scene(root)); // Imposta la scena con la radice della vista
                stage.showAndWait(); // Mostra la finestra e attende la sua chiusura

                // Controlla se la risposta è stata inviata dal controller della vista di risposta
                if (controller.isReplySent()) {
                    // Ottiene la comunicazione di risposta dal controller
                    Comunicazione reply = controller.getReplyComunicazione();
                    // Aggiunge la comunicazione di risposta alla lista delle comunicazioni
                    addComunicazione(reply);
                    // Aggiorna la vista della casella di posta in arrivo
                    showInbox();
                }
            } catch (IOException e) {
                e.printStackTrace(); // Gestisce eventuali eccezioni durante il caricamento della vista di risposta
            }
        } else {
            // Mostra un avviso se nessun messaggio è stato selezionato per rispondere
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nessuna selezione");
            alert.setHeaderText(null);
            alert.setContentText("Seleziona un messaggio a cui rispondere.");
            alert.showAndWait();
        }
    }
    @FXML
    private void handleViewMessage() {
        Label selectedLabel = comunicazioniListView.getSelectionModel().getSelectedItem();
        if (selectedLabel != null) {
            Comunicazione selectedComunicazione = (Comunicazione) selectedLabel.getUserData();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/configuratoreautoonline/view-message-view.fxml"));
                Parent root = loader.load();

                ViewMessageController controller = loader.getController();
                controller.setComunicazione(selectedComunicazione);

                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UTILITY);
                stage.setScene(new Scene(root));
                stage.showAndWait();

                if (controller.isMessageDeleted()) {
                    comunicazioni.remove(selectedComunicazione);
                    saveComunicazioni();
                    showInbox();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
