package com.example.configuratoreautoonline;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.Objects;

public class HomeController {
    @FXML
    public static boolean loggato;
    @FXML
    private MenuItem switchToViewLogin;
    @FXML
    private ImageView audiImageView;
    @FXML
    private ImageView bmwImageView;
    @FXML
    private ImageView alfaImageView;
    @FXML
    private MenuItem switchToViewSingIn;
    @FXML
    private MenuItem vediDettagliUtente;
    @FXML
    private Menu userMenu;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleSwitchToViewLoginClick(ActionEvent event) {
        showDialog("login-view.fxml");
    }

    @FXML
    private void handleSwitchToViewSingInClick(ActionEvent event) {
        showSignInDialog();
    }

    @FXML
    private void handleViewUserDetailsClick(ActionEvent event) {
        System.out.println("View User Details clicked");
        changeScene("user-details.fxml");
    }

    private void changeScene(String fxmlFile) {
        try {
            System.out.println("Changing scene to: " + fxmlFile);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Recupera il controller della nuova scena se necessario
            UserDetailsController userDetailsController = loader.getController();
            userDetailsController.setStage(stage); // Passa lo stage se necessario

            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading FXML file: " + fxmlFile);
            // Puoi aggiungere un messaggio di errore per l'utente qui
        }
    }

    private void showDialog(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initOwner(stage);
            dialogStage.setScene(new Scene(root));
            dialogStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Puoi aggiungere un messaggio di errore per l'utente qui
        }
    }

    private void showSignInDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sign-in-view.fxml"));
            Parent root = loader.load();
            SignInController controller = loader.getController();
            Stage dialogStage = new Stage();
            dialogStage.initOwner(stage);
            controller.setStage(dialogStage);
            dialogStage.setScene(new Scene(root));
            dialogStage.setTitle("Registrazione Utente");
            dialogStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Puoi aggiungere un messaggio di errore per l'utente qui
        }
    }

    @FXML
    public void initialize() {
        updateMenuVisibility();
    }

    public void loadImages() {
        try {
            Image audiImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/LOGHI/Audi-Logo_2016.svg.png")));
            audiImageView.setImage(audiImage);

            Image bmwImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/LOGHI/BMW.svg.png")));
            bmwImageView.setImage(bmwImage);

            Image alfaImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/LOGHI/alfa.svg.png")));
            alfaImageView.setImage(alfaImage);

        } catch (Exception e) {
            e.printStackTrace();
            // Gestisci l'errore, ad esempio mostrando un'immagine di default o un messaggio di errore
        }
    }

    public void updateMenuVisibility() {
        UserSession session = UserSession.getInstance();
        if (vediDettagliUtente != null) {
            vediDettagliUtente.setVisible(session.getAccesso());
        } else {
            System.out.println("vediDettagliUtente is null");
        }
    }
}