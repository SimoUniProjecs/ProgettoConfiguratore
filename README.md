
# ProgettoConfiguratore

**ProgettoConfiguratore** è un'applicazione sviluppata come progetto per l'esame di **Ingegneria del Software**. L'obiettivo principale è la realizzazione di un **configuratore di prodotto** con una struttura modulare e facilmente estendibile, seguendo i principi dell'ingegneria del software e della programmazione orientata agli oggetti.

## Obiettivi del Progetto

- Progettare e implementare un configuratore di componenti.
- Applicare design pattern e tecniche di progettazione software.
- Sviluppare un'applicazione con interfaccia utente funzionale.
- Produrre documentazione tecnica conforme agli standard del corso.

## Tecnologie Utilizzate

- **Java**
- **JavaFX** per l'interfaccia grafica
- **JUnit** per il testing
- **MVC (Model-View-Controller)** come pattern architetturale

## Struttura del Progetto

Il progetto è organizzato nei seguenti package:

```
ProgettoConfiguratore/
│
├── src/
│   ├── controller/        # Logica di controllo (gestione eventi, interazioni)
│   ├── model/             # Classi che rappresentano i dati e logiche di business
│   ├── view/              # Interfaccia utente JavaFX
│   ├── exceptions/        # Eccezioni personalizzate
│   ├── utility/           # Classi di supporto e helper
│
├── test/                  # Test unitari con JUnit
├── docs/                  # Documentazione tecnica (UML, manuali, ecc.)
├── README.md              # Questo file
├── build.gradle / pom.xml # File di configurazione del build tool (Gradle o Maven)
└── .gitignore
```

## Requisiti

- Java 17+
- JavaFX SDK
- IDE consigliato: IntelliJ IDEA o Eclipse

## Istruzioni per l’Esecuzione

1. Clonare il repository:
    ```bash
    git clone https://github.com/SimoUniProjecs/ProgettoConfiguratore.git
    cd ProgettoConfiguratore
    ```

2. Importare il progetto nel proprio IDE.

3. Configurare JavaFX nel progetto se necessario.

4. Eseguire la classe `Main.java` nel package `view`.

## Esecuzione dei Test

Per eseguire i test con JUnit:

```bash
./gradlew test
```
_Oppure utilizzando l’IDE selezionando i file di test._

## Autori

- Simone Mattioli – Matricola vr486911 – Ruolo: Progettazione e sviluppo Con la collaborazione di David Cavada e Omar Bodio

## Licenza

Questo progetto è distribuito sotto licenza MIT. Vedi il file LICENSE per ulteriori dettagli.

## Note Finali

Il progetto è stato consegnato come parte dell'esame di **Ingegneria del Software** presso [Nome dell'università o dipartimento].

