package com.example.configuratoreautoonline;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public abstract class PersonSession {
    // private static PersonSession instance;
    private SimpleStringProperty email;
    private SimpleStringProperty nome;
    private SimpleStringProperty cognome;
    private SimpleStringProperty numero;
    private SimpleStringProperty codiceFiscale;
    private SimpleStringProperty citta;
    private SimpleStringProperty via;
    private SimpleIntegerProperty civico;
    private SimpleStringProperty provincia;

    private SimpleIntegerProperty permessi;

    public PersonSession() {
        email = new SimpleStringProperty();
        nome = new SimpleStringProperty();
        cognome = new SimpleStringProperty();
        numero = new SimpleStringProperty();
        codiceFiscale = new SimpleStringProperty();
        citta = new SimpleStringProperty();
        via = new SimpleStringProperty();
        civico = new SimpleIntegerProperty();
        provincia = new SimpleStringProperty();
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

    public void setPermessi(int permessi) {
        this.permessi.set(permessi);
    }

    public int getPermessi() {
        return permessi.get();
    }

    public void aggiungiTutto(String email, String nome, String cognome, String numero, String codiceFiscale, String citta, String via, String provincia, int civico, int permessi) {
        setEmail(email);
        setNome(nome);
        setCognome(cognome);
        setNumero(numero);
        setCodiceFiscale(codiceFiscale);
        setCitta(citta);
        setVia(via);
        System.out.println("Cjttà" + citta + getCitta() + "\n" + "Via" + via + getVia() + "\n Nome" + getNome());
        setProvincia(provincia);
        setCivico(civico);
        setPermessi(permessi);
    }

}