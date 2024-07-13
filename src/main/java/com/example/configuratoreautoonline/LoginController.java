package com.example.configuratoreautoonline;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class LoginController {
    @FXML
    TextField emailField;
    @FXML
    PasswordField passwordField;
    @FXML
    private Label messageLbl;

    @FXML
    protected boolean controlloEmail() {
        String email = emailField.getText();
        if (!EmailValidator.emailPresente(email)) {
            messageLbl.setText("Email non trovata");
            return false;
        } else if (!EmailValidator.isValidEmail(email)) {
            messageLbl.setText("Email non soddisfa requisiti");
            return false;
        }
        if (passwordValida(email)) {
            try {
                UserSession session = UserSession.getInstance();
                JsonNode userInfo = getUserInfo(email);
                if (userInfo != null) {
                    String emailVal = getNodeValue(userInfo, "email");
                    String nomeVal = getNodeValue(userInfo, "nome");
                    String cognomeVal = getNodeValue(userInfo, "cognome");
                    String telefonoVal = getNodeValue(userInfo, "telefono");
                    String viaVal = getNodeValue(userInfo, "via");
                    String codiceFiscale = getNodeValue(userInfo, "codiceFiscale");
                    String citta = getNodeValue(userInfo, "citta");
                    String provincia = getNodeValue(userInfo, "provincia");
                    String civicoStr = getNodeValue(userInfo, "civico");
                    int civico = civicoStr.isEmpty() ? -1 : Integer.parseInt(civicoStr);  // Gestione del valore vuoto
                    int permessiVal = userInfo.has("permessi") ? userInfo.get("permessi").asInt() : -1;

                    login(emailVal, nomeVal, cognomeVal, telefonoVal, codiceFiscale, citta, viaVal, provincia, civico, permessiVal);

                    // Chiudi la finestra di login
                    Stage stage = (Stage) emailField.getScene().getWindow();
                    stage.close();

                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                messageLbl.setText("Errore nel recupero delle informazioni utente");
                return false;
            }
        }
        return false;
    }

    /**
     * Ottiene le informazioni di un utente dal file JSON "datiUtenti.json" basandosi sull'email fornita.
     * Restituisce il nodo JSON dell'utente corrispondente all'email, o null se l'utente non è trovato.
     *
     * @param email L'email dell'utente di cui si vogliono ottenere le informazioni.
     * @return Il nodo JSON dell'utente corrispondente all'email, o null se non trovato.
     * @throws IOException Se si verifica un errore durante la lettura del file JSON.
     */
    private JsonNode getUserInfo(String email) throws IOException {
        // Percorso del file JSON contenente i dati degli utenti
        File file = new File("public/res/data/datiUtenti.json");

        // Creazione dell'oggetto ObjectMapper per gestire JSON
        ObjectMapper mapper = new ObjectMapper();

        // Lettura del file JSON in un oggetto JsonNode
        JsonNode rootNode = mapper.readTree(file);

        // Ottenere il nodo "datiUtenti" dal nodo radice
        JsonNode datiUtenti = rootNode.get("datiUtenti");

        // Verifica se il nodo "datiUtenti" è un array
        if (datiUtenti.isArray()) {
            // Itera attraverso ogni nodo utente nell'array "datiUtenti"
            for (JsonNode userNode : datiUtenti) {
                // Verifica se il nodo utente ha la chiave "email" e se il suo valore corrisponde all'email fornita
                if (userNode.has("email") && userNode.get("email").asText().equals(email)) {
                    // Se l'email corrisponde, restituisci il nodo utente trovato
                    return userNode;
                }
            }
        }

        // Se l'utente non è trovato, restituisce null
        return null;
    }

    /**
     * Restituisce il valore di un nodo JSON per una chiave specificata.
     * Se il nodo non contiene la chiave, restituisce una stringa vuota.
     *
     * @param node Il nodo JSON da cui ottenere il valore.
     * @param key La chiave del valore desiderato nel nodo JSON.
     * @return Il valore come stringa, o una stringa vuota se la chiave non è presente nel nodo.
     */
    private String getNodeValue(JsonNode node, String key) {
        // Verifica se il nodo contiene la chiave specificata
        if (node.has(key)) {
            // Restituisce il valore associato alla chiave come stringa
            return node.get(key).asText();
        } else {
            // Se la chiave non è presente nel nodo, restituisce una stringa vuota
            return "";
        }
    }
    @FXML
    protected boolean passwordValida(String email) { // Controllo la validità della password
        ControlloPassword controllaPassword = new ControlloPassword();
        if (ControlloPassword.contienePsw(passwordField.getText(), email)) {
            return true;
        } else {
            messageLbl.setText("Password errata");
            return false;
        }
    }
    protected void login(String email, String nome, String cognome, String telefono, String codiceFiscale, String citta, String via, String provincia, int civico, int permessi) {
        UserSession session = UserSession.getInstance();
        // Salvo la sessione corrente
        session.aggiungiTutto(
                email,
                nome,
                cognome,
                telefono,
                codiceFiscale,
                citta,
                via,
                provincia,
                civico,
                permessi
        );

        // Imposta l'utente come loggato nella sessione
        session.setLoggato(true);

            // Mostra un popup di successo
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successo");
            alert.setHeaderText(null);
            alert.setContentText("Login effettuato correttamente");
            alert.showAndWait();

        // Chiude la finestra di login corrente
        Stage stage = (Stage) emailField.getScene().getWindow();
        Event.fireEvent(stage, new UserLoginEvent());
        stage.close();

    }
}
