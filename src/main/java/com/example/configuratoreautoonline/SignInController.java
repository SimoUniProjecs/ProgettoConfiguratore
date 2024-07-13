package com.example.configuratoreautoonline;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

// controllore per la pagina fxml dedicata alla pagina di sign in
public class SignInController {
    @FXML
    private TextField nomeField;
    @FXML
    private TextField cognomeField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField CodiceFiscaleField;
    @FXML
    private TextField CittaField;
    @FXML
    private TextField ViaField;
    @FXML
    private TextField TelefonoField;
    @FXML
    private TextField ProvinciaField;
    @FXML
    private TextField CivicoField;
    @FXML
    private Label passwordErrorLabel;

    private Stage stage;

    @FXML
    public void initialize() {
        passwordField.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (!passwordValida(passwordField.getText())) {
                passwordErrorLabel.setText("La password deve essere di almeno 8 caratteri, contenere una maiuscola e un numero.");
            } else {
                passwordErrorLabel.setText("");
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // funzione per prendere i dati dai campi della pagine, controllare la loro validità, e in caso siano validi li aggiunge al json con i dati
    // degli utenti oltre ad effettuare il login e aggiungerli alla UserSession
    @FXML
    protected void onSubmit() {
        try {
            String nome = nomeField.getText();
            String cognome = cognomeField.getText();
            String password = passwordField.getText();
            String email = emailField.getText();
            String telefono = TelefonoField.getText();
            String codiceFiscale = CodiceFiscaleField.getText();
            String citta = CittaField.getText();
            String via = ViaField.getText();
            String provincia = ProvinciaField.getText();
            int civico;

            // Controllo che non sia vuoto il campo
            if(CivicoField.getText().isEmpty()){
                civico = -1;
            } else {
                civico = Integer.parseInt(CivicoField.getText());
            }

            if (!campoVuotoPresente() && passwordValida(password) && controlloEmail(email)) { // se ci sono tutti i campi e soddisfano i requisiti
                aggiungiUtente(nome, cognome, password, email, codiceFiscale, citta, via, provincia, civico, telefono);

                // Esegui il login automatico
                autoLogin(email, password);

                // Chiudi la finestra di registrazione se il login automatico è completato correttamente
                if (this.stage != null) {
                    this.stage.close();
                }

            } else {
                // Differenzio i tipi di errori possibili
                if (campoVuotoPresente()) {
                    passwordErrorLabel.setText("Riempire i campi vuoti.");
                }else if(!passwordValida(password)){
                    passwordErrorLabel.setText("La password deve essere di almeno 8 caratteri, contenere una maiuscola e un numero.");
                }else if (!controlloEmail(email)){
                    passwordErrorLabel.setText("Email non valida.");
                }else if(!CodiceFiscaleField.getText().matches("^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$")){
                    passwordErrorLabel.setText("Codice fiscale non valido.");
                }else if(!TelefonoField.getText().matches("^[0-9]{10}$")) {
                    passwordErrorLabel.setText("Telefono non valido.");
                } else if(!CivicoField.getText().matches("^[0-9]{1,4}$")){
                    passwordErrorLabel.setText("Civico non valido.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Errore nel salvataggio dei dati: " + e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    // Controlla se ci sono campi vuoti
    private boolean campoVuotoPresente() {
        return nomeField.getText().isEmpty() || cognomeField.getText().isEmpty() || CodiceFiscaleField.getText().isEmpty() || CittaField.getText().isEmpty() || ProvinciaField.getText().isEmpty() || ViaField.getText().isEmpty() || CivicoField.getText().isEmpty() || TelefonoField.getText().isEmpty() || passwordField.getText().isEmpty() || emailField.getText().isEmpty();
    }

    // Aggiungi un nuovo utente al file JSON
    private void aggiungiUtente(String nome, String cognome, String password, String email, String codiceFiscale, String citta, String via, String provincia, int civico, String telefono) {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("public/res/data/datiUtenti.json");

        try {
            // Leggi il file JSON esistente o crea uno nuovo se non esiste
            ObjectNode root;
            if (file.exists()) {
                root = (ObjectNode) mapper.readTree(file);
            } else {
                root = mapper.createObjectNode();
                root.set("datiUtenti", mapper.createArrayNode());
            }
            ArrayNode utenti = (ArrayNode) root.get("datiUtenti");

            // Crea un nuovo utente e aggiungi al JSON array
            ObjectNode utente = mapper.createObjectNode();
            utente.put("nome", nome);
            utente.put("cognome", cognome);
            utente.put("password", password);
            utente.put("email", email);
            utente.put("codiceFiscale", codiceFiscale);
            utente.put("città", citta);
            utente.put("via", via);
            utente.put("provincia", provincia);
            utente.put("civico", civico);
            utente.put("permessi", 1);
            utente.put("idConfigurazione", -1);
            utente.put("telefono", telefono);

            utenti.add(utente);
            mapper.writeValue(file, root);
        } catch (IOException e) {
            e.printStackTrace();  // Improve error handling based on your application needs
        }
    }
    // Controlla se la password soddisfa i requisiti
    protected boolean passwordValida(String password) {
        ControlloPassword controlloPassword = new ControlloPassword();
        return controlloPassword.formatoCorretto(password);
    }

    // Controlla se l'email è già presente nel file JSON
    @FXML
    protected boolean controlloEmail(String email) {
        if (EmailValidator.emailPresente(email)) {
            passwordErrorLabel.setText("Email già in uso");
            return false;
        } else if (!EmailValidator.isValidEmail(email)) {
            passwordErrorLabel.setText("Email non soddisfa requisiti");
            return false;
        }
        return true;
    }

    // Effettua il login automatico dopo la registrazione
    private void autoLogin(String email, String password) {
        try {
            // Carica il file FXML del LoginController
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/configuratoreautoonline/login-view.fxml"));
            Parent root = loader.load();

            // Ottieni il controller del Login e imposta i campi necessari
            LoginController loginController = loader.getController();

            // Assegna i valori al LoginController
            loginController.emailField.setText(email);
            loginController.passwordField.setText(password);

            // Imposta la nuova scena sullo stage
            Scene scene = new Scene(root);
            Stage loginStage = new Stage();
            loginStage.setScene(scene);

            // Prova ad effettuare il login
            loginController.controlloEmail();

            // Puoi chiudere l'attuale finestra di registrazione se necessario
            if (this.stage != null) {
                this.stage.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Errore durante il login automatico: " + e.getMessage(), ButtonType.OK).showAndWait();
        }
    }
}