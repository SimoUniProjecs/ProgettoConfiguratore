package com.example.configuratoreautoonline;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

// controller utilizzato per gestire la pagina in cui l'utente può vendere la sua auto e richiedere una valutazione

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
    private TextField proprietariTxt; // numero dei proprietari che hannno avuto l'auto
    @FXML
    private DatePicker immatricolazione;
    @FXML
    private ChoiceBox<Integer> statoChoiceBox; //stato dell'auto
    @FXML
    private ChoiceBox<String> trasmissioniChoiceBox;
    @FXML
    private ChoiceBox<String> carburanteChoiceBox;

    private String email;

    private Stage stage; // per il cambio della scena

    @FXML
    public void initialize() {
        stage = new Stage();
    }

    // funzione per tornare alla home page
    @FXML
    private void handleHomeButton(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        changeScene("/com/example/configuratoreautoonline/Home-view.fxml", currentStage);
    }

    // funzione di supporto per tornare alla home page
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

    // selezionare le immagini della propria auto da vendere
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
            // Copia l'immagine nel percorso desiderato (es. public/res/images/)
            String imageFileName = selectedFile.getName();
            String targetPath = "public/res/images/" + imageFileName;

            // Crea un nuovo file nel percorso target
            File targetFile = new File(targetPath);

            try {
                // Copia il file selezionato nel percorso target
                Files.copy(selectedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Imposta l'immagine nell'ImageView
                Image image = new Image(targetFile.toURI().toString());
                imageView.setImage(image);

            } catch (IOException e) {
                showAlert("Errore", "Impossibile copiare l'immagine nel percorso desiderato.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void richiediPreventivo(ActionEvent event) {
        if (isValidInput()) {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File("public/res/data/datiAutoUsate.json");

            // prendo i dati dalla sessione dello user
            UserSession session = UserSession.getInstance();
            email = session.getEmail();

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
                String imagePath = "public/res/images/" + new File(imageView.getImage().getUrl()).getName();
                auto.put("immagine", imagePath);
                auto.put("km", kmTxt.getText());
                auto.put("prezzo", -1); // Prezzo non specificato (da calcolare in seguito dalla segretaria)
                auto.put("email", email); // Email dell'utente che ha richiesto il preventivo (da recuperare dalla sessione
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
                // Aggiungi l'auto usata all'array di auto usate
                autoUsate.add(auto);

                // Scrivi l'oggetto JSON aggiornato nel file
                mapper.writeValue(file, root);

            } catch (IOException e) {
                showAlert("Errore", "Impossibile salvare i dati dell'auto usata.");
                e.printStackTrace();
            }

            // Mostra una conferma
            showAlert("Richiesta preventivo", "L'auto è inserita nel database delle richieste, verrà contattato al più presto per un preventivo!");
        } else {
            showAlert("Errore", "Compilare tutti i campi in maniera corretta per richiedere un preventivo!");
        }
    }

    // funzione per controllare i valori di json per i numeri
    private Integer safelyParseInteger(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + input);
            return null; // Return null or handle accordingly
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = null;
        if (title.equals("Richiesta preventivo"))
            alert = new Alert(Alert.AlertType.INFORMATION);
        else
            alert = new Alert(Alert.AlertType.ERROR);
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