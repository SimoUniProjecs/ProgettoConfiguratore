package Interfaces;

import Classi.Configurazione;

public interface Utente {
    String nome = "";
    String cognome = "";
    String email = "";
    String password = "";
    int permessi = 0;
    int idConfigurazione = 0;

    void login(String username, String password);
}
