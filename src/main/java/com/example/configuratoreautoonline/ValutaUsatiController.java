package com.example.configuratoreautoonline;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ValutaUsatiController extends Application {

    public Button stimaBtn;
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane pannelloAncora;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/configuratoreautoonline/Vendi-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Configuratore Auto Online - Vendita");
        primaryStage.show();
    }

    @FXML
    public void initialize() {
        stage = new Stage();
    }
    public void onValutaUsatiClicked(ActionEvent event) {
        changeScene("/com/example/configuratoreautoonline/valutaUsati.fxml");
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
        } catch (Exception e) {
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
    public void onInserisciVeicoloClicked(ActionEvent event) {
        changeScene("/com/example/configuratoreautoonline/Vendi-view.fxml");
    }
    public void handleSwitchHome(ActionEvent event) {
        changeScene("/com/example/configuratoreautoonline/Home-view.fxml");
    }

    public void onStimaBtnClicked(ActionEvent event) {
    }

    public void onProssimoVeicoloClicked(ActionEvent event) {
    }
}