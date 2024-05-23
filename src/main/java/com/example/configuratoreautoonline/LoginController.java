package com.example.configuratoreautoonline;

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
            // Ottieni le informazioni dell'utente dal JSON
            try {
                UserSession session = UserSession.getInstance();
                JsonNode userInfo = getUserInfo(email);
                if (userInfo != null) {
                    String emailVal = getNodeValue(userInfo, "email");
                    String nomeVal = getNodeValue(userInfo, "nome");
                    String cognomeVal = getNodeValue(userInfo, "cognome");
                    String telefonoVal = getNodeValue(userInfo, "telefono");
                    String indirizzoVal = getNodeValue(userInfo, "indirizzo");
                    int permessiVal = userInfo.has("permessi") ? userInfo.get("permessi").asInt() : -1;

                    session.aggiungiTutto(
                            emailVal,
                            nomeVal,
                            cognomeVal,
                            telefonoVal,
                            "", // assuming codiceFiscale is not available in JSON
                            indirizzoVal,
                            "", // assuming provincia is not available in JSON
                            indirizzoVal, // You might need to correct this if it's not accurate
                            -1, // assuming civico is not available in JSON
                            permessiVal
                    );
                    HomeController.loggato = true;

                    // Chiudi la finestra di login
                    Stage stage = (Stage) emailField.getScene().getWindow();
                    stage.close();

                    // Mostra un popup di successo
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successo");
                    alert.setHeaderText(null);
                    alert.setContentText("Login effettuato correttamente");
                    alert.showAndWait();

                    // Carica la finestra principale
                    Stage primaryStage = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Home-view.fxml"));
                    Parent root = loader.load();
                    HomeController homeController = loader.getController();
                    homeController.setStage(primaryStage);
                    primaryStage.setScene(new Scene(root));
                    primaryStage.setTitle("Pagina Principale");
                    primaryStage.show();

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
}
