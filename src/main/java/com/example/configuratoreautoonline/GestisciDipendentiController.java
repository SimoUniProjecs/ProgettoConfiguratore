package com.example.configuratoreautoonline;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class GestisciDipendentiController {

    @FXML
    private TextField emailFieldGD;

    @FXML
    private TextField numberFieldGD;

    @FXML
    private Button BtnAggiungi;

    @FXML
    private void handleSubmit() {
        // Ottieni l'email e il numero di permessi dai campi di input
        String email = emailFieldGD.getText();
        String number = numberFieldGD.getText();

        // Debug: stampa a console l'email e il numero di permessi
        System.out.println("arrivato qui " + email + " " + number);

        // Verifica se l'email è presente nel file dei dati degli utenti
        if (EmailValidator.emailPresente(email)) {
            System.out.println("Email presente");

            try {
                // Leggi il file JSON contenente i dati degli utenti
                ObjectMapper mapper = new ObjectMapper();
                File file = new File("public/res/data/datiUtenti.json");
                JsonNode root = mapper.readTree(file);
                JsonNode datiUtenti = root.get("datiUtenti");

                // Itera attraverso ogni utente nel nodo "datiUtenti"
                for (JsonNode user : datiUtenti) {
                    // Verifica se l'email dell'utente corrente corrisponde all'email inserita
                    if (user.get("email").asText().equals(email)) {
                        // Aggiorna il campo "permessi" dell'utente con il nuovo valore
                        ((ObjectNode) user).put("permessi", Integer.parseInt(number));
                        break; // Interrompi il ciclo dopo aver trovato l'utente
                    }
                }

                // Mostra una finestra di dialogo informativa per confermare l'avvenuta modifica
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successo");
                alert.setHeaderText(null);
                alert.setContentText("Permessi modificati correttamente");
                alert.showAndWait();

                // Scrivi le modifiche nel file JSON aggiornato
                mapper.writerWithDefaultPrettyPrinter().writeValue(file, root);

                // Chiudi la finestra corrente (finestra di modifica dei permessi)
                Stage stage = (Stage) numberFieldGD.getScene().getWindow();
                stage.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
