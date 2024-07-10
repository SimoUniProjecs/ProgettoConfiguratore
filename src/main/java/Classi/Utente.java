package Classi;

public class Utente extends Persona {
    private String password;

    public Utente(String nome, String cognome, String codiceFiscale, String email, int telefono, Indirizzo indirizzo, String password) {
        super(nome, cognome, codiceFiscale, email, telefono, indirizzo);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}