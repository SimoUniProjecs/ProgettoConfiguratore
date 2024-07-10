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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class ValutaUsatiController extends Application {

    @FXML
    private AnchorPane pannelloAncora;
    @FXML
    private MenuBar menuBar;
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
    private TextField prezzoStimatoField; // Assicurati che questo campo sia collegato alla tua GUI

    private Stage stage;
    private JsonNode autoNode;
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
        primaryStage.setTitle("Configuratore Auto Online - Valuta Usati");
        primaryStage.show();
    }

    @FXML
    public void initialize() {
        stage = new Stage();
        // Carica i dati dal JSON e imposta i campi nel controller
        loadJSONData(jsonFilePath);
    }

    @FXML
    public void handleSwitchHome(ActionEvent event) {
        changeScene("/com/example/configuratoreautoonline/Home-view.fxml");
    }

    @FXML
    public void onInserisciVeicoloClicked(ActionEvent event) {
        changeScene("/com/example/configuratoreautoonline/Vendi-view.fxml");
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
        } catch (IOException e) {
            showAlert("Error loading scene", "Cannot load scene from file: " + fxmlFile + "\n" + e.getMessage());
            e.printStackTrace(); // Stampa lo stack trace per il debug
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }

    private void loadJSONData(String jsonFilePath) {
        try {
            // Legge il contenuto del file JSON
            String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));

            // Converte il contenuto JSON in un oggetto JsonNode
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonContent);

            // Estrai l'array "datiAutoUsate"
            JsonNode datiAutoUsate = rootNode.get("datiAutoUsate");

            // Prendi il primo oggetto auto (presumendo che ce ne sia solo uno)
            if (datiAutoUsate != null && datiAutoUsate.isArray() && datiAutoUsate.size() > 0) {
                autoNode = datiAutoUsate.get(0);

                // Estrapola i dati dall'oggetto JSON
                String marca = autoNode.get("marca").asText();
                String modello = autoNode.get("modello").asText();
                String km = autoNode.get("km").asText();
                String proprietari = String.valueOf(autoNode.get("proprietari").asInt());
                String immatricolazione = autoNode.get("anno").asText();
                String imageUrl = autoNode.get("immagine").asText();

                // Imposta i dati nei campi della GUI
                marcaTxt.setText(marca);
                modelloTxt.setText(modello);
                kmTxt.setText(km);
                proprietariTxt.setText(proprietari);
                this.immatricolazione.setText(immatricolazione);

                // Carica e visualizza l'immagine
                loadImage(imageUrl);
            } else {
                showAlert("JSON Error", "Nessun dato auto trovato nel file JSON.");
            }
        } catch (IOException e) {
            showAlert("JSON Error", "Errore durante il caricamento dei dati dal file JSON: " + e.getMessage());
            e.printStackTrace(); // Stampa lo stack trace per il debug
        }
    }

    private void loadImage(String path) {
        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
            imageView.setImage(image);
        } catch (NullPointerException e) {
            showAlert("Loading Error", "Failed to load image: " + path);
        }
    }

    @FXML
    public void onStimaBtnClicked(ActionEvent event) {
        if (autoNode != null) {
            try {
                // Modifica il prezzo dell'auto
                ((ObjectNode) autoNode).put("prezzo", prezzoStimatoField.getText()); // Imposta il nuovo prezzo dal campo input

                // Leggi il contenuto del file JSON
                String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));

                // Converte il contenuto JSON in un oggetto JsonNode
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(jsonContent);

                // Trova l'array "datiAutoUsate"
                ArrayNode datiAutoUsate = (ArrayNode) rootNode.get("datiAutoUsate");

                // Sostituisci l'autoNode nell'array con la nuova versione aggiornata
                for (int i = 0; i < datiAutoUsate.size(); i++) {
                    JsonNode currentNode = datiAutoUsate.get(i);
                    if (currentNode.get("modello").asText().equals(autoNode.get("modello").asText())) {
                        datiAutoUsate.set(i, autoNode);
                        break;
                    }
                }

                // Scrivi il JSON modificato nel file
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

    // Metodo per gestire l'azione dei MenuItem nel MenuBar
    @FXML
    public void handleMenuItemAction(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String menuItemText = menuItem.getText();

        switch (menuItemText) {
            case "Torna alla Home":
                handleSwitchHome(event);
                break;
            case "Valuta Usati":
                // Non necessario, siamo già sulla scena Valuta Usati
                break;
            case "Inserisci Veicolo":
                onInserisciVeicoloClicked(event);
                break;
            default:
                showAlert("Menu Item Clicked", "Azione non gestita per il MenuItem: " + menuItemText);
        }
    }

    public void onProssimoVeicoloClicked(ActionEvent event) {
        // Deve cercare il prossimo veicolo che ha come prezzo -1 e caricare i suoi dati
        // Nel caso non ci siano auto con prezzo -1 allora non c'è nessuna auto da valutare
    }
}