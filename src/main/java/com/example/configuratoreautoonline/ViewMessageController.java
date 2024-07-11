package com.example.configuratoreautoonline;

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
    private Label mittenteLabel;

    @FXML
    private Label titoloLabel;

    @FXML
    private TextArea testoArea;

    private Comunicazione comunicazione;
    private boolean messageDeleted = false;

    public void setComunicazione(Comunicazione comunicazione) {
        this.comunicazione = comunicazione;
        this.mittenteLabel.setText("Mittente: " + comunicazione.getMittente());
        this.titoloLabel.setText("Titolo: " + comunicazione.getTitolo());
        this.testoArea.setText(comunicazione.getTesto());
    }

    @FXML
    private void handleDeleteMessage() {
        messageDeleted = true;
        Stage stage = (Stage) mittenteLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleReplyMessage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/configuratoreautoonline/reply-view.fxml"));
            Parent root = loader.load();

            ReplyController controller = loader.getController();
            controller.setComunicazione(comunicazione);
            controller.setParentStage((Stage) mittenteLabel.getScene().getWindow());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            if (controller.isReplySent()) {
                Comunicazione reply = controller.getReplyComunicazione();
                ComunicazioniController.getInstance().addComunicazione(reply);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) mittenteLabel.getScene().getWindow();
        stage.close();
    }

    public boolean isMessageDeleted() {
        return messageDeleted;
    }
}
