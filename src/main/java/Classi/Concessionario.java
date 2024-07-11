package Classi;

import java.util.ArrayList;
import java.util.List;

public class Concessionario {
    private String nome;
    private Indirizzo luogo;
    private List<Sede> sedi = new ArrayList<>();

    public Concessionario(String nome, Indirizzo luogo) {
        this.nome = nome;
        this.luogo = luogo;
    }

    public Concessionario(String nome, Indirizzo luogo, List<Sede> sedi) {
        this.nome = nome;
        this.luogo = luogo;
        this.sedi = sedi;
    }

    public String getNome() {
        return nome;
    }

    public Indirizzo getLuogo() {
        return luogo;
    }

    public List<Sede> getSedi() {
        return sedi;
    }

    public String toString() {
        return "Nome: " + nome + ", si trova in " + luogo.toString();
    }

    public void addSede(Sede sede) {
        sedi.add(sede);
    }
}
