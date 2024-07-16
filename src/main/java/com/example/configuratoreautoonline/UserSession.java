package com.example.configuratoreautoonline;

import Enums.Concessionari;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class UserSession extends PersonSession {
    // Istanza unica della classe
    private static UserSession instance;
    private SimpleBooleanProperty loggato;
    private SimpleIntegerProperty permessi;
    private Concessionari concessionario; // concessionario di riferimento

    // Costruttore privato per evitare la creazione di nuove istanze
    private UserSession() {
        super();
        loggato = new SimpleBooleanProperty(false);
        permessi = new SimpleIntegerProperty();
        concessionario = Concessionari.CONCESSIONARIO1;
    }

    // Metodo pubblico statico per ottenere l'istanza unica della classe
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public int getPermessi() {
        return permessi.get();
    }

    public void setPermessi(int permessi) {
        this.permessi.set(permessi);
    }

    public boolean isLoggato() {
        return loggato.get();
    }

    public void setLoggato(boolean loggato) {
        this.loggato.set(loggato);
    }

    public BooleanProperty loggatoProperty() {
        return loggato;
    }

    // funzione per aggiungere tutti i dati
    public void aggiungiTutto(String email, String nome, String cognome, String numero, String codiceFiscale, String citta, String via, String provincia, int civico, int permessi) {
        super.aggiungiTutto(email, nome, cognome, numero, codiceFiscale, citta, via, provincia, civico, permessi);
        setLoggato(true);
    }

    public Concessionari getConcessionario() {
        return concessionario;
    }

    public void setConcessionario(Concessionari concessionario) {
        this.concessionario = concessionario;
    }

    public void disconnetti() {
        setEmail("");
        setNome("");
        setCognome("");
        setNumero("");
        setCodiceFiscale("");
        setCitta("");
        setVia("");
        setProvincia("");
        setCivico(0);
        setPermessi(0);
        setLoggato(false);
    }
}