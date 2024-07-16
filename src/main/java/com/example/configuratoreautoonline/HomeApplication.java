package com.example.configuratoreautoonline;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class HomeApplication extends Application {

    private Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception { // Fa partire la home page dell'applicazione
        this.stage = primaryStage;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HomeApplication.class.getResource("Home-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Pagina Principale");
            stage.setScene(scene);

            // Get the screen size
            double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
            double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

            // Set full screen
            stage.setX(0);
            stage.setY(0);
            stage.setWidth(screenWidth);
            stage.setHeight(screenHeight);

            // Get the controller and set the stage
            HomeController homeController = fxmlLoader.getController();
            homeController.setStage(stage);
            homeController.loadImages();

            stage.show();
        } catch (Exception e) {
            System.out.println("Errore durante il caricamento della schermata principale");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}