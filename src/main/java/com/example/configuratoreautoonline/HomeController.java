package com.example.configuratoreautoonline;

import Classi.Concessionario;
import Classi.DataLoader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class HomeController {
    public MenuItem logout;
    private List<Concessionario> concessionari;
    @FXML
    private MenuItem mieiOrdini;
    @FXML
    private MenuItem switchToViewLogin;
    @FXML
    private ImageView audiImageView;
    @FXML
    private ImageView bmwImageView;
    @FXML
    private ImageView alfaImageView;
    @FXML
    private MenuItem mieiPreventivi;
    @FXML
    private AnchorPane pannelloAncora;
    @FXML
    private MenuItem switchToViewSignIn;
    @FXML
    private MenuItem vediDettagliUtente;
    @FXML
    private MenuItem switchToInserisciVeicolo;
    @FXML
    private MenuItem messaggi;
    @FXML
    private Menu userNameMenu;
    @FXML
    private Menu VendiMenu;
    @FXML
    private Menu loginVisibilityMenu;
    @FXML
    private Menu secretaryVisibilityMenu;
    @FXML
    private MenuItem gestisciDipendenti;
    private Stage stage;
    @FXML
    private Menu configuraMenu;
    private static JsonNode datiModelliAuto;
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
    private void handleSwitchToMieiOrdiniClick(ActionEvent event) {
        changeScene("/com/example/configuratoreautoonline/miei-ordini.fxml");
    }

    @FXML
    private void handleSwitchToPreventiviClick(ActionEvent event) {
        changeScene("/com/example/configuratoreautoonline/preventivi.fxml");
    }

    @FXML
    private void handleVendiClick(ActionEvent event) {
        changeScene("/com/example/configuratoreautoonline/vendi.fxml");
    }

    @FXML
    private void handleSwitchToViewSignInClick(ActionEvent event) {
        showSignInDialog();
    }

    @FXML
    private void handleViewUserDetailsClick(ActionEvent event) {
        showDialog("/com/example/configuratoreautoonline/user-details.fxml");
    }

    // Metodo per cambiare la scena al configuratore per una marca specifica
    private void changeSceneToConfiguratore(String fxmlFile, String marca) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Ottieni il controller associato alla nuova scena
            CarConfiguratorController controller = loader.getController();
            controller.initData(marca);

            // Ottieni lo Stage dalla scena corrente
            Stage currentStage = (Stage) pannelloAncora.getScene().getWindow();

            Scene scene = new Scene(root);
            currentStage.setScene(scene); // Usa il currentStage
            currentStage.show();
        } catch (IOException e) {
            showAlert("Error loading scene", "Cannot load scene from file: " + fxmlFile + "\n" + e.getMessage());
            e.printStackTrace();
        }
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

    public static void loadJsonData() {
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File("public/res/data/datiModelliAuto.json");

        try {
            datiModelliAuto = objectMapper.readTree(jsonFile).get("datiModelliAuto");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> restituisciMarche() {
        List<String> marcheTrovate = new ArrayList<>();

        if (datiModelliAuto == null || datiModelliAuto.isEmpty()) {
            // Gestione dell'errore nel caricamento dei dati JSON
            showAlert("Errore", "Impossibile caricare i dati del file JSON.");
            return marcheTrovate;
        }

        // Itera attraverso gli elementi di datiModelliAuto
        for (JsonNode concessionarioNode : datiModelliAuto) {
            Iterator<String> keys = concessionarioNode.fieldNames();
            while (keys.hasNext()) {
                String key = keys.next();
                marcheTrovate.add(key); // Aggiungi la marca trovata alla lista
            }
        }

        return marcheTrovate;
    }

    // Metodo per aggiornare dinamicamente il menu "configura" con le marche disponibili
    private void updateConfiguraMenu() {
        List<String> marche = restituisciMarche();

        // Pulisci il menu "configura" se ci sono già elementi
        configuraMenu.getItems().clear();

        // Aggiungi ogni marca come un MenuItem nel menu "configura"
        for (String marca : marche) {
            MenuItem menuItem = new MenuItem(marca);
            menuItem.setOnAction(event -> handleConfiguraMarcaClick(event, marca));
            configuraMenu.getItems().add(menuItem);
        }
    }

    // Metodo per gestire il click su una marca nel menu "configura"
    private void handleConfiguraMarcaClick(ActionEvent event, String marca) {
        // Cambia la scena al configuratore per la marca specificata
        String fxmlFile = "/com/example/configuratoreautoonline/configuratore.fxml";
        changeSceneToConfiguratore(fxmlFile, marca);
    }

    @FXML
    public void initialize() {
        updateMenuVisibility();
        loadImages();
        loadJsonData();
        pannelloAncora.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.widthProperty().addListener((observable, oldValue, newValue) -> updateLayout(newScene.getWidth(), newScene.getHeight()));
                newScene.heightProperty().addListener((observable, oldValue, newValue) -> updateLayout(newScene.getWidth(), newScene.getHeight()));
            }
        });

        // Aggiorna il menu "configura" con le marche disponibili
        updateConfiguraMenu();

        // Carica i dati degli ordini dal file JSON
        try {
            concessionari = DataLoader.loadConcessionari("public/res/data/ordini.json");

        } catch (IOException e) {
            showAlert("Error", "Cannot load data from JSON file: " + e.getMessage());
            e.printStackTrace();
        }

        // Aggiungi listener per aggiornare la visibilità del menu e la label del nome utente
        UserSession.getInstance().loggatoProperty().addListener((obs, wasLoggato, isNowLoggato) -> {
            updateMenuVisibility();
        });
        pannelloAncora.widthProperty().addListener((obs, oldVal, newVal) -> resizeBigImage());
        pannelloAncora.heightProperty().addListener((obs, oldVal, newVal) -> resizeBigImage());
    }
    private void resizeBigImage() {
        double width = pannelloAncora.getWidth() - 20; // 10 pixels padding on each side
        double height = pannelloAncora.getHeight() - 210; // Adjust for other components and padding

        bigImageView.setFitWidth(width);
        bigImageView.setFitHeight(height);
    }

    private void updateLayout(double width, double height) {
        // Calculate the new sizes and positions based on the scene size
        double newWidth = width - 20; // Example margin
        double newHeight = (height - 210) * 0.5; // Example calculations for the big image view
        bigImageView.setFitWidth(newWidth);
        bigImageView.setFitHeight(newHeight);
    }

    // Carica le immagini dei loghi delle marche
    public void loadImages() {
        loadImage(audiImageView, "/img/LOGHI/Audi-Logo_2016.svg.png");
        loadImage(bmwImageView, "/img/LOGHI/BMW.svg.png");
        loadImage(alfaImageView, "/img/LOGHI/alfa.svg.png");
        loadImage(bigImageView, "/img/LOGHI/home.png");
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
            gestisciDipendenti.setVisible(session.getPermessi()==3);
        }
        if(secretaryVisibilityMenu!=null)   {
            secretaryVisibilityMenu.setVisible(session.getPermessi()>=2);
        }
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
        UserSession session = UserSession.getInstance();
        session.disconnetti();
        updateMenuVisibility();
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
                showAlert("File Not Found", "File datiUtenti.json non trovato.");
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
                showAlert("Utente non trovato", "L'utente con l'email " + email + " non è stato trovato.");
            }

        } catch (IOException e) {
            e.printStackTrace();  // Migliora la gestione degli errori in base alle necessità dell'applicazione
        }
    }

    public void handleSwitchToValutaUsatiClick(ActionEvent event) {
        changeScene("/com/example/configuratoreautoonline/valutaUsati.fxml");
    }

    public void handleInserisciVeicoloClick(ActionEvent event) {
        changeScene("/com/example/configuratoreautoonline/inserisci-veicolo.fxml");
    }
    @FXML
    private void handleSwitchToMessaggiClick(ActionEvent event) {
        changeScene("/com/example/configuratoreautoonline/comunicazioni.fxml");
    }
    public void handleSwitchToSegretariaClick(ActionEvent event) {
        changeScene("/com/example/configuratoreautoonline/segretaria.fxml");
    }
}