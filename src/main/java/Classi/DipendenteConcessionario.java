package Classi;

import java.util.ArrayList;
import java.util.List;

import Enums.Mansione;
import Interfaces.Utente;

public class DipendenteConcessionario extends Persona implements Utente {


    private List<Mansione> mansioni = new ArrayList<>();

    public DipendenteConcessionario(String nome, String cognome, String codiceFiscale, String email, int telefono, Indirizzo indirizzo, Mansione mansione) {
        super(nome, cognome, codiceFiscale, email, telefono, indirizzo);
        mansioni.add(mansione);
    }

    @Override
    public void login(String username, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'login'");
    }

    public List<Mansione> getMansioni() {
        return mansioni;
    }

}
