package com.example.configuratoreautoonline;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

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

    @FXML
    protected void onSubmit() throws IOException {
        String nome = nomeField.getText();
        String cognome = cognomeField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        String telefono = TelefonoField.getText();
        String codiceFiscale = CodiceFiscaleField.getText();
        String citta = CittaField.getText();
        String via = ViaField.getText();
        String provincia = ProvinciaField.getText();
        int civico = Integer.parseInt(CivicoField.getText());

        if (passwordValida(password) && controlloEmail(email) && !campoVuotoPresente()) {
            aggiungiUtente(nome, cognome, password, email, codiceFiscale, citta, via, provincia, civico, telefono);

            // Chiudi il dialogo
            if (stage != null) {
                stage.close();
            }

            // Mostra un popup di successo
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successo");
            alert.setHeaderText(null);
            alert.setContentText("Sei Registrato Correttamente");
            alert.showAndWait();
        } else if (!controlloEmail(email)) {
            passwordErrorLabel.setText("La mail non è valida. Riprova.");
        } else if (!passwordValida(password)) {
            passwordErrorLabel.setText("La password non è valida. Riprova.");
        } else {
            passwordErrorLabel.setText("Completare tutti i campi. Riprova.");
        }
    }

    private boolean campoVuotoPresente() {
        return nomeField.getText().isEmpty() || cognomeField.getText().isEmpty() || CodiceFiscaleField.getText().isEmpty() || CittaField.getText().isEmpty() || ProvinciaField.getText().isEmpty() || ViaField.getText().isEmpty() || CivicoField.getText().isEmpty() || TelefonoField.getText().isEmpty();
    }

    private void aggiungiUtente(String nome, String cognome, String password, String email, String codiceFiscale, String citta, String via, String provincia, int civico, String telefono) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        File file = new File("public/res/data/datiUtenti.json");

        // Leggi il file JSON esistente
        ObjectNode root = (ObjectNode) mapper.readTree(file);
        ArrayNode utenti = (ArrayNode) root.get("datiUtenti");

        // Sovrascrivi il file JSON con i nuovi dati
        mapper.writeValue(file, root);

        // Faccio fare direttamente il login all'utente con i dati appena inseriti
        LoginController lg = new LoginController();
        System.out.print("Email: " + email);
        System.out.print(" Controllo PSW: " + passwordValida(email));
        System.out.print("Password: " + password);
        System.out.print(" Controllo EMAIL: " + controlloEmail(email));
        if (!(passwordValida(password) && controlloEmail(email))) {
            // Mostra un popup di errore
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore di Login");
            alert.setHeaderText(null);
            alert.setContentText("C'è stato un problema nel LOGIN, Riprovare");
            alert.showAndWait();
        }else{
            lg.login(email, nome, cognome, telefono, codiceFiscale, citta, via, provincia, civico, 1 );
            // Crea un nuovo utente
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

            // Aggiungi il nuovo utente all'array di utenti
            utenti.add(utente);
        }
    }

    protected boolean passwordValida(String password) {
        ControlloPassword controlloPassword = new ControlloPassword();
        return controlloPassword.formatoCorretto(password);
    }

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
}