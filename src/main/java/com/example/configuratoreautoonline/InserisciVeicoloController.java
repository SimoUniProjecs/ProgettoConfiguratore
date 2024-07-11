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
            List<String> motorizzazioni = List.of(motorizzazioniTxt.getText().split(";")); // Assume motorizzazioni are separated by semicolons

            String pathRadice;

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
                    System.out.println("Cartella per la marca creata con successo.");
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
                System.out.println("Cartella per il modello creata con successo.");
            }

            // Path completo per il file
            pathRadice = percorsoModello + "/";
            System.out.println("Path radice: " + pathRadice);

            // Aggiungo al file JSON
            aggiungiAutoAlJson(marca, modello, colori, optionals, motorizzazioni);

            // Mostra un messaggio di successo
            showAlert("Successo", "Auto aggiunta correttamente.");

            // Torna alla home (scommenta se necessario)
            // changeScene("/com/example/configuratoreautoonline/Home-view.fxml");
        }
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


    private ObjectNode creaModelloNode(ObjectMapper objectMapper, List<String> colori, List<String> optionals, List<String> motorizzazioni) {
        ObjectNode modelloNode = objectMapper.createObjectNode();

        ArrayNode coloriArray = objectMapper.createArrayNode();
        for (String colore : colori) {
            coloriArray.add(colore.trim());
        }

        ArrayNode optionalsArray = objectMapper.createArrayNode();
        for (String optional : optionals) {
            optionalsArray.add(optional.trim());
        }

        ArrayNode motorizzazioniArray = objectMapper.createArrayNode();
        for (String motorizzazione : motorizzazioni) {
            String[] parts = motorizzazione.split(",");
            if (parts.length == 5) {
                ObjectNode motorizzazioneNode = objectMapper.createObjectNode();
                motorizzazioneNode.put("cilindrata", parts[0].trim());
                motorizzazioneNode.put("potenza", parts[1].trim());
                motorizzazioneNode.put("coppia", parts[2].trim());
                motorizzazioneNode.put("alimentazione", parts[3].trim());
                motorizzazioneNode.put("prezzo", parts[4].trim());
                motorizzazioniArray.add(motorizzazioneNode);
            } else {
                System.out.println("Motorizzazione data format is incorrect: " + motorizzazione);
            }
        }

        modelloNode.set("colori", coloriArray);
        modelloNode.set("optionals", optionalsArray);
        modelloNode.set("motorizzazioni", motorizzazioniArray);
        modelloNode.put("percorsoImg", "src/main/resources/img/" + marcaTxt.getText() + "/" + modelloTxt.getText() + "/");

        return modelloNode;
    }




    private void aggiungiAutoAlJson(String marca, String modello, List<String> colori, List<String> optionals, List<String> motorizzazioni) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File("public/res/data/datiModelliAuto.json");
            JsonNode root = objectMapper.readTree(file);
            JsonNode datiModelliAutoNode = root.get("datiModelliAuto");

            boolean marcaTrovata = false;
            JsonNode marcaNode = null;

            // Cerca se la marca esiste già
            for (JsonNode concessionarioNode : datiModelliAutoNode) {
                Iterator<String> keys = concessionarioNode.fieldNames();
                while (keys.hasNext()) {
                    String key = keys.next();
                    if (key.equalsIgnoreCase(marca)) {
                        marcaNode = concessionarioNode.get(key);
                        marcaTrovata = true;
                        break;
                    }
                }
                if (marcaTrovata) {
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
                ((ObjectNode) datiModelliAutoNode.get(0)).set(marca, nuovaMarca);
            }

            // Scrivi i cambiamenti nel file JSON
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, root);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Errore", "Impossibile scrivere i dati nel file JSON.");
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
