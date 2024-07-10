package Classi;

import Enums.Alimentazioni;

public class Motore {
    private int cilindrata;
    private int potenza;
    private int numeroCilindri;
    private Alimentazioni alimentazione;

    public Motore(int cilindrata, int potenza, int numeroCilindri, Alimentazioni alimentazione) {
        this.cilindrata = cilindrata;
        this.potenza = potenza;
        this.numeroCilindri = numeroCilindri;
        this.alimentazione = alimentazione;
    }

    public int getCilindrata() {
        return cilindrata;
    }

    public int getPotenza() {
        return potenza;
    }

    public int getNumeroCilindri() {
        return numeroCilindri;
    }

    public Alimentazioni getAlimentazione() {
        return alimentazione;
    }

    public String toString() {
        return "Cilindrata: " + cilindrata + " Potenza: " + potenza + " Numero cilindri: " + numeroCilindri + " Alimentazione: " + alimentazione;
    }
}
