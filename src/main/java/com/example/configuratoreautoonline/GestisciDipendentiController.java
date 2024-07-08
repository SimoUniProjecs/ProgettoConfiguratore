package com.example.configuratoreautoonline;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
        String email = emailFieldGD.getText();
        String number = numberFieldGD.getText();
        System.out.println("arrivato qui " + email + " " + number);
        if (EmailValidator.emailPresente(email)) {
            System.out.println("Email presente");
            try {
                // Load the existing data
                ObjectMapper mapper = new ObjectMapper();
                File file = new File("public/res/data/datiUtenti.json"); // Adjust the path accordingly
                JsonNode root = mapper.readTree(file);
                JsonNode datiUtenti = root.get("datiUtenti");
                // Assuming root is an array of users
                for (JsonNode user : datiUtenti) {
                    if (user.get("email").asText().equals(email)) {
                        ((ObjectNode) user).put("permessi", Integer.parseInt(number));
                        break;
                    }
                }
                System.out.println("Aggiornato permessi");
                // Save the updated data back to the file
                mapper.writerWithDefaultPrettyPrinter().writeValue(file, root);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
