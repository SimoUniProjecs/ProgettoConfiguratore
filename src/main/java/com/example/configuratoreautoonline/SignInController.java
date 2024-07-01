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
            int civico = Integer.parseInt(CivicoField.getText());

            if (!campoVuotoPresente() && passwordValida(password) && controlloEmail(email)) {
                aggiungiUtente(nome, cognome, password, email, codiceFiscale, citta, via, provincia, civico, telefono);

                // Close the dialog and show a success message
                if (stage != null) {
                    stage.close();
                }
                new Alert(Alert.AlertType.INFORMATION, "Sei Registrato Correttamente", ButtonType.OK).showAndWait();
            } else {
                passwordErrorLabel.setText("Controllare gli errori nei campi.");
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Errore nel salvataggio dei dati: " + e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    private boolean campoVuotoPresente() {
        return nomeField.getText().isEmpty() || cognomeField.getText().isEmpty() || CodiceFiscaleField.getText().isEmpty() || CittaField.getText().isEmpty() || ProvinciaField.getText().isEmpty() || ViaField.getText().isEmpty() || CivicoField.getText().isEmpty() || TelefonoField.getText().isEmpty();
    }

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