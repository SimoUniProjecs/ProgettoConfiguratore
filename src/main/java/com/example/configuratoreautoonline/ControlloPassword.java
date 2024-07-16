package com.example.configuratoreautoonline;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ControlloPassword { // controlla tutti i requisiti della password

    public boolean formatoCorretto(String password) {
        return password.length() >= 8 && contieneMaiuscola(password) && contieneNumero(password);
    }

    private boolean contieneMaiuscola(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean contieneNumero(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    public static boolean contienePsw(String psw, String email) {
        File file = new File("public/res/data/datiUtenti.json");
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode rootNode = mapper.readTree(file);
            JsonNode datiUtenti = rootNode.get("datiUtenti");
            if (datiUtenti.isArray()) {
                for (JsonNode userNode : datiUtenti) {
                    if (userNode.has("email") && userNode.get("email").asText().equals(email)) {
                        if (userNode.has("password") && userNode.get("password").asText().equals(psw)) {
                            return true;
                        }
                        return false; // Email trovata ma password non corrisponde
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false; // Email non trovata o errore nella lettura del file
    }
}
