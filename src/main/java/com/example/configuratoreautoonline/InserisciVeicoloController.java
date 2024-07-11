package com.example.configuratoreautoonline;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.List;

public class InserisciVeicoloController {
    @FXML
    private TextField marcaTxt;
    @FXML
    private TextField modelloTxt;
    @FXML
    private TextArea coloriTxt;
    @FXML
    private TextArea optionalsTxt;
    @FXML
    private TextArea motorizzazioniTxt;
    @FXML
    private Button caricaConfigurazioneBtn;
    @FXML
    private HBox imageContainer;
    private JsonNode datiModelliAuto;
    private Stage stage;

    // Carica datiModelliAuto.json in datiModelliAuto
    private void loadJsonData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File("public/res/data/datiModelliAuto.json");
            JsonNode root = objectMapper.readTree(file);
            datiModelliAuto = root.get("datiModelliAuto").get(0); // Prendiamo solo il primo elemento dell'array
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Errore di caricamento", "Impossibile caricare i dati del file JSON.");
        }
    }

    @FXML
    public void initialize() {
        loadJsonData();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void handleHomeButtonAction(ActionEvent event) {
        changeScene("/com/example/configuratoreautoonline/Home-view.fxml");
    }

    private void showAlert(String title, String content) {
        Alert alert;
        if(title.equals("Successo"))
            alert = new Alert(Alert.AlertType.INFORMATION);
        else
            alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleImageSelection(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        // Imposta il titolo del file chooser
        fileChooser.setTitle("Seleziona Immagini");

        // Imposta i filtri per mostrare solo file di immagine
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Apri la finestra di dialogo per selezionare i file
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(((Button) event.getSource()).getScene().getWindow());

        // Se dei file sono stati selezionati, carica le immagini e impostale nel VBox
        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            for (File selectedFile : selectedFiles) {
                // Copia l'immagine nel percorso desiderato (es. public/res/images/)
                String imageFileName = selectedFile.getName();
                String targetPath = "src/main/resources/img/" + imageFileName;

                // Crea un nuovo file nel percorso target
                File targetFile = new File(targetPath);

                try {
                    // Copia il file selezionato nel percorso target
                    Files.copy(selectedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    // Imposta l'immagine nel VBox
                    Image image = new Image(targetFile.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(200); // Imposta la larghezza desiderata
                    imageView.setPreserveRatio(true);
                    imageView.setSmooth(true);

                    // Aggiungi l'ImageView al VBox
                    imageContainer.getChildren().add(imageView);

                } catch (IOException e) {
                    showAlert("Errore", "Impossibile copiare l'immagine nel percorso desiderato.");
                    e.printStackTrace();
                }
            }
        }
    }

    private void changeScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage currentStage = (Stage) motorizzazioniTxt.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.show();
        } catch (Exception e) {
            showAlert("Error loading scene", "Cannot load scene from file: " + fxmlFile + "\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    // Scrivi la modifica nel json delle auto disponibili
    public void oncaricaConfigurazioneBtnClicked(ActionEvent event) {
        if (isValid()) {
            String marca = marcaTxt.getText().toLowerCase(); // Converte in lowercase
            String modello = modelloTxt.getText();
            List<String> colori = List.of(coloriTxt.getText().split(","));
            List<String> optionals = List.of(optionalsTxt.getText().split(","));
            List<String> motorizzazioni = List.of(motorizzazioniTxt.getText().split(","));

            String pathRadice;
            if (marcaEsistente(marca)) {
                pathRadice = "src/main/resources/json/" + marca + ".json";
            } else {
                pathRadice = "src/main/resources/json/" + marca + ".json";
            }

            System.out.println(marcaEsistente(marca));

            // Torna alla home
            //changeScene("/com/example/configuratoreautoonline/Home-view.fxml");

            // Mostra un messaggio di successo
            showAlert("Successo", "Auto aggiunta correttamente.");
        }
    }

    // Verifica se la marca esiste già nel JSON
    public boolean marcaEsistente(String marca) {
        loadJsonData();

        if (datiModelliAuto == null || datiModelliAuto.isEmpty()) {
            showAlert("Errore", "Impossibile caricare i dati del file JSON.");
            return false;
        }

        marca = marca.toLowerCase(); // Converte in lowercase per il confronto

        // Itera attraverso gli elementi di datiModelliAuto
        for (JsonNode marcaNode : datiModelliAuto) {
            Iterator<String> keys = marcaNode.fieldNames();
            while (keys.hasNext()) {
                String key = keys.next();
                // Confronta la chiave con il nome della marca convertito in lowercase
                if (key.equalsIgnoreCase(marca)) {
                    return true; // Trovata corrispondenza
                }
            }
        }
        return false; // Marca non trovata
    }

    // Verifica se i dati inseriti dall'utente sono validi
    public boolean isValid() {
        if (marcaTxt.getText().isEmpty() || modelloTxt.getText().isEmpty() || coloriTxt.getText().isEmpty() || optionalsTxt.getText().isEmpty() || motorizzazioniTxt.getText().isEmpty()) {
            showAlert("Errore", "Compilare tutti i campi.");
            return false;
        }
        return true;
    }
}