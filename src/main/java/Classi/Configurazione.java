package Classi;

import java.util.List;

public class Configurazione {
    private int idConfigurazione;
    private String marcaAutomobile;
    private String modelloAutomobile;
    private String colore;
    private Motorizzazione motorizzazione;
    private List<String> optionals;
    private int prezzo;
    private String emailCliente;

    public Configurazione(int idConfigurazione, String marca, String modello, String colore, Motorizzazione motorizzazione, List<String> optionals, int prezzo, String emailCliente) {
        this.idConfigurazione = idConfigurazione;
        this.marcaAutomobile = marca;
        this.modelloAutomobile = modello;
        this.colore = colore;
        this.motorizzazione = motorizzazione;
        this.optionals = optionals;
        this.prezzo = prezzo;
        this.emailCliente = emailCliente;
    }

    // Getter e setter
    public int getIdConfigurazione() {
        return idConfigurazione;
    }

    public String getMarcaAutomobile() {
        return marcaAutomobile;
    }

    public String getModelloAutomobile() {
        return modelloAutomobile;
    }

    public String getColore() {
        return colore;
    }

    public Motorizzazione getMotorizzazione() {
        return motorizzazione;
    }

    public List<String> getOptionals() {
        return optionals;
    }

    public int getPrezzo() {
        return prezzo;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    @Override
    public String toString() {
        return "Configurazione: " + idConfigurazione + ", " + marcaAutomobile + ", " + modelloAutomobile + ", " + colore + ", " + motorizzazione.toString();
    }
}
