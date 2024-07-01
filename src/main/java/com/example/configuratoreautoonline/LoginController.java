package com.example.configuratoreautoonline;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class LoginController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
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
                    String viaVal = getNodeValue(userInfo, "indirizzo");
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

                    // Mostra un popup di successo
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successo");
                    alert.setHeaderText(null);
                    alert.setContentText("Login effettuato correttamente");
                    alert.showAndWait();

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

    private JsonNode getUserInfo(String email) throws IOException {
        File file = new File("public/res/data/datiUtenti.json");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(file);
        JsonNode datiUtenti = rootNode.get("datiUtenti");
        if (datiUtenti.isArray()) {
            for (JsonNode userNode : datiUtenti) {
                if (userNode.has("email") && userNode.get("email").asText().equals(email)) {
                    return userNode;
                }
            }
        }
        return null;
    }

    private String getNodeValue(JsonNode node, String key) {
        return node.has(key) ? node.get(key).asText() : "";
    }

    @FXML
    protected boolean passwordValida(String email) {
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

        System.out.println("Login effettuato con successo");
        System.out.println("Utente: " + nome + " " + cognome);
        System.out.println("Stato di loggato: " + session.isLoggato());

        // Chiude la finestra di login corrente
        Stage stage = (Stage) emailField.getScene().getWindow();
        Event.fireEvent(stage, new UserLoginEvent());
        stage.close();

        // Potenzialmente notifica altri componenti del cambiamento di stato
        // Questo richiede che altri componenti 'osservino' lo stato di UserSession
    }


}
