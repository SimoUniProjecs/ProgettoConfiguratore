package com.example.configuratoreautoonline;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class HomeController {
    public MenuItem logout;
    @FXML
    private MenuItem switchToViewLogin;
    @FXML
    private ImageView audiImageView;
    @FXML
    private ImageView bmwImageView;
    @FXML
    private ImageView alfaImageView;
    @FXML
    private MenuItem switchToViewSignIn;
    @FXML
    private MenuItem vediDettagliUtente;
    @FXML
    private Label userNameLabel;
    @FXML
    private Menu userNameMenu;
    @FXML
    private Menu loginVisibilityMenu;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleSwitchToViewLoginClick(ActionEvent event) {
        showDialog("/com/example/configuratoreautoonline/login-view.fxml");
    }

    @FXML
    private void handleSwitchToViewSignInClick(ActionEvent event) {
        showSignInDialog();
    }

    @FXML
    private void handleViewUserDetailsClick(ActionEvent event) {
        showDialog("/com/example/configuratoreautoonline/user-details.fxml");
    }

    private void changeScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            showAlert("Error loading scene", "Cannot load scene from file: " + fxmlFile);
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
            showAlert("Error", "Cannot open the dialog, please check your configuration.");
        }
    }

    private void showSignInDialog() {
        showDialog("/com/example/configuratoreautoonline/sign-in-view.fxml");
    }

    public void handleLoginSuccessful() {
        updateMenuVisibility();
    }

    @FXML
    public void initialize() {
        updateMenuVisibility();
        loadImages();

        // Aggiungi listener per aggiornare la visibilità del menu e la label del nome utente
        UserSession.getInstance().loggatoProperty().addListener((obs, wasLoggato, isNowLoggato) -> {
            updateMenuVisibility();
            updateUserNameLabel();
        });
    }

    // Carica le immagini dei loghi delle marche
    void loadImages() {
        loadImage(audiImageView, "/img/LOGHI/Audi-Logo_2016.svg.png");
        loadImage(bmwImageView, "/img/LOGHI/BMW.svg.png");
        loadImage(alfaImageView, "/img/LOGHI/alfa.svg.png");
    }

    // Carica le 3 immagini dei loghi delle marche
    private void loadImage(ImageView imageView, String path) {
        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
            imageView.setImage(image);
        } catch (NullPointerException e) {
            showAlert("Loading Error", "Failed to load image: " + path);
        }
    }

    public void updateMenuVisibility() {
        UserSession session = UserSession.getInstance();
        if (vediDettagliUtente != null) {
            vediDettagliUtente.setVisible(session.isLoggato());
        }
        if(userNameMenu != null) {
            userNameMenu.setVisible(session.isLoggato());
        }
        if(loginVisibilityMenu != null) {
            loginVisibilityMenu.setVisible(!session.isLoggato());
        }
    }

    private void updateUserNameLabel() {
        if (UserSession.getInstance().isLoggato()) {
            this.userNameLabel.setVisible(true);
            // Rimuovi il binding prima di impostare il testo manualmente
            this.userNameLabel.textProperty().unbind();
            this.userNameLabel.setText(UserSession.getInstance().getNome());
        } else {
            this.userNameLabel.setVisible(false);
        }

        System.out.println(UserSession.getInstance());
    }

    // Stampare gli errori
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();

    }

    //effettua il logout
    public void handleLogoutClick(ActionEvent actionEvent) {
        // Clear the user session
        UserSession.getInstance().setLoggato(false);
    }

    // Se utente vuole sloggarsi e rimuovere dal JSON il suo userame
    public void handleEliminaUtenteClick(ActionEvent actionEvent) {
        // Clear the user session
        UserSession.getInstance().setLoggato(false);

        rimuoviUtente(UserSession.getInstance().getEmail());

        // Show a success popup
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rimozione con Successo");
        alert.setHeaderText(null);
        alert.setContentText("Utente rimosso con successo");
        alert.showAndWait();
    }

    // Rimuovi l'utente con l'email specificata dal file JSON
    private void rimuoviUtente(String email) {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("public/res/data/datiUtenti.json");

        try {
            // Leggi il file JSON esistente o esce se non esiste
            if (!file.exists()) {
                System.out.println("File datiUtenti.json non trovato.");
                return;
            }

            // Leggi il JSON dal file
            ObjectNode root = (ObjectNode) mapper.readTree(file);
            ArrayNode utenti = (ArrayNode) root.get("datiUtenti");

            // Cerca e rimuovi l'utente con l'email specificata
            boolean utenteTrovato = false;
            for (int i = 0; i < utenti.size(); i++) {
                JsonNode utente = utenti.get(i);
                if (utente.get("email").asText().equals(email)) {
                    utenti.remove(i);
                    utenteTrovato = true;
                    break;
                }
            }

            // Se l'utente è stato trovato e rimosso, riscrivi il file JSON
            if (utenteTrovato) {
                mapper.writeValue(file, root);
                System.out.println("Utente rimosso con successo.");
            } else {
                System.out.println("Utente non trovato.");
            }

        } catch (IOException e) {
            e.printStackTrace();  // Migliora la gestione degli errori in base alle necessità dell'applicazione
        }
    }
}
