package com.example.configuratoreautoonline;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CarConfiguratorController {
    @FXML
    private ComboBox<String> motorizzazioneComboBox;
    @FXML
    private ComboBox<String> modelloComboBox;
    @FXML
    private ComboBox<String> coloreComboBox;
    @FXML
    private CheckBox vetriCheck;
    @FXML
    private CheckBox cerchiCheck;
    @FXML
    private CheckBox pinzeCheck;
    @FXML
    private CheckBox cerchiScuriCheck;
    @FXML
    private CheckBox internoCheck;
    @FXML
    private CheckBox impiantoAudioCheck;
    @FXML
    private CheckBox abbonamentoCheck;
    @FXML
    private ImageView carImageView;
    @FXML
    private Label prezzoLbl;
    @FXML
    private Button interniBtn;
    private Stage stage;
    private String selectedMarca;
    private JsonNode datiModelliAuto;

    // Inizializza i dati per la marca dell'auto selezionata
    public void initData(String marca) {
        this.selectedMarca = marca;
        loadJsonData();
        initializeModelComboBox();
    }

    @FXML
    public void initialize() {
        stage = new Stage();

        // Listener per aggiornare l'immagine quando cambia il modello
        modelloComboBox.setOnAction(event -> {
            onModelloSelected(event);
            updateImage();
        });

        // Listener per aggiornare l'immagine quando cambia il colore
        coloreComboBox.setOnAction(event -> updateImage());

        // Listener per aggiornare l'immagine quando cambia la motorizzazione
        motorizzazioneComboBox.setOnAction(event -> {
            onMotoSelected(event);
            updateImage();
        });

        // Listener per aggiornare l'immagine quando si selezionano/deselezionano i CheckBox
        vetriCheck.setOnAction(event -> updateImage());
        cerchiCheck.setOnAction(event -> updateImage());
        pinzeCheck.setOnAction(event -> updateImage());
        cerchiScuriCheck.setOnAction(event -> updateImage());
        internoCheck.setOnAction(event -> onInternoCheckChanged(event));
        impiantoAudioCheck.setOnAction(event -> updateImage());
        abbonamentoCheck.setOnAction(event -> updateImage());
    }
    @FXML
    private void onInternoCheckChanged(ActionEvent event) {
        if (interniBtn.getText().equals("Visualizza Esterni")) {
            // Se stiamo visualizzando gli interni, aggiorniamo l'immagine degli interni
            loadImage(generaPathInterni(selectedMarca, modelloComboBox.getValue()));
        } else {
            updateImage();
        }
    }
    @FXML
    private void onModelloSelected(ActionEvent event) {
        String selectedModello = modelloComboBox.getValue();
        if (selectedModello != null) {
            List<String> colori = getColoriPerModello(selectedMarca, selectedModello);
            coloreComboBox.setItems(FXCollections.observableArrayList(colori));
            coloreComboBox.setDisable(false);
            coloreComboBox.getSelectionModel().clearSelection();

            updateMotorizzazioneComboBox(selectedModello);
        }
        updateImage();
    }

    private String generaPathInterni(String marca, String modello){
        String percorso = "";
        JsonNode marcaNode = datiModelliAuto.get(marca.toLowerCase());
        if (marcaNode != null) {
            Iterator<JsonNode> modelliIterator = marcaNode.elements();
            while (modelliIterator.hasNext()) {
                JsonNode modelloNode = modelliIterator.next().get("modelli").get(0);
                JsonNode optionalNode = modelloNode.get(modello);
                if (optionalNode != null && optionalNode.has("percorsoImg")) {
                    percorso = optionalNode.get("percorsoImg").asText();
                }
            }
        }

        if(internoCheck.isSelected() && getOptionalsForModello(selectedMarca, modelloComboBox.getValue()).contains("interni pelle")){
            percorso += "internopelle.png";
        } else {
            percorso += "internobase.png";
        }
        return percorso.toLowerCase();
    }
    @FXML
    private void onInterniClicked(ActionEvent event){
        if(interniBtn.getText().equals("Visualizza Interni")){
            try {
                loadImage(generaPathInterni(selectedMarca, modelloComboBox.getValue()));
                interniBtn.setText("Visualizza Esterni");
            }catch (Exception e) {
                showAlert("Errore", "Impossibile caricare l'immagine degli interni.");
            }
        } else {
            interniBtn.setText("Visualizza Interni");
            updateImage();
        }
    }

    // Carica datiModelliAuto.json in datiModelliAuto
    private void loadJsonData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File("public/res/data/datiModelliAuto.json");
            JsonNode root = objectMapper.readTree(file);
            datiModelliAuto = root.get("datiModelliAuto").get(0);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Errore di caricamento", "Impossibile caricare i dati del file JSON.");
        }
    }

    // Inizializza il ComboBox per i modelli dell'auto [ con visibilità o meno ]
    private void initializeModelComboBox() {
        if (selectedMarca != null) {
            List<String> modelli = getModelliForMarca(selectedMarca);
            modelloComboBox.setItems(FXCollections.observableArrayList(modelli));
            modelloComboBox.setDisable(false);
            modelloComboBox.getSelectionModel().clearSelection();
            coloreComboBox.setDisable(true);
            coloreComboBox.getItems().clear();
            interniBtn.setDisable(true);
            motorizzazioneComboBox.setDisable(true);
            motorizzazioneComboBox.getItems().clear();
            carImageView.setImage(null);

            // Rimuovi il listener vecchio se presente
            modelloComboBox.setOnAction(null);

            // Aggiungi nuovo listener per il cambio dinamico dell'immagine
            modelloComboBox.setOnAction(event -> onModelloSelected(event));

            // Disabilita gli optional
            disableOptionalCheckboxes(true);
        }
    }
    @FXML
    private void disableOptionalCheckboxes(boolean disable) {
        vetriCheck.setDisable(disable);
        cerchiCheck.setDisable(disable);
        cerchiScuriCheck.setDisable(disable);
        pinzeCheck.setDisable(disable);
        internoCheck.setDisable(disable);
        impiantoAudioCheck.setDisable(disable);
        abbonamentoCheck.setDisable(disable);
    }

    // Una volta che è stato scelto il modello, si possono selezionare gli optional partendo dal colore

    // Richiama getMotorizzazioniForModello per ottenere le motorizzazioni del modello selezionato
    private void updateMotorizzazioneComboBox(String modello) {
        List<String> motorizzazioni = getMotorizzazioniForModello(selectedMarca, modello);
        motorizzazioneComboBox.setItems(FXCollections.observableArrayList(motorizzazioni));
        motorizzazioneComboBox.setDisable(false);

        // Rimuovi il listener vecchio se presente
        motorizzazioneComboBox.setOnAction(null);

        // Aggiungi nuovo listener per il cambio dinamico dell'immagine
        motorizzazioneComboBox.setOnAction(event -> onMotoSelected(event));
    }

    // Restituisci gli optional per il modello selezionato
    private List<String> getOptionalsForModello(String marca, String modello) {
        List<String> optionals = new ArrayList<>();
        JsonNode marcaNode = datiModelliAuto.get(marca.toLowerCase());
        if (marcaNode != null) {
            Iterator<JsonNode> modelliIterator = marcaNode.elements();
            while (modelliIterator.hasNext()) {
                JsonNode modelloNode = modelliIterator.next().get("modelli").get(0);
                JsonNode optionalNode = modelloNode.get(modello);
                if (optionalNode != null && optionalNode.has("optionals")) {
                    optionalNode.get("optionals").forEach(optional -> optionals.add(optional.asText()));
                }
            }
        }
        return optionals;
    }

    // popola la choicebox con i colori disponibili per il modello selezionato
    private List<String> getColoriPerModello(String marca, String modello) {
        List<String> colori = new ArrayList<>();
        JsonNode marcaNode = datiModelliAuto.get(marca.toLowerCase());
        if (marcaNode != null) {
            Iterator<JsonNode> modelliIterator = marcaNode.elements();
            while (modelliIterator.hasNext()) {
                JsonNode modelloNode = modelliIterator.next().get("modelli").get(0);
                JsonNode optionalNode = modelloNode.get(modello);
                if (optionalNode != null && optionalNode.has("colori")) {
                    optionalNode.get("colori").forEach(optional -> colori.add(optional.asText()));
                }
            }
        }
        return colori;
    }

    // Ritorna il percorso alla cartella del modello:
    private String getPercorsoIMGPerModello(String marca, String modello) {
        String percorso = "";
        JsonNode marcaNode = datiModelliAuto.get(marca.toLowerCase());
        if (marcaNode != null) {
            Iterator<JsonNode> modelliIterator = marcaNode.elements();
            while (modelliIterator.hasNext()) {
                JsonNode modelloNode = modelliIterator.next().get("modelli").get(0);
                JsonNode optionalNode = modelloNode.get(modello);
                if (optionalNode != null && optionalNode.has("percorsoImg")) {
                    percorso = optionalNode.get("percorsoImg").asText();
                }
            }
        }
        return percorso;
    }

    // popola la choicebox con le motorizzazioni disponibili per il modello selezionato
    private List<String> getMotorizzazioniForModello(String marca, String modello) {
        List<String> motorizzazioni = new ArrayList<>();
        JsonNode marcaNode = datiModelliAuto.get(marca.toLowerCase());
        if (marcaNode != null) {
            Iterator<JsonNode> modelliIterator = marcaNode.elements();
            while (modelliIterator.hasNext()) {
                JsonNode modelloNode = modelliIterator.next().get("modelli").get(0);
                JsonNode modelloSpecificoNode = modelloNode.get(modello);
                if (modelloSpecificoNode != null && modelloSpecificoNode.has("motorizzazioni")) {
                    modelloSpecificoNode.get("motorizzazioni").forEach(motorizzazione -> {
                        if (motorizzazione.has("potenza") && motorizzazione.has("alimentazione")) {
                            String alimentazione = motorizzazione.get("alimentazione").asText();
                            String potenza = motorizzazione.get("potenza").asText();
                            motorizzazioni.add(alimentazione + " - " + potenza);
                        }
                    });
                }
            }
        }
        return motorizzazioni;
    }

    //scelta motorizzazione
    public void onMotoSelected(ActionEvent event) {
        String selectedModello = modelloComboBox.getValue();
        if (selectedModello != null) {
            List<String> motori = getMotorizzazioniForModello(selectedMarca, selectedModello);
            motorizzazioneComboBox.setItems(FXCollections.observableArrayList(motori));
            motorizzazioneComboBox.setDisable(false);

            List<String> optionals = getOptionalsForModello(selectedMarca, selectedModello);

            if (optionals.contains("vetri oscurati")) {
                vetriCheck.setDisable(false);
            } else {
                vetriCheck.setDisable(true);
            }

            if (optionals.contains("cerchi maggiorati")) {
                cerchiCheck.setDisable(false);
            } else {
                cerchiCheck.setDisable(true);
            }

            if (optionals.contains("cerchi neri")) {
                cerchiScuriCheck.setDisable(false);
            } else {
                cerchiScuriCheck.setDisable(true);
            }

            if (optionals.contains("freni rossi")) {
                pinzeCheck.setDisable(false);
            } else {
                pinzeCheck.setDisable(true);
            }

            if (optionals.contains("interni pelle")) {
                internoCheck.setDisable(false);
            } else {
                internoCheck.setDisable(true);
            }

            if (optionals.contains("impianto audio HarmanCardon")) {
                impiantoAudioCheck.setDisable(false);
            } else {
                impiantoAudioCheck.setDisable(true);
            }

            if (motorizzazioneComboBox.getValue().contains("Elettrica") || motorizzazioneComboBox.getValue().contains("Ibrida Plug-in")) {
                abbonamentoCheck.setDisable(false);
            } else {
                abbonamentoCheck.setDisable(true);
            }

            // Aggiorna il prezzo base con quel motore
            prezzoLbl.setText(getPrezzo(selectedMarca, modelloComboBox.getValue(), motorizzazioneComboBox.getValue()) + " €");

            // do la possibilità di vedere gli interni
            interniBtn.setDisable(false);
        }
        updateImage();
    }
    @FXML
    private void onConfiguraButtonClicked() {
        String selectedModello = modelloComboBox.getValue();
        String selectedColore = coloreComboBox.getValue();

        if (selectedMarca != null && selectedModello != null && selectedColore != null ) {
            String path = getPercorsoIMGPerModello(selectedMarca, selectedModello) + getSecondaParteIMG(getOptionalsForModello(selectedMarca, selectedModello), selectedColore);
            loadImage(path);
        } else {
            showAlert("Errore", "Seleziona marca, modello e colore per configurare l'auto.");
        }
    }

    // Ritorna il prezzo dell'auto base senza optional
    private String getPrezzo(String selectedMarca, String modello, String motorizzazione) {
        // Dividiamo l'input di motorizzazione in alimentazione e potenza
        String[] parts = motorizzazione.split(" - ");
        if (parts.length != 2) {
            return null; // Restituisce null se l'input non è nel formato corretto
        }
        String alimentazione = parts[0];
        String potenza = parts[1];

        JsonNode marcaNode = datiModelliAuto.get(selectedMarca.toLowerCase());
        if (marcaNode != null) {
            Iterator<JsonNode> modelliIterator = marcaNode.elements();
            while (modelliIterator.hasNext()) {
                JsonNode modelliNode = modelliIterator.next().get("modelli");
                for (JsonNode modelloNode : modelliNode) {
                    JsonNode specificModelNode = modelloNode.get(modello);
                    if (specificModelNode != null) {
                        JsonNode motorizzazioniNode = specificModelNode.get("motorizzazioni");
                        for (JsonNode motorizzazioneNode : motorizzazioniNode) {
                            String potenzaModello = motorizzazioneNode.get("potenza").asText();
                            String alimentazioneModello = motorizzazioneNode.get("alimentazione").asText();
                            // Confrontiamo sia l'alimentazione che la potenza
                            if (potenzaModello.equals(potenza) && alimentazioneModello.equalsIgnoreCase(alimentazione)) {
                                return motorizzazioneNode.get("prezzo").asText();
                            }
                        }
                    }
                }
            }
        }
        return ""; // Se non trova corrispondenze
    }
    // Costruisce il percorso all'interno della directory del modello
    private String getSecondaParteIMG(List<String> optionalsForModello, String selectedColore) {
        StringBuilder risultato = new StringBuilder();

        // Aggiungo il colore
        risultato.append(selectedColore);

        // Per ogni possibile optional controllo se è stato selezionato
        // e se è presente tra gli optional possibili del modello
        // Altrimenti potrebbe essere un optional non disponibile per quel modello perchè disattivato
        // ma disattivato solo per quel modello

        if(cerchiScuriCheck.isSelected() && optionalsForModello.contains("cerchi neri")){
            risultato.append("cerchineri");
        }

        if(cerchiCheck.isSelected() && optionalsForModello.contains("cerchi maggiorati")){
            risultato.append("cerchimaggiorati");
        }

        if(vetriCheck.isSelected() && optionalsForModello.contains("vetri oscurati")){
            risultato.append("vetrioscurati");
        }

        if(pinzeCheck.isSelected() && optionalsForModello.contains("freni rossi")){
            risultato.append("frenirossi");
        }

        return risultato.append(".png").toString().toLowerCase();
    }
    // Aggiorna l'immagine caricata in base al modello e al colore selezionati e optionals
    public void updateImage() {
        if (modelloComboBox.getValue() != null &&
                coloreComboBox.getValue() != null &&
                motorizzazioneComboBox.getValue() != null) {
            String path = getPercorsoIMGPerModello(selectedMarca, modelloComboBox.getValue()) +
                    getSecondaParteIMG(getOptionalsForModello(selectedMarca, modelloComboBox.getValue()), coloreComboBox.getValue());
            loadImage(path);
        }
    }
    // Restituisce i modelli per la marca selezionata in una LISTA di stringhe
    private List<String> getModelliForMarca(String marca) {
        List<String> modelli = new ArrayList<>();
        JsonNode marcaNode = datiModelliAuto.get(marca.toLowerCase());
        if (marcaNode != null) {
            Iterator<JsonNode> modelliIterator = marcaNode.elements();
            while (modelliIterator.hasNext()) {
                JsonNode modelloNode = modelliIterator.next().get("modelli").get(0);
                modelloNode.fieldNames().forEachRemaining(modelli::add);
            }
        }
        return modelli;
    }
    // DAto il percorso dell'immagine, la carica
    private void loadImage(String path) {
        try {
            File file = new File(path);
            Image image = new Image(file.toURI().toString());
            carImageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // per tornare alla home
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
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
