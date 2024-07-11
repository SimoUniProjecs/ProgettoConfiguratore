package com.example.configuratoreautoonline;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ValutaUsatiController extends Application {

    @FXML
    private AnchorPane pannelloAncora;
    @FXML
    private ImageView imageView;
    @FXML
    private Label marcaTxt;
    @FXML
    private Label modelloTxt;
    @FXML
    private Label kmTxt;
    @FXML
    private Label proprietariTxt;
    @FXML
    private Label immatricolazione;
    @FXML
    private TextField prezzoStimatoField;

    private Stage stage;
    private JsonNode rootNode;
    private List<JsonNode> filteredAutoList;
    private Iterator<JsonNode> autoIterator;
    private JsonNode currentAuto;
    private String jsonFilePath = "public/res/data/datiAutoUsate.json";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/configuratoreautoonline/ValutaUsati.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Valuta Usati");
        primaryStage.show();
    }

    @FXML
    public void initialize() {
        stage = new Stage();
        // Carica i dati dal JSON e imposta i campi nel controller
        loadJSONData(jsonFilePath);
        loadNextValidAuto();
    }

    @FXML
    public void onProssimoVeicoloClicked(ActionEvent event) {
        loadNextValidAuto();
    }

    // Quando l'utente clicca sul pulsante "Stima" e viene salvato il prezzo stimato
    @FXML
    public void onStimaBtnClicked(ActionEvent event) {
        if (currentAuto != null) {
            try {
                // Modifica il prezzo dell'auto corrente
                ((ObjectNode) currentAuto).put("prezzo", prezzoStimatoField.getText());

                // Scrivi il JSON modificato nel file
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(jsonFilePath).toFile(), rootNode);

                // Mostra un messaggio di successo
                showAlert("Stima", "Stima del veicolo effettuata con successo!");
            } catch (IOException e) {
                showAlert("JSON Error", "Errore durante la scrittura dei dati nel file JSON: " + e.getMessage());
                e.printStackTrace(); // Stampa lo stack trace per il debug
            }
        } else {
            showAlert("Stima", "Nessun dato di veicolo da stimare.");
        }
    }

    // legge li file json e filtra le auto con prezzo minore di 0 ( quindi da valutare )
    private void loadJSONData(String jsonFilePath) {
        try {
            // Legge il contenuto del file JSON
            String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));

            // Converte il contenuto JSON in un oggetto JsonNode
            ObjectMapper objectMapper = new ObjectMapper();
            rootNode = objectMapper.readTree(jsonContent);

            // Estrai l'array "datiAutoUsate"
            ArrayNode autoArray = (ArrayNode) rootNode.get("datiAutoUsate");

            // Filtra le auto con prezzo diverso da -1
            filteredAutoList = new ArrayList<>();
            for (JsonNode auto : autoArray) {
                if (auto.get("prezzo").asInt() < 0) {
                    filteredAutoList.add(auto);
                }
            }

            autoIterator = filteredAutoList.iterator();
        } catch (IOException e) {
            showAlert("JSON Error", "Errore durante la lettura del file JSON: " + e.getMessage());
            e.printStackTrace(); // Stampa lo stack trace per il debug
        }
    }

    // Carica il prossimo veicolo valido
    private void loadNextValidAuto() {
        if (autoIterator.hasNext()) {
            currentAuto = autoIterator.next();
            updateUI(currentAuto);
        } else {
            showAlert("Fine della lista", "Non ci sono più veicoli nella lista.");
            resetUI();
        }
    }

    // popola i campi con i dati dell'auto corrente
    private void updateUI(JsonNode autoNode) {
        marcaTxt.setText(autoNode.get("marca").asText());
        modelloTxt.setText(autoNode.get("modello").asText());
        kmTxt.setText(autoNode.get("km").asText());
        proprietariTxt.setText(String.valueOf(autoNode.get("proprietari").asInt()));
        immatricolazione.setText(autoNode.get("anno").asText());
        prezzoStimatoField.setText(autoNode.get("prezzo").asText());

        // Imposta l'immagine del veicolo
        String imagePath = autoNode.get("immagine").asText();
        Image image = new Image(Paths.get(imagePath).toUri().toString());
        imageView.setImage(image);
    }

    // svuota tutti i campi
    private void resetUI() {
        marcaTxt.setText("");
        modelloTxt.setText("");
        kmTxt.setText("");
        proprietariTxt.setText("");
        immatricolazione.setText("");
        prezzoStimatoField.setText("");
        imageView.setImage(null);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
    private void changeScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Ottieni lo Stage dalla scena corrente
            Stage currentStage = (Stage) pannelloAncora.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.show();
        } catch (Exception e) {
            showAlert("Error loading scene", "Cannot load scene from file: " + fxmlFile + "\n" + e.getMessage());
            e.printStackTrace(); // Stampa lo stack trace per il debug
        }
    }


    public void handleHomeButtonAction(ActionEvent event) {
        changeScene("/com/example/configuratoreautoonline/Home-view.fxml");
    }
}