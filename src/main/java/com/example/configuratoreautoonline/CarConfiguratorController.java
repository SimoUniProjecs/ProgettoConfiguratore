package com.example.configuratoreautoonline;

import Classi.DecisionTree;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

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

    private DecisionTree tree;
    //private Nodo root;

    @FXML
    private void onMarcaSelected(ActionEvent event) {
    }

    @FXML
    private void onModelloSelected(ActionEvent event)   {

    }

    @FXML
    private void onColoreSelected(ActionEvent event)    {

    }

    @FXML
    private void onConfiguraButtonClicked(ActionEvent event)    {

    }

    @FXML
    public void initialize() {
        /**
         *  root = buildCarDecisionTree();
         *         tree = new DecisionTree();
         *         tree.train(root);
         */

        List<String> marche = Arrays.asList("BMW", "AUDI", "ALFA");
        marcaComboBox.setItems(FXCollections.observableArrayList(marche));
    }

    @FXML
    private void onMarcaSelected() {
        String selectedMarca = marcaComboBox.getValue();
        if (selectedMarca != null) {
            List<String> modelli = getModelliForMarca(selectedMarca);
            modelloComboBox.setItems(FXCollections.observableArrayList(modelli));
            modelloComboBox.setDisable(false);
            modelloComboBox.getSelectionModel().clearSelection();
            coloreComboBox.setDisable(true);
            ruoteComboBox.setDisable(true);
        }
    }

    @FXML
    private void onModelloSelected() {
        String selectedMarca = marcaComboBox.getValue();
        String selectedModello = modelloComboBox.getValue();
        if (selectedModello != null) {
            List<String> colori = getColoriForModello(selectedMarca, selectedModello);
            coloreComboBox.setItems(FXCollections.observableArrayList(colori));
            coloreComboBox.setDisable(false);
            coloreComboBox.getSelectionModel().clearSelection();
            ruoteComboBox.setDisable(true);
        }
    }

    @FXML
    private void onColoreSelected() {
        String selectedColore = coloreComboBox.getValue();
        if (selectedColore != null) {
            List<String> ruote = Arrays.asList("RuoteGrandi", "RuoteBase");
            ruoteComboBox.setItems(FXCollections.observableArrayList(ruote));
            ruoteComboBox.setDisable(false);
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

