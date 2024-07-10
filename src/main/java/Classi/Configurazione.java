package Classi;

public class Configurazione {
    private int idConfigurazione;
    private String marcaAutomobile;
    private String modelloAutomobile;
    private String optionals;
    private int prezzo;
    private String emailCliente;


    public Configurazione(int idConfigurazione, String marca ,String modello ,String optionals, int prezzo, String emailCliente) {
        this.marcaAutomobile = marca;
        this.idConfigurazione = idConfigurazione;
        this.modelloAutomobile = modello;
        this.optionals = optionals;
        this.prezzo = prezzo;
        this.emailCliente = emailCliente;
    }

    public int getIdConfigurazione() {
        return idConfigurazione;
    }

    public String getModelloAutomobile() {
        return modelloAutomobile;
    }

    public String getOptionals() {
        return optionals;
    }

    public String toString() {
        return "Configurazione: " + idConfigurazione + ", " + modelloAutomobile.toString();
    }

    public void setIdConfigurazione(int idConfigurazione) {
        this.idConfigurazione = idConfigurazione;
    }

    public void setModelloAutomobile(String modelloAutomobile) {
        this.modelloAutomobile = modelloAutomobile;
    }

    public void setOptionals(String optional) {
        this.optionals = optionals;
    }
}
