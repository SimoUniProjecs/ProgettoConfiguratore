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
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File(filePath));

        Map<String, Sede> sedi = new HashMap<>();
        for (Concessionari concessionario : Concessionari.values()) {
            sedi.put(concessionario.name(), new Sede(concessionario.name(), new Indirizzo(concessionario.getIndirizzo(), "Città", "CAP", "Provincia", "Nazione", "Civico")));
        }

        for (JsonNode node : rootNode) {
            Configurazione configurazione = creaConfigurazioneDaJson(node);
            Sede sede = sedi.get(configurazione.getLuogoConcessionario().name());
            if (sede != null) {
                sede.addOrdine(configurazione);
            }
        }

        List<Concessionario> concessionari = new ArrayList<>();
        for (Sede sede : sedi.values()) {
            Concessionario concessionario = new Concessionario(sede.getNome(), sede.getLuogo());
            concessionario.addSede(sede);
            concessionari.add(concessionario);
        }

        return concessionari;
    }

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
