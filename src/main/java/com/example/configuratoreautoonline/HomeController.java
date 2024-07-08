package com.example.configuratoreautoonline;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration; // Assicurati di importare correttamente Duration

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
    @FXML
    private MenuItem gestisciDipendenti;
    private Stage stage;
    @FXML
    private ImageView bigImageView;
    @FXML
    private Timeline timeline;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleSwitchToViewLoginClick(ActionEvent event) {
        showDialog("/com/example/configuratoreautoonline/login-view.fxml");
    }
    @FXML
    private void handlePopUpDipendent(ActionEvent event){
        showDialog("/com/example/configuratoreautoonline/gestisci-dipendenti.fxml");
    }
    @FXML
    private void initializeImageSlider() {
        // Array di URL delle immagini per lo slider
        String[] imageUrls = {
                "/img/AUDI/RS3/colore_nero.png",
                "/img/ALFA/GIULIA/colore_grigio.png",
                "/img/BMW/M2/colore_azzurro.png",
                "/img/ALFA/GIULIA/colore_rosso.png",
                "/img/BMW/XM/pastiglie_rosse_cerchi_grandi.png",
                "/img/AUDI/RS4/colore_bianco_vetri_oscurati_cerchi_neri.png",
                "/img/ALFA/GIULIA/colore_rosso_cerchi_grandi.png",
                "/img/ALFA/STELVIO/colore_blu.png",
      };

        // Indice iniziale dell'array delle immagini
        int[] index = {0};

        // Carico la prima immagine iniziale
        try {
            // Carica l'immagine successiva
            Image nextImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imageUrls[(imageUrls.length-1) / 2])));
            bigImageView.setImage(nextImage);

        } catch (NullPointerException e) {
            // Incrementa l'indice per la prossima immagine
            index[0] = (index[0] + 1) % imageUrls.length;
            System.out.println("Errore nel caricamento dell'immagine con indice: " + index[0]);
        }

        // Timeline per cambiare l'immagine ogni 5 secondi
        timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            try {
                // Carica l'immagine successiva
                Image nextImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imageUrls[index[0]])));
                bigImageView.setImage(nextImage);

                // Incrementa l'indice per la prossima immagine
                index[0] = (index[0] + 1) % imageUrls.length;
            } catch (NullPointerException e) {
                    // Incrementa l'indice per la prossima immagine
                    index[0] = (index[0] + 1) % imageUrls.length;
                    System.out.println("Errore nel caricamento dell'immagine con indice: " + index[0]);
            }
        }));

        timeline.setCycleCount(Animation.INDEFINITE); // Esecuzione indefinita della Timeline
        timeline.play(); // Avvia la Timeline
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



        // Inizializza e avvia il cambio delle immagini ogni 5 secondi
        initializeImageSlider();
    }

    // Carica le immagini dei loghi delle marche
    public void loadImages() {
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
        if(gestisciDipendenti!=null)    {
            gestisciDipendenti.setVisible(session.getPermessi()==7);
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
        alert.show();

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
