package Classi;

public class Motorizzazione {
    private String cilindrata;
    private String potenza;
    private String coppia;
    private String alimentazione;
    private String prezzo;

    // Costruttori, getter e setter

    public Motorizzazione(String cilindrata, String potenza, String coppia, String alimentazione, String prezzo) {
        this.cilindrata = cilindrata;
        this.potenza = potenza;
        this.coppia = coppia;
        this.alimentazione = alimentazione;
        this.prezzo = prezzo;
    }

    public String getCilindrata() {
        return cilindrata;
    }

    public String getPotenza() {
        return potenza;
    }

    public String getCoppia() {
        return coppia;
    }

    public String getAlimentazione() {
        return alimentazione;
    }

    public String getPrezzo() {
        return prezzo;
    }

    @Override
    public String toString() {
        return cilindrata + " " + potenza + " " + coppia + " " + alimentazione + " " + prezzo;
    }
}
