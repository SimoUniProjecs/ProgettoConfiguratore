package Classi;

import java.util.ArrayList;
import java.util.List;

public class Sede {
    private String nome;
    private Indirizzo luogo;
    private List<Configurazione> ordini = new ArrayList<>();

    public Sede(String nome, Indirizzo luogo) {
        this.nome = nome;
        this.luogo = luogo;
    }

    public Sede(String nome, Indirizzo luogo, List<Integer> idOrdini) {
        this.nome = nome;
        this.luogo = luogo;
    }

    public String getNome() {
        return nome;
    }

    public Indirizzo getLuogo() {
        return luogo;
    }

    public List<Configurazione> getOrdini() {
        return ordini;
    }

    public String toString() {
        return "Nome: " + nome + ", si trova in " + luogo.toString();
    }

    public void addOrdine(Configurazione configurazione) {
        ordini.add(configurazione);
    }
}
