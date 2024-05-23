package Classi;

public abstract class Persona {
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private String email;
    private int telefono;
    private Indirizzo indirizzo;

    public Persona(String nome, String cognome, String codiceFiscale, String email, int telefono, Indirizzo indirizzo) {
        this.nome = nome;
        this.cognome = cognome;
        this.telefono = telefono;
        this.indirizzo = indirizzo;
        this.codiceFiscale = codiceFiscale;
        this.email = email;
    }

    public Indirizzo getIndirizzo() {
        return indirizzo;
    }

    public int getTelefono() {
        return telefono;
    }
    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public String getEmail() {
        return email;
    }
}
