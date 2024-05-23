package Classi;

public class Dimensione {
    private int lunghezza;
    private int larghezza;
    private int altezza;
    private int peso;
    private int litriBagagliaio;

    public Dimensione(int lunghezza, int larghezza, int altezza, int litriBagagliaio, int peso) {
        this.lunghezza = lunghezza;
        this.larghezza = larghezza;
        this.altezza = altezza;
        this.peso = peso;
        this.litriBagagliaio = litriBagagliaio;
    }

    public int getLunghezza() {
        return lunghezza;
    }

    public int getPeso() {
        return peso;
    }

    public int getLarghezza() {
        return larghezza;
    }

    public int getAltezza() {
        return altezza;
    }

    public int getLitriBagagliaio() {
        return litriBagagliaio;
    }
}
