package Interfaces;

import Classi.Utente;

public interface Ordine {
        String idOrdine = "";
        String email = "";
        Configurazione config = null;


        public int getId();

        public String getData();

        public String getStato();

        public Configurazione getConfigurazione();

        public Classi.Utente getUtente();

        public void setId(int id);

        public void setData(String data);

        public void setStato(String stato);

        public void setConfigurazione(Classi.Configurazione configurazione);

        public void setUtente(Utente utente);

}
