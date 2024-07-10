DIAGRAMMA DELLE CLASSI:    

+-------------------------------------+
|             SistemaVendita           |
+-------------------------------------+
| - modelli: List<ModelloAuto>        |
| - sedi: List<SedeConsegna>          |
| - utenti: List<Utente>              |
| - preventivi: List<Preventivo>      |
+-------------------------------------+
| + aggiungiModello(modello: Modello) |
| + aggiungiSede(sede: SedeConsegna)  |
| + aggiungiUtente(utente: Utente)    |
| + generaPreventivo(auto: Auto,     |
|     utente: Utente): Preventivo     |
| + confermaPreventivo(preventivo:   |
|     Preventivo): void               |
| + registraPagamento(preventivo:    |
|     Preventivo, acconto: double):   |
|     void                            |
| + avvisaCliente(auto: Auto): void   |
+-------------------------------------+

+-------------------------------------+
|            ModelloAuto              |
+-------------------------------------+
| - marca: String                     |
| - nome: String                      |
| - descrizione: String               |
| - dimensioni: Dimensioni            |
| - motori: List<Motore>              |
| - immagini: List<Immagine>          |
| - optional: List<Optional>          |
| - sconti: List<Sconto>              |
+-------------------------------------+
| + calcolaPrezzoFinale(): double     |
+-------------------------------------+

+-------------------------------------+
|              Dimensioni             |
+-------------------------------------+
| - altezza: double                   |
| - lunghezza: double                 |
| - larghezza: double                 |
| - peso: double                      |
| - volumeBagagliaio: double          |
+-------------------------------------+

+-------------------------------------+
|               Motore                |
+-------------------------------------+
| - tipo: String                      |
| - alimentazione: String             |
+-------------------------------------+

+-------------------------------------+
|              Immagine               |
+-------------------------------------+
| - url: String                       |
+-------------------------------------+

+-------------------------------------+
|              Optional               |
+-------------------------------------+
| - nome: String                      |
| - prezzo: double                    |
| - applicabile: boolean              |
+-------------------------------------+

+-------------------------------------+
|               Sconto                |
+-------------------------------------+
| - mese: String                      |
| - importo: double                   |
+-------------------------------------+

+-------------------------------------+
|            SedeConsegna             |
+-------------------------------------+
| - nome: String                      |
| - indirizzo: String                 |
| - ordini: List<Ordine>              |
+-------------------------------------+

+-------------------------------------+
|               Utente                |
+-------------------------------------+
| - username: String                  |
| - password: String                  |
| - preventivi: List<Preventivo>      |
+-------------------------------------+

+-------------------------------------+
|             Auto                    |
+-------------------------------------+
| - modello: ModelloAuto              |
| - colore: String                    |
| - optionalScelti: List<Optional>    |
| - prezzo: double                    |
+-------------------------------------+

+-------------------------------------+
|             Preventivo               |
+-------------------------------------+
| - auto: Auto                        |
| - utente: Utente                    |
| - dataCreazione: Date               |
| - stato: String                     |
| - accontoPagato: boolean            |
+-------------------------------------+
# ProgettoConfiguratore
