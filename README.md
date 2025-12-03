# ⚽ FootBro-BE - Documentazione Ufficiale

Benvenuto nella documentazione ufficiale di **FootBro-BE**, il backend per la gestione completa di campionati e partite di calcio amatoriali. Questo progetto è sviluppato in Java utilizzando il framework Spring Boot ed è progettato per essere scalabile, sicuro e facilmente manutenibile.

## 📋 Indice
1. [Introduzione](#introduzione)
2. [Stack Tecnologico](#stack-tecnologico)
3. [Architettura del Progetto](#architettura-del-progetto)
4. [Funzionalità Principali](#funzionalità-principali)
   - [Autenticazione e Sicurezza](#autenticazione-e-sicurezza)
   - [Gestione Campionati](#gestione-campionati)
   - [Gestione Partite](#gestione-partite)
   - [Gestione Utenti](#gestione-utenti)
5. [Documentazione API](#documentazione-api)
6. [Guida all'Installazione e Avvio](#guida-allinstallazione-e-avvio)
7. [Deployment e CI/CD](#deployment-e-cicd)

---

## <a name="introduzione"></a>1. Introduzione

FootBro-BE è un'applicazione server-side che espone API RESTful per permettere agli utenti di organizzare, gestire e partecipare a campionati e partite di calcio. L'applicazione gestisce l'intero ciclo di vita di un evento sportivo, dalla creazione del campionato, alla programmazione delle partite, fino alla gestione delle squadre e alla registrazione dei risultati.

Il sistema è pensato per essere consumato da client frontend (Web o Mobile) ed implementa moderni standard di sicurezza.

---

## <a name="stack-tecnologico"></a>2. Stack Tecnologico

Il progetto si basa su tecnologie open source solide e performanti:

*   **Linguaggio:** Java 24 (OpenJDK)
*   **Framework Principale:** Spring Boot 3.5.4
*   **Database:** MySQL
*   **ORM:** Hibernate / Spring Data JPA
*   **Sicurezza:** Spring Security + JWT (JSON Web Token)
*   **Mapping Oggetti:** MapStruct
*   **Riduzione Boilerplate:** Lombok
*   **Documentazione API:** SpringDoc OpenAPI (Swagger UI)
*   **Build Tool:** Maven
*   **Containerizzazione:** Docker

---

## <a name="architettura-del-progetto"></a>3. Architettura del Progetto

Il progetto segue una classica architettura a livelli (Layered Architecture) per garantire la separazione delle responsabilità:

### Struttura delle Directory (`src/main/java/com/giggi`)

*   **`controller`**: Contiene le classi REST Controller che gestiscono le richieste HTTP, validano l'input e restituiscono le risposte al client. Sono il punto di ingresso dell'applicazione.
*   **`service`**: Contiene la logica di business. I service orchestrano le operazioni, comunicano con i repository e applicano le regole del dominio.
*   **`repository`**: Interfacce che estendono `JpaRepository`. Si occupano dell'interazione diretta con il database (CRUD operations).
*   **`entity`**: Classi POJO annotate con JPA che rappresentano le tabelle del database (es. `Utente`, `Partita`, `Campionato`).
*   **`dto` (Data Transfer Objects)**: Oggetti utilizzati per trasferire dati tra i layer e verso l'esterno, disaccoppiando le entità interne dalle API pubbliche.
*   **`mapper`**: Interfacce MapStruct per la conversione automatica tra Entity e DTO.
*   **`security`**: Configurazioni di Spring Security, filtri JWT e gestione dei ruoli.
*   **`exception`**: Gestione centralizzata delle eccezioni.

---

## <a name="funzionalità-principali"></a>4. Funzionalità Principali

### <a name="autenticazione-e-sicurezza"></a>🔐 Autenticazione e Sicurezza

Il sistema utilizza un'autenticazione stateless basata su **JWT**.
*   **Registrazione (`/api/auth/signup`)**: Permette a nuovi utenti di registrarsi fornendo username, email, password e nome. La password viene cifrata con **BCrypt** prima di essere salvata.
*   **Login (`/api/auth/signin`)**: L'utente invia le credenziali e riceve un token JWT.
*   **Validazione Token**: Ogni richiesta successiva deve includere il token nell'header `Authorization: Bearer <token>`. Un filtro (`AuthTokenFilter`) intercetta le richieste e valida il token.
*   **Ruoli**: Il sistema supporta ruoli (es. `USER`, `ADMIN`) per limitare l'accesso a determinati endpoint.

### <a name="gestione-campionati"></a>🏆 Gestione Campionati

Permette agli utenti di creare e partecipare a leghe.
*   **Creazione**: Un utente può creare un campionato definendo nome, descrizione e tipologia.
*   **Codice Univoco**: Ogni campionato ha un codice alfanumerico univoco generato automaticamente.
*   **Iscrizione (`join`)**: Gli utenti possono iscriversi a un campionato conoscendone il codice di invito.
*   **Classifiche**: Il sistema calcola e restituisce la classifica aggiornata per ogni campionato.

### <a name="gestione-partite"></a>⚽ Gestione Partite

Cuore dell'applicazione, gestisce i singoli eventi.
*   **Programmazione**: Le partite vengono create all'interno di un campionato, specificando data, ora e luogo.
*   **Stati Partita**: Una partita può essere `PROGRAMMATA`, in corso o terminata.
*   **Partecipazione**: Gli utenti iscritti al campionato possono segnarsi per una partita (`iscriviti`) o cancellarsi (`disiscriviti`).
*   **Gestione Squadre**: Supporto per la suddivisione dei giocatori in Squadra A e Squadra B.
*   **Terminazione**: Al termine della partita, è possibile salvare il risultato (gol squadra A vs B) che andrà ad aggiornare le statistiche.

### <a name="gestione-utenti"></a>👤 Gestione Utenti

*   **Profilo**: Gestione dei dati anagrafici.
*   **Statistiche Rapide**: Endpoint dedicato (`quickStats`) per visualizzare performance dell'utente (partite giocate, gol, ecc.).
*   **Ruoli Preferiti**: Gli utenti possono indicare i loro ruoli preferiti in campo (es. Portiere, Attaccante).

---

## <a name="documentazione-api"></a>5. Documentazione API

L'applicazione integra **Swagger UI** per l'esplorazione interattiva delle API.
Una volta avviata l'applicazione, la documentazione è disponibile all'indirizzo:

`http://localhost:8080/swagger-ui.html`

In questa interfaccia è possibile:
*   Visualizzare tutti gli endpoint disponibili.
*   Vedere i modelli dei dati (Schema) per richieste e risposte.
*   Testare le chiamate API direttamente dal browser (ricordati di autenticarti tramite il pulsante "Authorize" inserendo il token JWT).

---

## <a name="guida-allinstallazione-e-avvio"></a>6. Guida all'Installazione e Avvio

### Prerequisiti
*   Java 24 (o versione compatibile specificata nel `pom.xml`)
*   Maven
*   Docker (opzionale, per il database o per eseguire l'intera app)
*   MySQL Server (se non si usa Docker)

### Avvio Locale con Maven

1.  **Clona il repository:**
    ```bash
    git clone https://github.com/tuo-username/Footbro-BE.git
    cd Footbro-BE
    ```

2.  **Configura il Database:**
    Assicurati di avere un database MySQL in esecuzione. Configura le credenziali nel file `application.properties` (o tramite variabili d'ambiente).

3.  **Compila il progetto:**
    ```bash
    ./mvnw clean install
    ```

4.  **Avvia l'applicazione:**
    ```bash
    ./mvnw spring-boot:run
    ```
    L'applicazione sarà disponibile su `http://localhost:8080`.

### Avvio con Docker

Il progetto include un `Dockerfile` per la containerizzazione.

1.  **Build dell'immagine:**
    ```bash
    docker build -t footbro-be .
    ```

2.  **Esegui il container:**
    ```bash
    docker run -p 8080:8080 footbro-be
    ```

---

## <a name="deployment-e-cicd"></a>7. Deployment e CI/CD

Il progetto include una pipeline di **GitHub Actions** configurata nel file `.github/workflows/deploy.yml`.

**Flusso di Deploy:**
1.  Al `push` sul branch `main`, parte il workflow automatico.
2.  Il codice viene compilato e testato.
3.  Viene creata un'immagine Docker.
4.  L'immagine viene trasferita sul server VPS configurato.
5.  Il container viene aggiornato e riavviato automaticamente tramite comandi SSH.

*Nota: Il workflow richiede la configurazione dei secret di GitHub (`VPS_HOST`, `DEPLOY_KEY`, ecc.) per funzionare correttamente.*

---
*Documentazione generata automaticamente da Jules (AI Assistant).*
