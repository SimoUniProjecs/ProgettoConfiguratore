package com.example.configuratoreautoonline;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
    private VBox imageContainer;

    @FXML
    private AnchorPane pannelloAncora;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void handleHomeButtonAction(ActionEvent event) {
        changeScene("/com/example/configuratoreautoonline/Home-view.fxml");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
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
                String targetPath = "public/res/images/" + imageFileName;

                // Crea un nuovo file nel percorso target
                File targetFile = new File(targetPath);

                try {
                    // Copia il file selezionato nel percorso target
                    Files.copy(selectedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    // Imposta l'immagine nel VBox
                    Image image = new Image(targetFile.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(100); // Imposta la larghezza desiderata
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
            Stage currentStage = (Stage) pannelloAncora.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.show();
        } catch (Exception e) {
            showAlert("Error loading scene", "Cannot load scene from file: " + fxmlFile + "\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    // Scrivo la modifica nel json delle auto disponibili
    public void oncaricaConfigurazioneBtnClicked(ActionEvent event) {
    }

    public void onSelezionaImmaginiBtnSelected(ActionEvent event) {

    }

}
