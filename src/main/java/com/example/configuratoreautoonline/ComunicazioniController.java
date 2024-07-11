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
        String userEmail = UserSession.getInstance().getEmail();
        comunicazioniListView.getItems().clear();
        List<Comunicazione> filteredComunicazioni = comunicazioni.stream()
                .filter(comunicazione -> comunicazione.getDestinatario().equals(userEmail))
                .collect(Collectors.toList());

        for (Comunicazione comunicazione : filteredComunicazioni) {
            Label label = new Label(comunicazione.getMittente() + " - " + comunicazione.getTitolo() + ": " + comunicazione.getTesto());
            label.setUserData(comunicazione);
            comunicazioniListView.getItems().add(label);
        }
    }

    private void showSent() {
        String userEmail = UserSession.getInstance().getEmail();
        comunicazioniListView.getItems().clear();
        List<Comunicazione> filteredComunicazioni = comunicazioni.stream()
                .filter(comunicazione -> comunicazione.getMittente().equals(userEmail))
                .collect(Collectors.toList());

        for (Comunicazione comunicazione : filteredComunicazioni) {
            Label label = new Label(comunicazione.getDestinatario() + " - " + comunicazione.getTitolo() + ": " + comunicazione.getTesto());
            label.setUserData(comunicazione);
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
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nessuna selezione");
            alert.setHeaderText(null);
            alert.setContentText("Seleziona un messaggio da eliminare.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleSendMessage() {
        String titolo = titoloField.getText();
        String testo = testoField.getText();
        String destinatario = "segretaria@example.com";

        if ("segretaria@example.com".equals(UserSession.getInstance().getEmail())) {
            destinatario = emailField.getText();
            if (destinatario.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Campo vuoto");
                alert.setHeaderText(null);
                alert.setContentText("Inserisci l'email del destinatario.");
                alert.showAndWait();
                return;
            }
        }

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
        nuovaComunicazione.setDestinatario(destinatario);
        nuovaComunicazione.setTitolo(titolo);
        nuovaComunicazione.setTesto(testo);

        if (comunicazioni == null) {
            comunicazioni = new ArrayList<>();
        }

        comunicazioni.add(nuovaComunicazione);
        saveComunicazioni();
        titoloField.clear();
        testoField.clear();
        if ("segretaria@example.com".equals(UserSession.getInstance().getEmail())) {
            emailField.clear();
        }
        showSent();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReplyMessage() {
        Label selectedLabel = comunicazioniListView.getSelectionModel().getSelectedItem();
        if (selectedLabel != null) {
            Comunicazione selectedComunicazione = (Comunicazione) selectedLabel.getUserData();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/configuratoreautoonline/reply-view.fxml"));
                Parent root = loader.load();

                ReplyController controller = loader.getController();
                controller.setComunicazione(selectedComunicazione);

                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UTILITY);
                stage.setScene(new Scene(root));
                stage.showAndWait();

                if (controller.isReplySent()) {
                    comunicazioni.add(controller.getReplyComunicazione());
                    saveComunicazioni();
                    showInbox();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
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
