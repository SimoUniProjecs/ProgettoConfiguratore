package Classi;

public class Indirizzo {
    private String via;
    private String citta;
    private String cap;
    private String provincia;
    private String nazione;
    private String civico;

    public Indirizzo(String via, String citta, String cap, String provincia, String nazione, String civico) {
        this.via = via;
        this.citta = citta;
        this.cap = cap;
        this.provincia = provincia;
        this.nazione = nazione;
        this.civico = civico;
    }

    public String getVia() {
        return via;
    }

    public String getCitta() {
        return citta;
    }

    public String getCap() {
        return cap;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getNazione() {
        return nazione;
    }

    public String getCivico() {
        return civico;
    }

    @Override
    public String toString() {
        return via + ", " + civico + ", " + cap + ", " + citta + ", " + provincia + ", " + nazione;
    }
}