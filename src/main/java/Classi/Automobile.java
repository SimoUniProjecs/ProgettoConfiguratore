package Classi;

public class Automobile {
    private String marca;
    private String modello;
    private String colore;
    private String alimentazione;
    private String cambio;
    private double prezzo;
    private String immagine;
    private Dimensione dimensione;

    private static String[] optionals = {
        "pastiglie rosse",
        "cerchi maggiorati",
        "vetri oscurati",
        "colore carrozzeria personalizzato",
        "abbonamento annuale per colonnine pubbliche",
        "tetto apribile",
        "interno in pelle",
        "impianto audio Harman Kardon",
        "cambio automatico"
    };
    

    public Automobile(String marca, String modello, String colore, String alimentazione, String cambio, double prezzo, String immagine) {
        this.marca = marca.toLowerCase();
        this.modello = modello.toLowerCase();
        this.colore = colore.toLowerCase();
        this.alimentazione = alimentazione;
        this.cambio = cambio.toLowerCase();
        this.prezzo = prezzo;
        this.immagine = immagine;
    }

    public String getMarca() {
        return marca;
    }

    public String getModello() {
        return modello;
    }

    public String getColore() {
        return colore;
    }

    public String getAlimentazione() {
        return alimentazione;
    }

    public String getCambio() {
        return cambio;
    }

    public Dimensione getDimensione() {
        return dimensione;
    }

    public int getAltezza() {
        return dimensione.getAltezza();
    }

    public int getLarghezza() {
        return dimensione.getLarghezza();
    }

    public int getLitriBagagliaio() {
        return dimensione.getLitriBagagliaio();
    }

    public int getLunghezza() {
        return dimensione.getLunghezza();
    }

    public double getPrezzo() {
        return prezzo;
    }

    public String getImmagine() {
        return immagine;
    }

    // da capire come fare per differenziarli per modello
    public String[] getOptionalsDisponibili() {
        switch (marca) {
            case "audi":
                
                break;
            case "bmw":
                
                break;
            case "alfa romeo":
                
                break;
            default:
                break;
        }
        
        return optionals;
    }
}
