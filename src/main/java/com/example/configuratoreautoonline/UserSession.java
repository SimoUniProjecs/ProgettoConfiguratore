package com.example.configuratoreautoonline;

import Enums.Concessionari;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class UserSession extends PersonSession {
    private static UserSession instance;
    private SimpleBooleanProperty loggato;
    private SimpleIntegerProperty permessi;
    private Concessionari concessionario;


    public UserSession() {
        super();
        loggato = new SimpleBooleanProperty(false);
        permessi = new SimpleIntegerProperty();
        concessionario = Concessionari.CONCESSIONARIO1;
    }

    public static UserSession getInstance() {
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
