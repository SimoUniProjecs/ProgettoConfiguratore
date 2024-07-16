package com.example.configuratoreautoonline;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ValutaUsatiController {

    // campi utilizzati per il file fxml
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
    private TextField prezzoStimatoField; // campo per inserire il prezzo valutato dalla segretaria
    @FXML
    private Label emailLabel;

    private Stage stage;
    private JsonNode rootNode;
    private List<JsonNode> filteredAutoList;
    private Iterator<JsonNode> autoIterator; // si può iterare sui nodi che sono le auto da Valutare
    private JsonNode currentAuto; // auto che si sta valutando in questo momento
    private String jsonFilePath = "public/res/data/datiAutoUsate.json";
    private String preventiviFilePath = "public/res/data/preventivi.json"; // si utilizza per aggiornare il preventivo e inserire se è stato utilizzato lo sconto

    @FXML
    public void initialize() {
        stage = new Stage();
        loadJSONData(jsonFilePath);
        loadNextValidAuto();
    }

    // caricare il veicolo da valutare successivo a quello che si sta valutando
    @FXML
    public void onProssimoVeicoloClicked(ActionEvent event) {
        loadNextValidAuto();
    }

    // funzione che salva la valutazione e imposta il valre della valutazione diverso da -1
    // serve anche ad aggioranre il file dei preventivi in modo opportuno
    @FXML
    public void onStimaBtnClicked(ActionEvent event) {
        if (currentAuto != null) {
            try {
                // Modifica il prezzo dell'auto corrente e salva come numero
                int prezzoStima = Integer.parseInt(prezzoStimatoField.getText());
                ((ObjectNode) currentAuto).put("prezzo", prezzoStima);

                // Scrivi il JSON modificato nel file datiAutoUsate.json
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(jsonFilePath).toFile(), rootNode);

                // Aggiorna il preventivo associato
                File preventiviFile = new File(preventiviFilePath);

                if (preventiviFile.exists() && preventiviFile.length() != 0) {
                    JsonNode preventiviRoot = objectMapper.readTree(preventiviFile);
                    ArrayNode preventiviArray = (ArrayNode) preventiviRoot;

                    boolean preventivoTrovato = false;

                    for (JsonNode preventivoNode : preventiviArray) {
                        if (preventivoNode.get("emailCliente").asText().equals(currentAuto.get("email").asText())) {
                            // Modifica il prezzo del preventivo e imposta scontoUsato a true
                            int prezzoPreventivo = preventivoNode.get("prezzo").asInt();
                            int nuovoPrezzo = prezzoPreventivo - prezzoStima;

                            ((ObjectNode) preventivoNode).put("prezzo", nuovoPrezzo);
                            ((ObjectNode) preventivoNode).put("scontoUsato", true);

                            // Salva i dati modificati nel file preventivi.json
                            objectMapper.writerWithDefaultPrettyPrinter().writeValue(preventiviFile, preventiviRoot);

                            showAlert("Stima", "Stima del veicolo effettuata con successo!");
                            preventivoTrovato = true;
                            break;
                        }
                    }

                    if (!preventivoTrovato) {
                        showAlert("Errore", "Non ci sono preventivi associati all'email del cliente.");
                    }

                } else {
                    showAlert("Errore", "Il file dei preventivi non esiste o è vuoto.");
                }

            } catch (IOException e) {
                showAlert("JSON Error", "Errore durante la scrittura dei dati nel file JSON: " + e.getMessage());
                e.printStackTrace();
            } catch (NumberFormatException e) {
                showAlert("Input Error", "Inserisci un prezzo valido.");
            }
        } else {
            showAlert("Stima", "Nessun dato di veicolo da stimare.");
        }
    }

    private void loadJSONData(String jsonFilePath) {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            ObjectMapper objectMapper = new ObjectMapper();
            rootNode = objectMapper.readTree(jsonContent);

            ArrayNode autoArray = (ArrayNode) rootNode.get("datiAutoUsate");
            filteredAutoList = new ArrayList<>();
            for (JsonNode auto : autoArray) {
                if (auto.get("prezzo").asInt() < 0) {
                    filteredAutoList.add(auto);
                }
            }

            autoIterator = filteredAutoList.iterator();
        } catch (IOException e) {
            showAlert("JSON Error", "Errore durante la lettura del file JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // carica la prossima auto
    private void loadNextValidAuto() {
        if (autoIterator.hasNext()) {
            currentAuto = autoIterator.next();
            updateUI(currentAuto);
        } else {
            showAlert("Fine della lista", "Non ci sono più veicoli nella lista.");
            resetUI();
        }
    }

    // aggiorna i campi della pagina
    private void updateUI(JsonNode autoNode) {
        marcaTxt.setText(autoNode.get("marca").asText());
        modelloTxt.setText(autoNode.get("modello").asText());
        kmTxt.setText(autoNode.get("km").asText());
        proprietariTxt.setText(String.valueOf(autoNode.get("proprietari").asInt()));
        immatricolazione.setText(autoNode.get("anno").asText());
        prezzoStimatoField.setText(autoNode.get("prezzo").asText());
        emailLabel.setText("Email proprietario: " + autoNode.get("email").asText());

        String imagePath = autoNode.get("immagine").asText();
        System.out.println("Percorso immagine copiata: " + imagePath + "\n");
        Image image = new Image(Paths.get(imagePath).toUri().toString());
        imageView.setImage(image);
    }

    // resetta i campi della pagina
    private void resetUI() {
        marcaTxt.setText("");
        modelloTxt.setText("");
        kmTxt.setText("");
        proprietariTxt.setText("");
        immatricolazione.setText("");
        prezzoStimatoField.setText("");
        emailLabel.setText("");
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
            Stage currentStage = (Stage) pannelloAncora.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.show();
        } catch (Exception e) {
            showAlert("Error loading scene", "Cannot load scene from file: " + fxmlFile + "\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void handleHomeButtonAction(ActionEvent event) {
        changeScene("/com/example/configuratoreautoonline/Home-view.fxml");
    }
}
