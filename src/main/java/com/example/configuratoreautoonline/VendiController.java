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

    @FXML
    public void initialize() {
        stage = new Stage();
    }
    @FXML
    private void handleHomeButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/configuratoreautoonline/Home-view.fxml"));
            Parent root = loader.load();

            // Crea una nuova scena con il root caricato
            Scene newScene = new Scene(root);

            // Ottieni lo Stage dalla scena corrente e chiudilo
            Stage currentStage = (Stage) imageView.getScene().getWindow();
            currentStage.close();

            // Crea un nuovo Stage per la nuova scena
            Stage newStage = new Stage();
            newStage.setScene(newScene);
            newStage.show();
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

    private Integer safelyParseInteger(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + input);
            return null; // Return null or handle accordingly
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

                // Crea un nuovo oggetto JSON per l'auto
                ObjectNode auto = mapper.createObjectNode();
                auto.put("marca", marcaTxt.getText());
                auto.put("modello", modelloTxt.getText());
                auto.put("alimentazione", carburanteChoiceBox.getValue());
                auto.put("cambio", trasmissioniChoiceBox.getValue());
                auto.put("anno", immatricolazione.getValue().toString());
                auto.put("km", kmTxt.getText());

                // Gestione dell'input per il campo proprietari
                Integer proprietari = safelyParseInteger(proprietariTxt.getText());
                if (proprietari == null) {
                    showAlert("Errore", "Il numero di proprietari deve essere un valore numerico valido.");
                    return;
                }
                auto.put("proprietari", proprietari);

                // Gestione dell'input per il campo condizioni (statoChoiceBox)
                if (statoChoiceBox.getValue() == null) {
                    showAlert("Errore", "Selezionare uno stato dall'elenco.");
                    return;
                }
                auto.put("condizioni", statoChoiceBox.getValue());
                auto.put("prezzo", -1);
                autoUsate.add(auto);
                mapper.writeValue(file, root);
            } catch (IOException e) {
                e.printStackTrace();  // Improve error handling based on your application needs
            }

            // Mostra una conferma
            showAlert("Richiesta preventivo", "L'auto è inserita nel database delle richieste, verrà contattato al più presto per un preventivo!");
        } else {
            showAlert("Errore", "Compilare tutti i campi in maniera corretta per richiedere un preventivo!");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert;
        if(title.equals("Richiesta preventivo")){
            alert = new Alert(Alert.AlertType.INFORMATION);
        }else{
            alert = new Alert(Alert.AlertType.ERROR);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
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