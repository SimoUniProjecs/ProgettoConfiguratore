package com.example.configuratoreautoonline;

import Classi.DecisionTree;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.util.List;

public class CarConfiguratorController {
    @FXML
    private ComboBox<String> marcaComboBox;

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
    private final DecisionTree tree = new DecisionTree(root);

    private Stage stage;

    @FXML
    public void initialize() {
        stage = new Stage();
        /**
         *  root = buildCarDecisionTree();
         *         tree = new DecisionTree();
         *         tree.train(root);
         */

        List<String> marche = Arrays.asList("BMW", "AUDI", "ALFA");
        marcaComboBox.setItems(FXCollections.observableArrayList(marche));
    }
    @FXML
    private void onMarcaSelected(ActionEvent event) {
        String selectedMarca = marcaComboBox.getValue();
        if (selectedMarca != null) {
            List<String> modelli = getModelliForMarca(selectedMarca);
            modelloComboBox.setItems(FXCollections.observableArrayList(modelli));
            modelloComboBox.setDisable(false);

            // Seleziona automaticamente il primo modello
            if (!modelli.isEmpty()) {
                modelloComboBox.setValue(modelli.get(0));
                onModelloSelected(null); // Chiamata diretta per aggiornare il seguente ComboBox
            }
        }
    }
    @FXML
    private void onModelloSelected(ActionEvent event) {
        String selectedMarca = marcaComboBox.getValue();
        String selectedModello = modelloComboBox.getValue();
        if (selectedModello != null) {
            List<String> colori = getColoriForModello(selectedMarca, selectedModello);
            coloreComboBox.setItems(FXCollections.observableArrayList(colori));
            coloreComboBox.setDisable(false);

            // Seleziona automaticamente il primo colore disponibile
            if (!colori.isEmpty()) {
                coloreComboBox.setValue(colori.get(0));
                onColoreSelected(null); // Chiamata diretta per aggiornare il seguente ComboBox
            }
        }
    }

    @FXML
    private void onColoreSelected(ActionEvent event) {
        String selectedColore = coloreComboBox.getValue();
        if (selectedColore != null) {
            List<String> ruote = Arrays.asList("RuoteGrandi", "RuoteBase");
            ruoteComboBox.setItems(FXCollections.observableArrayList(ruote));
            ruoteComboBox.setDisable(false);

            // Seleziona automaticamente il primo tipo di ruote disponibile
            if (!ruote.isEmpty()) {
                ruoteComboBox.setValue(ruote.get(0));
                updateImage(); // Aggiorna l'immagine in base alle selezioni complete
            }
        }
    }

    @FXML
    private void onConfiguraButtonClicked() {
        String selectedMarca = marcaComboBox.getValue();
        String selectedModello = modelloComboBox.getValue();
        String selectedColore = coloreComboBox.getValue();
        String selectedRuote = ruoteComboBox.getValue();

        if (selectedMarca != null && selectedModello != null && selectedColore != null && selectedRuote != null) {
            List<String> nodePath = Arrays.asList("img", selectedMarca, selectedModello, selectedColore, selectedRuote);
            String path = tree.predict(nodePath);
            resultLabel.setText("Percorso configurazione: " + path);
        } else {
            resultLabel.setText("Errore: Seleziona tutte le opzioni");
        }
    }

    private void updateImage() {
        if (marcaComboBox.getValue() != null && modelloComboBox.getValue() != null &&
                coloreComboBox.getValue() != null && ruoteComboBox.getValue() != null) {
            List<String> nodePath = Arrays.asList("img", marcaComboBox.getValue(), modelloComboBox.getValue(), coloreComboBox.getValue(), ruoteComboBox.getValue());
            String path = tree.predict(nodePath);
            resultLabel.setText("Percorso configurazione: " + path);
            loadImage(path);
        }
    }

    private List<String> getModelliForMarca(String marca) {
        List<String> modelli = new ArrayList<>();
        switch (marca) {
            case "BMW":
                modelli.add("M2");
                modelli.add("XM");
                break;
            case "AUDI":
                modelli.add("RS3");
                modelli.add("RS4");
                break;
            case "ALFA":
                modelli.add("GIULIA");
                modelli.add("STELVIO");
                break;
        }
        return modelli;
    }

    private void loadImage(String imagePath) {
        try {
            File file = new File(imagePath);
            Image image = new Image(file.toURI().toString());
            carImageView.setImage(image);
        } catch (Exception e) {
            carImageView.setImage(null);
            resultLabel.setText("Errore: Immagine non trovata");
        }
    }

    @FXML
    private void handleHomeButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/configuratoreautoonline/Home-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Errore nel tornare alla Home", "Si è verificato un errore nel tornare alla Home");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }

    private List<String> getColoriForModello(String marca, String modello) {
        List<String> colori = new ArrayList<>();
        switch (marca) {
            case "BMW":
                if (modello.equals("M2")) {
                    colori.add("colore_azzurro");
                    colori.add("colore_grigio");
                    colori.add("colore_rosso");
                } else if (modello.equals("XM")) {
                    colori.add("Base");
                }
                break;
            case "AUDI":
                if (modello.equals("RS3")) {
                    colori.add("colore_nero");
                    colori.add("colore_grigio");
                    colori.add("colore_giallo");
                } else if (modello.equals("RS4")) {
                    colori.add("colore_bianco");
                    colori.add("colore_blu");
                    colori.add("colore_grigio");
                }
                break;
            case "ALFA":
                if (modello.equals("GIULIA")) {
                    colori.add("colore_verde");
                    colori.add("colore_grigio");
                    colori.add("colore_rosso");
                } else if (modello.equals("STELVIO")) {
                    colori.add("colore_rosso");
                    colori.add("colore_blu");
                    colori.add("colore_verde");
                }
                break;
        }
        return colori;
    }
/**
 *
 private Nodo buildCarDecisionTree() {
 Nodo root = new Nodo("img", null);

 Nodo bmw = new Nodo("BMW", root);
 Nodo audi = new Nodo("AUDI", root);
 Nodo alfa = new Nodo("ALFA", root);

 root.addBranch(bmw);
 root.addBranch(audi);
 root.addBranch(alfa);

 Nodo Giulia = new Nodo("GIULIA", alfa);
 Nodo Stelvio = new Nodo("STELVIO", alfa);

 alfa.addBranch(Stelvio, Giulia);

 Nodo VerdeGiulia = new Nodo("Verde", Giulia);
 Nodo GrigioGiulia = new Nodo("Grigio", Giulia);
 Nodo RossoGiulia = new Nodo("Rosso", Giulia);

 Giulia.addBranch(VerdeGiulia, GrigioGiulia, RossoGiulia);

 Nodo RS3 = new Nodo("RS3", audi);
 Nodo RS4 = new Nodo("RS4", audi);

 audi.addBranch(RS3, RS4);

 Nodo RossoStelvio = new Nodo("Rosso", Stelvio);
 Nodo BluStelvio = new Nodo("Blu", Stelvio);
 Nodo VerdeStelvio = new Nodo("Verde", Stelvio);

 Stelvio.addBranch(RossoStelvio, BluStelvio, VerdeStelvio);

 RS3.addBranch(new Nodo("Nero", RS3), new Nodo("Grigio", RS3), new Nodo("Giallo", RS3));
 RS4.addBranch(new Nodo("Bianco", RS4), new Nodo("Blu", RS4), new Nodo("Grigio", RS4));

 Nodo M2 = new Nodo("M2", bmw);
 Nodo XM = new Nodo("XM", bmw);

 bmw.addBranch(M2, XM);

 M2.addBranch(new Nodo("Azzurro", M2), new Nodo("Grigio", M2), new Nodo("Rosso", M2));
 XM.addBranch(new Nodo("Base", XM));

 // Aggiungi nodi "RuoteGrandi" e "RuoteBase" per ogni colore
 for (Nodo colore : new Nodo[]{VerdeGiulia, GrigioGiulia, RossoGiulia, RossoStelvio, BluStelvio, VerdeStelvio}) {
 colore.addBranch(new Nodo("RuoteGrandi", colore), new Nodo("RuoteBase", colore));
 }

 return root;
 }
 */
}

