package com.example.configuratoreautoonline;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.Image;

import javafx.scene.image.ImageView;

public class HomeApplication extends Application {
    @FXML
    private ImageView audiImageView;

    @FXML
    private ImageView bmwImageView;

    @FXML
    private ImageView alfaImageView;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HomeApplication.class.getResource("Home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Pagina Principale");
        stage.setScene(scene);

        // Get the controller and set the stage
        HomeController homeController = fxmlLoader.getController();
        homeController.setStage(stage);
        homeController.loadImages();

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
