package Classi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import Enums.Concessionari;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataLoader {

    public static List<Concessionario> loadConcessionari(String filePath) throws IOException {
        // Creazione di un ObjectMapper per la lettura del file JSON
        ObjectMapper objectMapper = new ObjectMapper();
        // Lettura del file JSON e memorizzazione della radice del nodo
        JsonNode rootNode = objectMapper.readTree(new File(filePath));

        // Creazione di una mappa per memorizzare le sedi dei concessionari
        Map<String, Sede> sedi = new HashMap<>();
        // Iterazione su tutti i valori dell'enumerazione Concessionari
        for (Concessionari concessionario : Concessionari.values()) {
            // Creazione e memorizzazione di una nuova Sede per ogni concessionario
            sedi.put(concessionario.name(), new Sede(concessionario.name(), new Indirizzo(concessionario.getIndirizzo(), "Città", "CAP", "Provincia", "Nazione", "Civico")));
        }

        // Iterazione su tutti i nodi JSON nella radice del nodo
        for (JsonNode node : rootNode) {
            // Creazione di una configurazione a partire dal nodo JSON
            Configurazione configurazione = creaConfigurazioneDaJson(node);
            // Ottenimento della sede corrispondente alla configurazione
            Sede sede = sedi.get(configurazione.getLuogoConcessionario().name());
            // Se la sede esiste, aggiunge la configurazione alla sede
            if (sede != null) {
                sede.addOrdine(configurazione);
            }
        }

        // Creazione di una lista per memorizzare i concessionari
        List<Concessionario> concessionari = new ArrayList<>();
        // Iterazione su tutte le sedi memorizzate nella mappa
        for (Sede sede : sedi.values()) {
            // Creazione di un nuovo concessionario con la sede
            Concessionario concessionario = new Concessionario(sede.getNome(), sede.getLuogo());
            // Aggiunta della sede al concessionario
            concessionario.addSede(sede);
            // Aggiunta del concessionario alla lista
            concessionari.add(concessionario);
        }

        // Ritorno della lista dei concessionari
        return concessionari;
    }

    // Ritorna un nuovo oggetto di tipo configurazione
    private static Configurazione creaConfigurazioneDaJson(JsonNode node) {
        int idConfigurazione = node.get("idConfigurazione").asInt();
        String marcaAutomobile = node.get("marcaAutomobile").asText();
        String modelloAutomobile = node.get("modelloAutomobile").asText();
        String colore = node.get("colore").asText();
        JsonNode motorizzazioneNode = node.get("motorizzazione");
        Motorizzazione motorizzazione = new Motorizzazione(
                motorizzazioneNode.get("cilindrata").asText(),
                motorizzazioneNode.get("potenza").asText(),
                motorizzazioneNode.get("coppia").asText(),
                motorizzazioneNode.get("alimentazione").asText(),
                motorizzazioneNode.get("prezzo").asText()
        );
        List<String> optionals = new ArrayList<>();
        for (JsonNode optionalNode : node.get("optionals")) {
            optionals.add(optionalNode.asText());
        }
        int prezzo = node.get("prezzo").asInt();
        String emailCliente = node.get("emailCliente").asText();
        Concessionari luogoConcessionario = Concessionari.valueOf(node.get("luogoConcessionario").asText());
        String dataArrivo = node.get("dataArrivo").asText();
        boolean pagato = node.get("pagato").asBoolean();

        return new Configurazione(idConfigurazione, marcaAutomobile, modelloAutomobile, colore, motorizzazione, optionals, prezzo, emailCliente, luogoConcessionario, dataArrivo, pagato);
    }
}
