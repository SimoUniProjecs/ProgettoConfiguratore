package Classi;

import java.util.ArrayList;
import java.util.List;

import Enums.Alimentazioni;
import Enums.Optionals;

public class Configurazione {
    private int idConfigurazione;
    private Automobile modelloAutomobile;
    private Motore motore;
    private List<Optionals> optionals = new ArrayList<>();

    public Configurazione(int idConfigurazione, Automobile modelloAutomobile, Motore motore, Alimentazioni alimentazione, Optionals optionals) {
        this.idConfigurazione = idConfigurazione;
        this.modelloAutomobile = modelloAutomobile;
        this.motore = motore;
    }

    public int getIdConfigurazione() {
        return idConfigurazione;
    }

    public Automobile getModelloAutomobile() {
        return modelloAutomobile;
    }

    public Motore getMotore() {
        return motore;
    }

    public List<Optionals> getOptionals() {
        return optionals;
    }

    public String toString() {
        return "Configurazione: " + idConfigurazione + ", " + modelloAutomobile.toString() + ", " + motore.toString();
    }

    public void setIdConfigurazione(int idConfigurazione) {
        this.idConfigurazione = idConfigurazione;
    }

    public void setModelloAutomobile(Automobile modelloAutomobile) {
        this.modelloAutomobile = modelloAutomobile;
    }

    public void setMotore(Motore motore) {
        this.motore = motore;
    }

    public void setOptionals(Optionals optional) {
        this.optionals.add(optional);
    }
}
