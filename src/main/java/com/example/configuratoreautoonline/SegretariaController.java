package com.example.configuratoreautoonline;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SegretariaController {

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

    // Metodo chiamato quando l'utente seleziona Audi
    @FXML
    void handleSceltaAudi() {
        impostaTesto("Audi"); // Imposta il testo nella Label per mostrare la marca selezionata
    }

    // Metodo chiamato quando l'utente seleziona Alfa Romeo
    @FXML
    void handleSceltaAlfaRomeo() {
        impostaTesto("Alfa Romeo"); // Imposta il testo nella Label per mostrare la marca selezionata
    }

    // Metodo chiamato quando l'utente seleziona BMW
    @FXML
    void handleSceltaBMW() {
        impostaTesto("BMW"); // Imposta il testo nella Label per mostrare la marca selezionata
    }

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

    @FXML
    private void handleValutaUsati(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();

        changeScene("/com/example/configuratoreautoonline/valutaUsati.fxml", currentStage);
    }

    @FXML
    private void handleHomeButton(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();

        changeScene("/com/example/configuratoreautoonline/Home-view.fxml", currentStage);
    }

    private void changeScene(String fxmlFile, Stage currentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            currentStage.setScene(scene);
            currentStage.show();
        } catch (Exception e) {
            showAlert("Error loading scene", "Cannot load scene from file: " + fxmlFile + "\n" + e.getMessage());
            e.printStackTrace();
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
        // Da implementare
    }
}