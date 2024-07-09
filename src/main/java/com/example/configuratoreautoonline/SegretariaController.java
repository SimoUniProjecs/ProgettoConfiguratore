package com.example.configuratoreautoonline;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
        impostaTesto("Audi"); // Imposta il testo nella Text per mostrare la marca selezionata
    }

    // Metodo chiamato quando l'utente seleziona Alfa Romeo
    @FXML
    void handleSceltaAlfaRomeo() {
        impostaTesto("Alfa Romeo"); // Imposta il testo nella Text per mostrare la marca selezionata
    }

    // Metodo chiamato quando l'utente seleziona BMW
    @FXML
    void handleSceltaBMW() {
        impostaTesto("BMW"); // Imposta il testo nella Text per mostrare la marca selezionata
    }
    private void impostaTesto(String marca) {
        textMarcaSeg.setText(marca); // Imposta il testo nella Text per mostrare la marca selezionata
    }

    // Ritorna gli optional inseriti manualmente dall'utente
    String[] getOptional() {
        return listaOptionals.getText().split(","); // , come separatore
    }

    // Aggiunge un'auto al catalogo
    boolean aggiungiAlCatalogo(){

        return true; // TODO: Implementare
    }

    @FXML
    private void handleValutaUsati(ActionEvent event) {}

    @FXML
    private void handleInserisciVeicolo(ActionEvent event) {}
}