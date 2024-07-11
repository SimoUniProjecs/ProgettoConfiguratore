package Classi;

import java.util.ArrayList;
import java.util.List;

public class Sede {
    private String nome;
    private Indirizzo luogo;
    private List<Integer> idOrdini = new ArrayList<>();

    public Sede(String nome, Indirizzo luogo) {
        this.nome = nome;
        this.luogo = luogo;
    }

    public Sede(String nome, Indirizzo luogo, List<Integer> idOrdini) {
        this.nome = nome;
        this.luogo = luogo;
        this.idOrdini = idOrdini;
    }

    public String getNome() {
        return nome;
    }

    public Indirizzo getLuogo() {
        return luogo;
    }

    public List<Integer> getIdOrdini() {
        return idOrdini;
    }

    public String toString() {
        return "Nome: " + nome + ", si trova in " + luogo.toString();
    }

    public void addOrdine(int idOrdine) {
        idOrdini.add(idOrdine);
    }
}
