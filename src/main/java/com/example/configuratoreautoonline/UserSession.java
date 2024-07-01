package com.example.configuratoreautoonline;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class UserSession {
    private static UserSession instance;
    private SimpleBooleanProperty loggato;
    private SimpleStringProperty email;
    private SimpleStringProperty nome;
    private SimpleStringProperty cognome;
    private SimpleIntegerProperty permessi;
    private SimpleStringProperty numero;
    private SimpleStringProperty codiceFiscale;
    private SimpleStringProperty citta;
    private SimpleStringProperty via;
    private SimpleIntegerProperty civico;
    private SimpleStringProperty provincia;

    private UserSession() {
        loggato = new SimpleBooleanProperty(false);
        email = new SimpleStringProperty();
        nome = new SimpleStringProperty();
        cognome = new SimpleStringProperty();
        permessi = new SimpleIntegerProperty();
        numero = new SimpleStringProperty();
        codiceFiscale = new SimpleStringProperty();
        citta = new SimpleStringProperty();
        via = new SimpleStringProperty();
        civico = new SimpleIntegerProperty();
        provincia = new SimpleStringProperty();
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // Getters e Setters per le informazioni utente
    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getNome() {
        return nome.get();
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public String getCognome() {
        return cognome.get();
    }

    public void setCognome(String cognome) {
        this.cognome.set(cognome);
    }

    public int getPermessi() {
        return permessi.get();
    }

    public void setPermessi(int permessi) {
        this.permessi.set(permessi);
    }

    public String getNumero() {
        return numero.get();
    }

    public void setNumero(String numero) {
        this.numero.set(numero);
    }

    public String getCodiceFiscale() {
        return codiceFiscale.get();
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale.set(codiceFiscale);
    }

    public String getCitta() {
        return citta.get();
    }

    public void setCitta(String citta) {
        this.citta.set(citta);
    }

    public String getVia() {
        return via.get();
    }

    public void setVia(String via) {
        this.via.set(via);
    }

    public int getCivico() {
        return civico.get();
    }

    public void setCivico(int civico) {
        this.civico.set(civico);
    }

    public String getProvincia() {
        return provincia.get();
    }

    public void setProvincia(String provincia) {
        this.provincia.set(provincia);
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

    public SimpleStringProperty nomeProperty() {
        return nome;
    }

    public void aggiungiTutto(String email, String nome, String cognome, String numero, String codiceFiscale, String citta, String via, String provincia, int civico, int permessi) {
        setEmail(email);
        setNome(nome);
        setCognome(cognome);
        setNumero(numero);
        setCodiceFiscale(codiceFiscale);
        setCitta(citta);
        setVia(via);
        setProvincia(provincia);
        setCivico(civico);
        setPermessi(permessi);
        setLoggato(true);
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
