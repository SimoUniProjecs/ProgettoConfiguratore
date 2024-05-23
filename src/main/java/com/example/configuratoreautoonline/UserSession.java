package com.example.configuratoreautoonline;

public class UserSession {
    private static UserSession instance;

    private boolean accessoEseguito = false;
    private String email;
    private String nome;
    private String cognome;
    private int permessi;
    private String numero;
    private String codiceFiscale;
    private String citta;
    private String via;
    private int civico;
    private String provincia;

    private UserSession() { }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // Getters e Setters per le informazioni utente
    public String getEmail() {
        return email;
    }

    public void aggiungiTutto(String email, String nome, String cognome, String numero, String codiceFiscale, String citta, String via, String provincia, int civico, int permessi  )    {
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.numero = numero;
        this.codiceFiscale = codiceFiscale;
        this.citta = citta;
        this.civico = civico;
        this.permessi = permessi;
        this.via = via;
        this.provincia = provincia;
        this.accessoEseguito = true;
    }

    public boolean getAccesso() {
        return accessoEseguito;
    }

    public void disconnetti()   {
        accessoEseguito = false;
    }

    public String getNome() {
        return nome;
    }

    public String getNumero() {
        return numero;
    }

    public String getCodiceFiscale()    {
        return codiceFiscale;
    }

    public String getCitta()    {
        return citta;
    }

    public String getCognome()  {
        return cognome;
    }

    public String getVia()  {
        return via;
    }

    public int getCivico()   {
        return civico;
    }

    public String getProvincia()    {
        return provincia;
    }

    public int getPermessi()    {
        return permessi;
    }

    //DA AGGIUNGERE OPPORTUNI SETTERS
}
