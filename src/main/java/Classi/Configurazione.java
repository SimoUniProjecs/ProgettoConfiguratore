package Classi;

import Enums.Concessionari;

import java.time.LocalDate;
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
    private Concessionari luogoConcessionario;
    private String dataArrivo;

    public Configurazione() {}

    public Configurazione(int idConfigurazione, String marcaAutomobile, String modelloAutomobile, String colore, Motorizzazione motorizzazione, List<String> optionals, int prezzo, String emailCliente, Concessionari concessionario) {
        int numeroOptional = optionals.size();
        this.idConfigurazione = idConfigurazione;
        this.marcaAutomobile = marcaAutomobile;
        this.modelloAutomobile = modelloAutomobile;
        this.colore = colore;
        this.motorizzazione = motorizzazione;
        this.optionals = optionals;
        this.prezzo = prezzo;
        this.emailCliente = emailCliente;
        this.dataArrivo = LocalDate.now().plusDays(28+10*numeroOptional).toString();
        this.luogoConcessionario = concessionario;
    }

    public int getIdConfigurazione() {
        return idConfigurazione;
    }

    public void setIdConfigurazione(int idConfigurazione) {
        this.idConfigurazione = idConfigurazione;
    }

    public String getMarcaAutomobile() {
        return marcaAutomobile;
    }

    public void setMarcaAutomobile(String marcaAutomobile) {
        this.marcaAutomobile = marcaAutomobile;
    }

    public String getModelloAutomobile() {
        return modelloAutomobile;
    }

    public void setModelloAutomobile(String modelloAutomobile) {
        this.modelloAutomobile = modelloAutomobile;
    }

    public String getColore() {
        return colore;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }

    public Motorizzazione getMotorizzazione() {
        return motorizzazione;
    }

    public void setMotorizzazione(Motorizzazione motorizzazione) {
        this.motorizzazione = motorizzazione;
    }

    public List<String> getOptionals() {
        return optionals;
    }

    public void setOptionals(List<String> optionals) {
        this.optionals = optionals;
    }

    public int getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(int prezzo) {
        this.prezzo = prezzo;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public String getDataArrivo() {
        return dataArrivo;
    }

    public void setDataArrivo(String dataArrivo) {
        this.dataArrivo = dataArrivo;
    }

    public Concessionari getLuogoConcessionario() {
        return luogoConcessionario;
    }

    public void setLuogoConcessionario(Concessionari luogoConcessionario) {
        this.luogoConcessionario = luogoConcessionario;
    }
}
