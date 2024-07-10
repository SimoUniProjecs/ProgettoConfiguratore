package com.example.configuratoreautoonline;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SegretariaController {
    @FXML
    private AnchorPane pannelloAncora;
    @FXML
    private MenuButton sceltaMarca;

    @FXML
    private MenuItem sceltaAudi;

    @FXML
    private MenuItem sceltaAlfaRomeo;

    @FXML
    private MenuItem sceltaBMW;

    @FXML
    private Button aggiungi;

    @FXML
    private TextField modello;

    @FXML
    private Label textMarcaSeg;

    @FXML
    private TextArea listaOptionals;
    private void impostaTesto(String marca) {
        textMarcaSeg.setText(marca); // Imposta il testo nella Label per mostrare la marca selezionata
    }

    // Ritorna gli optional inseriti manualmente dall'utente
    String[] getOptional() {
        return listaOptionals.getText().split(","); // ',' come separatore
    }

    // Aggiunge un'auto al catalogo (da implementare)
    @FXML
    boolean aggiungiAlCatalogo() {
        // Implementazione dell'aggiunta dell'auto al catalogo
        return true; // Ritorna true se l'aggiunta è avvenuta con successo, altrimenti false
    }
    private void changeScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage currentStage = (Stage) pannelloAncora.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.show();
        } catch (Exception e) {
            showAlert("Error loading scene", "Cannot load scene from file: " + fxmlFile + "\n" + e.getMessage());
            e.printStackTrace(); // Stampa lo stack trace per il debug
        }
    }
    private void showAlert(String title, String s) {
        Alert alert = null;
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.show();
    }

    @FXML
    private void handleInserisciVeicolo(ActionEvent event) {
        // Da implementare perchè deve inserirsi nell'albero originale con tutte le relative foto annesse
    }

    public void handleValutaUsati(ActionEvent event) {
        changeScene("/com/example/configuratoreautoonline/valutaUsati.fxml");
    }

    public void handleSwitchHome(ActionEvent event) {
        changeScene("/com/example/configuratoreautoonline/Home-View.fxml");
    }

    public void onAcquistaBtnClicked(ActionEvent event) {
        System.out.println("Acquista button clicked");
    }

    public void onProssimoVeicoloClicked(ActionEvent event) {
        System.out.println("Prossimo veicolo button clicked");
    }
}