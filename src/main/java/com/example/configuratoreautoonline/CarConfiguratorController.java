package com.example.configuratoreautoonline;

import Classi.DecisionTree;
import Classi.Nodo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CarConfiguratorController {
    @FXML
    private ComboBox<String> modelloComboBox;

    @FXML
    private ComboBox<String> coloreComboBox;

    @FXML
    private ComboBox<String> ruoteComboBox;

    @FXML
    private Label resultLabel;

    @FXML
    private ImageView carImageView;

    private Nodo root = buildCarDecisionTree();

    private DecisionTree tree = new DecisionTree(root, "", "");

    private Stage stage;

    private String selectedMarca;

    private JsonNode datiAutoUsate;

    public void initData(String marca) {
        this.selectedMarca = marca;
        loadJsonData();
        initializeModelComboBox();
    }

    @FXML
    public void initialize() {
        stage = new Stage();
    }

    private void loadJsonData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File("public/res/data/datiModelliAuto.json"); // Sostituisci con il percorso corretto
            JsonNode root = objectMapper.readTree(file);
            datiAutoUsate = root.get("datiAutoUsate").get(0);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Errore di caricamento", "Impossibile caricare i dati del file JSON.");
        }
    }

    private void initializeModelComboBox() {
        if (selectedMarca != null) {
            List<String> modelli = getModelliForMarca(selectedMarca);
            modelloComboBox.setItems(FXCollections.observableArrayList(modelli));
            modelloComboBox.setDisable(false);
            modelloComboBox.getSelectionModel().clearSelection();

            coloreComboBox.setDisable(true);
            coloreComboBox.getItems().clear();

            ruoteComboBox.setDisable(true);
            ruoteComboBox.getItems().clear();

            carImageView.setImage(null);
            resultLabel.setText("");
        }
    }

    @FXML
    private void onModelloSelected(ActionEvent event) {
        String selectedModello = modelloComboBox.getValue();
        if (selectedModello != null) {
            List<String> optionalList = getOptionalsForModello(selectedMarca, selectedModello);
            coloreComboBox.setItems(FXCollections.observableArrayList(optionalList));
            coloreComboBox.setDisable(false);
            coloreComboBox.getSelectionModel().clearSelection();

            ruoteComboBox.setDisable(true);
            ruoteComboBox.getItems().clear();

            updateImage();
        }
    }

    private List<String> getOptionalsForModello(String marca, String modello) {
        List<String> optionals = new ArrayList<>();
        JsonNode marcaNode = datiAutoUsate.get(marca.toLowerCase());
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

    @FXML
    private void onColoreSelected(ActionEvent event) {
        String selectedColore = coloreComboBox.getValue();
        if (selectedColore != null) {
            List<String> ruote = Arrays.asList("_cerchi_grandi", "_cerchi_base");
            ruoteComboBox.setItems(FXCollections.observableArrayList(ruote));
            ruoteComboBox.setDisable(false);

            updateImage();
        }
    }

    @FXML
    private void onConfiguraButtonClicked() {
        String selectedModello = modelloComboBox.getValue();
        String selectedColore = coloreComboBox.getValue();
        String selectedRuote = ruoteComboBox.getValue();

        if (selectedMarca != null && selectedModello != null && selectedColore != null && selectedRuote != null) {
            List<String> nodePath = Arrays.asList("img", selectedMarca, selectedModello, selectedColore, selectedRuote);
            String path = tree.predict(nodePath);

            resultLabel.setText("Percorso configurazione: " + path);
            loadImage(path);
        } else {
            resultLabel.setText("Errore: Seleziona tutte le opzioni");
        }
    }

    private void updateImage() {
        if (modelloComboBox.getValue() != null &&
                coloreComboBox.getValue() != null && ruoteComboBox.getValue() != null) {
            List<String> nodePath = Arrays.asList("img", selectedMarca, modelloComboBox.getValue(), coloreComboBox.getValue(), ruoteComboBox.getValue());
            String path = tree.predict(nodePath);
            resultLabel.setText("Percorso configurazione: " + path);
            loadImage(path);
        }
    }

    private List<String> getModelliForMarca(String marca) {
        List<String> modelli = new ArrayList<>();
        JsonNode marcaNode = datiAutoUsate.get(marca.toLowerCase());
        if (marcaNode != null) {
            Iterator<JsonNode> modelliIterator = marcaNode.elements();
            while (modelliIterator.hasNext()) {
                JsonNode modelloNode = modelliIterator.next().get("modelli").get(0);
                modelloNode.fieldNames().forEachRemaining(modelli::add);
            }
        }
        return modelli;
    }

    private String createPath(String... lista) {
        String path = "src/main/resources/img/";
        int i = 0;
        for (String s : lista) {
            if (i < 2) {
                path += s + "/";
            } else {
                path += s;
            }
            i++;
        }
        path += ".png";
        return path;
    }

    private void loadImage(String imagePath) {
        String path = createPath(selectedMarca, modelloComboBox.getValue(), coloreComboBox.getValue(), ruoteComboBox.getValue());
        try {
            System.out.println(path);
            File file = new File(path);
            Image image = new Image(file.toURI().toString());
            carImageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Nodo buildCarDecisionTree() {
        // Implementazione per costruire l'albero decisionale
        return new Nodo("root", null); // Sostituisci con la logica effettiva
    }

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
