package Classi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MyApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Verifica del percorso del file FXML
            URL fxmlLocation = getClass().getResource("/com/example/configuratoreautoonline/home-view.fxml");
            System.out.println("FXML file location: " + fxmlLocation);

            if (fxmlLocation == null) {
                throw new RuntimeException("File FXML non trovato!");
            }

            // Caricamento del file FXML
            Parent root = FXMLLoader.load(fxmlLocation);

            // Impostazione della scena
            Scene scene = new Scene(root);

            // Impostazione del titolo della finestra
            primaryStage.setTitle("Configuratore Auto");
            primaryStage.setScene(scene);

            // Mostra la finestra
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
