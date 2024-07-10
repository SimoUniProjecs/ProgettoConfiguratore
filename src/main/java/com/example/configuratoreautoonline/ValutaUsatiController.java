package com.example.configuratoreautoonline;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
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

    private Stage stage;

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
        loadJSONData("public/res/data/datiAutoUsate.json");
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
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }

    private void loadJSONData(String jsonFilePath) {
        String marca = "", modello = "", km = "", proprietari = "", immatricolazione = "", imageUrl = "";
        // Legge il contenuto del file JSON


        // Imposta i dati nei campi della GUI
        marcaTxt.setText(marca);
        modelloTxt.setText(modello);
        kmTxt.setText(km);
        proprietariTxt.setText(proprietari);
        this.immatricolazione.setText(immatricolazione);

        // Carica e visualizza l'immagine
        loadImage(imageUrl);

    }
    private void loadImage (String path){
        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
            imageView.setImage(image);
        } catch (NullPointerException e) {
            showAlert("Loading Error", "Failed to load image: " + path);
        }
    }
    // Metodo per gestire l'azione dei MenuItem nel MenuBar
    @FXML
    public void handleMenuItemAction (ActionEvent event){
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
}