package Enums;

public enum Concessionari {
    CONCESSIONARIO1("Rovereto", "Via Rovereto, 1"),
    CONCESSIONARIO2("Trento", "Via Trento, 2"),
    CONCESSIONARIO3("Bolzano", "Via Bolzano, 3");

    private final String nome;
    private final String indirizzo;

    Concessionari(String nome, String indirizzo) {
        this.nome = nome;
        this.indirizzo = indirizzo;
    }

    public String getNome() {
        return nome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }
}
