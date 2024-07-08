package com.example.configuratoreautoonline;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class VendiController {

    @FXML
    private ImageView imageView;

    @FXML
    private TextField marcaTxt;
    @FXML
    private TextField modelloTxt;
    @FXML
    private TextField kmTxt;
    @FXML
    private TextField proprietariTxt;
    @FXML
    private DatePicker immatricolazione;
    @FXML
    private ChoiceBox<Integer> statoChoiceBox;
    @FXML
    private ChoiceBox<String> trasmissioniChoiceBox;
    @FXML
    private ChoiceBox<String> carburanteChoiceBox;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleHomeButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/configuratoreautoonline/Home-view.fxml"));
            Parent root = loader.load();

            // Ottieni lo Stage dalla scena corrente
            Stage currentStage = (Stage) imageView.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore nel tornare alla Home");
            alert.setHeaderText(null);
            alert.setContentText("Si è verificato un errore nel tornare alla Home");
            alert.show();
        }
    }

    @FXML
    private void handleImageSelection(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        // Imposta il titolo del file chooser
        fileChooser.setTitle("Seleziona Immagine");

        // Imposta i filtri per mostrare solo file di immagine
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Apri la finestra di dialogo per selezionare il file
        File selectedFile = fileChooser.showOpenDialog(((Button) event.getSource()).getScene().getWindow());

        // Se un file è stato selezionato, carica l'immagine e impostala nell'ImageView
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);
        }
    }

    @FXML
    private void richiediPreventivo(ActionEvent event) {
        if (isValidInput()) {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File("public/res/data/datiAutoUsate.json");

            try {
                // Leggi il file JSON esistente o crea uno nuovo se non esiste
                ObjectNode root;
                if (file.exists()) {
                    root = (ObjectNode) mapper.readTree(file);
                } else {
                    root = mapper.createObjectNode();
                    root.set("datiAutoUsate", mapper.createArrayNode());
                }
                ArrayNode autoUsate = (ArrayNode) root.get("datiAutoUsate");

                System.out.println("Marca: " + marcaTxt.getText());
                System.out.println("Modello: " + modelloTxt.getText());
                System.out.println("Alimentazione: " + carburanteChoiceBox.getValue());
                System.out.println("Cambio: " + trasmissioniChoiceBox.getValue());
                System.out.println("Anno: " + immatricolazione.getValue().getYear());
                System.out.println("Km: " + kmTxt.getText());
                System.out.println("Condizioni: " + statoChoiceBox.getValue());

                // Crea un nuovo oggetto JSON per l'auto
                ObjectNode auto = mapper.createObjectNode();
                auto.put("marca", marcaTxt.getText());
                auto.put("modello", modelloTxt.getText());
                auto.put("alimentazione", carburanteChoiceBox.getValue());
                auto.put("cambio", trasmissioniChoiceBox.getValue());
                auto.put("anno", immatricolazione.getValue().toString());
                auto.put("km", kmTxt.getText());

                // Gestione dell'input per il campo proprietari
                try {
                    Integer proprietari = Integer.valueOf(proprietariTxt.getText());
                    auto.put("proprietari", proprietari);
                } catch (NumberFormatException e) {
                    // Gestione nel caso in cui il testo non possa essere convertito in Integer
                    System.err.println("Errore nella conversione di proprietariTxt in Integer: " + e.getMessage());
                    // Decidi come gestire l'input non valido
                    return;  // Esco dal metodo in caso di errore, puoi decidere di gestirlo diversamente
                }

                // Gestione dell'input per il campo condizioni (statoChoiceBox)
                try {
                    Integer condizioni = statoChoiceBox.getValue();
                    auto.put("condizioni", condizioni);
                } catch (NullPointerException e) {
                    System.err.println("StatoChoiceBox non è stato selezionato correttamente: " + e.getMessage());
                    return;  // Esco dal metodo in caso di errore, puoi decidere di gestirlo diversamente
                }

                auto.put("prezzo", -1);
                autoUsate.add(auto);
                mapper.writeValue(file, root);
            } catch (IOException e) {
                e.printStackTrace();  // Improve error handling based on your application needs
            }

            // Mostra una conferma
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Richiesta preventivo");
            alert.setHeaderText(null);
            alert.setContentText("L'auto è inserita nel database delle richieste, verrà contattato al più presto per un preventivo!");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText(null);
            alert.setContentText("Compilare tutti i campi in maniera corretta per richiedere un preventivo!");
            alert.show();
        }
    }
    private boolean isValidInput() {
        // Verifica che i campi numerici siano correttamente compilati
        boolean numericFieldsValid =
                proprietariTxt.getText().matches("^[0-9]{1,2}$") && // Da 1 a 2 cifre numeriche per i proprietari
                        kmTxt.getText().matches("^[0-9]{1,10}$"); // Da 1 a 10 cifre numeriche per i chilometri

        // Verifica che almeno uno dei campi testuali non sia vuoto
        boolean textFieldsValid =
                !marcaTxt.getText().isEmpty() ||
                        !modelloTxt.getText().isEmpty();

        // Verifica che le choicebox siano selezionate
        boolean choiceBoxesValid =
                statoChoiceBox.getValue() != null &&
                        trasmissioniChoiceBox.getValue() != null &&
                        carburanteChoiceBox.getValue() != null;

        boolean dateValid = immatricolazione.getValue() != null &&
                immatricolazione.getValue().getYear() <= LocalDate.now().getYear();

        // Restituisci true solo se tutti i controlli sono validi
        return numericFieldsValid && textFieldsValid && choiceBoxesValid && dateValid;
    }
}