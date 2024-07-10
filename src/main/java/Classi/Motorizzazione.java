package Classi;

public class Motorizzazione {
    private String cilindrata;
    private String potenza;
    private String coppia;
    private String alimentazione;
    private String prezzo;

    public Motorizzazione() {}

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

    public void setCilindrata(String cilindrata) {
        this.cilindrata = cilindrata;
    }

    public String getPotenza() {
        return potenza;
    }

    public void setPotenza(String potenza) {
        this.potenza = potenza;
    }

    public String getCoppia() {
        return coppia;
    }

    public void setCoppia(String coppia) {
        this.coppia = coppia;
    }

    public String getAlimentazione() {
        return alimentazione;
    }

    public void setAlimentazione(String alimentazione) {
        this.alimentazione = alimentazione;
    }

    public String getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(String prezzo) {
        this.prezzo = prezzo;
    }

    @Override
    public String toString() {
        return alimentazione;
    }
}
