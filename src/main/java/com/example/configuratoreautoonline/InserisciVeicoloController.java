package com.example.configuratoreautoonline;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
import java.net.URI;
import java.net.URISyntaxException;
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
    private String pathRadice;

    // Carica datiModelliAuto.json in datiModelliAuto
    private void loadJsonData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File("public/res/data/datiModelliAuto.json");
            JsonNode root = objectMapper.readTree(file);
            datiModelliAuto = root.get("datiModelliAuto"); // Prendiamo l'array principale
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

        // Se dei file sono stati selezionati, aggiungi le immagini visivamente all'HBox
        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            for (File selectedFile : selectedFiles) {
                // Crea l'immagine e l'ImageView
                Image image = new Image(selectedFile.toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(200); // Imposta la larghezza desiderata
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);

                // Aggiungi l'ImageView all'HBox per la visualizzazione
                imageContainer.getChildren().add(imageView);
            }
        }
    }
    private String calcolaPercorso() {
        return "src/main/resources/img/" + marcaTxt.getText().toUpperCase() + "/" + modelloTxt.getText().toUpperCase() + "/";
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
            List<String> motorizzazioni = List.of(motorizzazioniTxt.getText().split(";")); // Assume motorizzazioni are separated by semicolons

            if (marcaEsistente(marca.toUpperCase())) {
                if (modelloEsistente(modello, marca)) {
                    showAlert("Errore: duplicato trovato", "Il modello inserito è già presente nel database.");
                    return;
                }
                pathRadice = "src/main/resources/img/" + marca.toUpperCase() + "/";
            } else {
                // Crea la nuova cartella per la marca
                String percorsoCartellaMarca = "src/main/resources/img/" + marca.toUpperCase();
                File cartellaMarca = new File(percorsoCartellaMarca);

                if (!cartellaMarca.exists()) {
                    boolean creata = cartellaMarca.mkdirs(); // mkdirs() crea tutte le directory necessarie
                    if (!creata) {
                        showAlert("Errore", "Impossibile creare la cartella per la marca.");
                        return;
                    }
                }
                pathRadice = percorsoCartellaMarca + "/";
            }

            // Crea la nuova cartella per il modello
            String percorsoModello = pathRadice + modello.toUpperCase();
            File cartellaModello = new File(percorsoModello);

            if (!cartellaModello.exists()) {
                boolean creata = cartellaModello.mkdirs(); // mkdirs() crea tutte le directory necessarie
                if (!creata) {
                    showAlert("Errore", "Impossibile creare la cartella per il modello.");
                    return;
                }
            }

            // Copia le immagini selezionate nella cartella del modello
            try {
                for (Node node : imageContainer.getChildren()) {
                    if (node instanceof ImageView) {
                        ImageView imageView = (ImageView) node;
                        Image image = imageView.getImage();
                        String imageName = extractFileName(image.getUrl()); // Ottieni il nome dell'immagine
                        String imagePath = percorsoModello + "/" + imageName;

                        // Esegui la copia effettiva del file
                        File selectedFile = new File(new URI(image.getUrl()));
                        File targetFile = new File(imagePath);
                        Files.copy(selectedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                }

                // Aggiungi l'auto al JSON
                aggiungiAutoAlJson(marca, modello, colori, optionals, motorizzazioni);

                // Mostra un messaggio di successo
                showAlert("Successo", "Auto aggiunta correttamente.");

                // Torna alla home (scommenta se necessario)
                changeScene("/com/example/configuratoreautoonline/Home-view.fxml");

            } catch (IOException | URISyntaxException e) {
                showAlert("Errore", "Impossibile copiare le immagini nella cartella del modello.");
                e.printStackTrace();
            }
        }
    }

    private String extractFileName(String url) {
        int lastIndex = url.lastIndexOf('/');
        return url.substring(lastIndex + 1);
    }
    // Metodo per controllare se il modello è presente nel JSON
    public boolean modelloEsistente(String modello, String marca) {
        if (datiModelliAuto == null || datiModelliAuto.isEmpty()) {
            showAlert("Errore", "Impossibile caricare i dati del file JSON.");
            return false;
        }

        modello = modello.toLowerCase(); // Converte in lowercase per il confronto
        marca = marca.toLowerCase(); // Converte in lowercase per il confronto

        // Cerca all'interno della marca specificata
        for (JsonNode concessionarioNode : datiModelliAuto) {
            Iterator<String> concessionarioKeys = concessionarioNode.fieldNames();
            while (concessionarioKeys.hasNext()) {
                String concessionarioKey = concessionarioKeys.next();
                if (concessionarioKey.equalsIgnoreCase(marca)) {
                    JsonNode modelliNode = concessionarioNode.get(concessionarioKey).get("modelli");
                    for (JsonNode modelloNode : modelliNode) {
                        Iterator<String> modelliKeys = modelloNode.fieldNames();
                        while (modelliKeys.hasNext()) {
                            String modelloKey = modelliKeys.next();
                            if (modelloKey.equalsIgnoreCase(modello)) {
                                return true; // Trovato corrispondenza
                            }
                        }
                    }
                }
            }
        }

        return false; // Modello non trovato nella marca specificata
    }

    private void aggiungiAutoAlJson(String marca, String modello, List<String> colori, List<String> optionals, List<String> motorizzazioni) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File("public/res/data/datiModelliAuto.json");
            JsonNode root = objectMapper.readTree(file);
            ArrayNode datiModelliAutoNode = (ArrayNode) root.get("datiModelliAuto");

            boolean marcaTrovata = false;
            JsonNode marcaNode = null;

            // Controlla se la marca esiste già nel JSON
            for (JsonNode node : datiModelliAutoNode) {
                marcaNode = node.get(marca.toLowerCase());
                if (marcaNode != null && !marcaNode.isNull()) {
                    marcaTrovata = true;
                    break;
                }
            }

            // Crea il nuovo modello da aggiungere
            ObjectNode nuovoModello = objectMapper.createObjectNode();
            nuovoModello.set(modello, creaModelloNode(objectMapper, colori, optionals, motorizzazioni));

            if (marcaTrovata && marcaNode != null) {
                // Aggiungi il modello alla marca esistente
                ((ArrayNode) marcaNode.get("modelli")).add(nuovoModello);
            } else {
                // Crea una nuova marca
                ObjectNode nuovaMarca = objectMapper.createObjectNode();
                ArrayNode modelliArray = objectMapper.createArrayNode();
                modelliArray.add(nuovoModello);
                nuovaMarca.set("modelli", modelliArray);
                // Aggiungi la nuova marca alla radice del JSON
                ObjectNode nuovaMarcaContainer = objectMapper.createObjectNode();
                nuovaMarcaContainer.set(marca.toLowerCase(), nuovaMarca);
                datiModelliAutoNode.add(nuovaMarcaContainer);
            }

            // Scrivi i cambiamenti nel file JSON
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, root);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Errore", "Impossibile scrivere i dati nel file JSON.");
        }
    }

    private ObjectNode creaModelloNode(ObjectMapper objectMapper, List<String> colori, List<String> optionals, List<String> motorizzazioni) {
        ObjectNode modelloNode = objectMapper.createObjectNode();
        ArrayNode coloriArray = objectMapper.createArrayNode();
        colori.forEach(coloriArray::add);
        modelloNode.set("colori", coloriArray);

        ArrayNode optionalsArray = objectMapper.createArrayNode();
        optionals.forEach(optionalsArray::add);
        modelloNode.set("optionals", optionalsArray);

        ArrayNode motorizzazioniArray = objectMapper.createArrayNode();
        motorizzazioni.forEach(motorizzazioniArray::add);
        modelloNode.set("motorizzazioni", motorizzazioniArray);

        // Nota: Assicurati di impostare altri campi necessari come percorsoImg, ecc.

        return modelloNode;
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
        for (JsonNode concessionarioNode : datiModelliAuto) {
            Iterator<String> keys = concessionarioNode.fieldNames();
            while (keys.hasNext()) {
                String key = keys.next();
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
