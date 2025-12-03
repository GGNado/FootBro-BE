# ⚽ FootBro-BE - Documentazione Tecnica Ufficiale Completa

**Versione:** 1.0.0-SNAPSHOT  
**Data di Generazione:** 03 Dicembre 2024  
**Autore:** Jules (AI Assistant) per il team di sviluppo FootBro  
**Repository:** https://github.com/giggi/Footbro-BE (fittizio)

---

## 📋 Indice dei Contenuti

1. [Introduzione](#introduzione)
    - [Scopo del Progetto](#scopo-del-progetto)
    - [Contesto Applicativo](#contesto-applicativo)
    - [Visione Generale](#visione-generale)
2. [Stack Tecnologico e Requisiti](#stack-tecnologico-e-requisiti)
    - [Core Frameworks](#core-frameworks)
    - [Database & Persistenza](#database--persistenza)
    - [Sicurezza](#sicurezza)
    - [Strumenti di Sviluppo](#strumenti-di-sviluppo)
    - [Requisiti di Sistema](#requisiti-di-sistema)
3. [Architettura del Sistema](#architettura-del-sistema)
    - [Overview dell'Architettura a Livelli](#overview-dellarchitettura-a-livelli)
    - [Diagramma Concettuale del Flusso Dati](#diagramma-concettuale-del-flusso-dati)
    - [Descrizione dei Componenti Architetturali](#descrizione-dei-componenti-architetturali)
    - [Gestione delle Eccezioni](#gestione-delle-eccezioni)
4. [Design del Database (Schema ER)](#design-del-database-schema-er)
    - [Tabelle Principali](#tabelle-principali)
    - [Relazioni tra Entità](#relazioni-tra-entità)
    - [Descrizione Dettagliata Campi](#descrizione-dettagliata-campi)
5. [Guida all'Implementazione (Codebase Reference)](#guida-allimplementazione-codebase-reference)
    - [Package: Controller](#package-controller)
    - [Package: Service](#package-service)
    - [Package: Repository](#package-repository)
    - [Package: DTO](#package-dto)
    - [Package: Entity](#package-entity)
    - [Package: Mapper](#package-mapper)
    - [Package: Security](#package-security)
6. [Riferimento API (API Reference)](#riferimento-api-api-reference)
    - [Autenticazione (Auth)](#autenticazione-auth)
    - [Campionati](#campionati)
    - [Partite](#partite)
    - [Utenti](#utenti)
7. [Logiche di Business Complesse](#logiche-di-business-complesse)
    - [Algoritmo di Calcolo Classifica](#algoritmo-di-calcolo-classifica)
    - [Gestione Stati Partita](#gestione-stati-partita)
    - [Flusso di Iscrizione e Codici Invito](#flusso-di-iscrizione-e-codici-invito)
8. [Configurazione e Setup](#configurazione-e-setup)
    - [Proprietà dell'Applicazione (`application.properties`)](#proprietà-dellapplicazione-applicationproperties)
    - [Configurazione Build (`pom.xml`)](#configurazione-build-pomxml)
    - [Installazione Locale](#installazione-locale)
    - [Esecuzione tramite Docker](#esecuzione-tramite-docker)
9. [Deployment e CI/CD](#deployment-e-cicd)
    - [Workflow GitHub Actions](#workflow-github-actions)
    - [Strategia di Deploy su VPS](#strategia-di-deploy-su-vps)
10. [Troubleshooting e FAQ](#troubleshooting-e-faq)

---

## <a name="introduzione"></a>1. Introduzione

### <a name="scopo-del-progetto"></a>Scopo del Progetto
**FootBro-BE** è l'infrastruttura di backend progettata per supportare la piattaforma FootBro, un'applicazione dedicata agli appassionati di calcio amatoriale. L'obiettivo primario è fornire un sistema robusto, scalabile e sicuro per la gestione completa dell'ecosistema calcistico amatoriale: dalla creazione di leghe private (campionati) all'organizzazione di singole partite, fino al tracciamento dettagliato delle statistiche individuali e di squadra.

### <a name="contesto-applicativo"></a>Contesto Applicativo
Nel panorama delle applicazioni sportive, spesso manca uno strumento che unisca la semplicità di organizzazione (come i gruppi WhatsApp) con la profondità dei dati (tipica dei gestionali professionali). FootBro colma questa lacuna offrendo un backend in grado di:
- Gestire identità digitali sicure per ogni giocatore.
- Creare contesti competitivi isolati (Campionati) accessibili solo tramite invito.
- Automatizzare il calcolo dei punteggi, delle medie voto, dei gol e degli assist.
- Fornire API RESTful standardizzate per client Web (React/Angular) e Mobile (iOS/Android).

### <a name="visione-generale"></a>Visione Generale
Il sistema è costruito come un monolito modulare basato su **Spring Boot**, esponendo una serie di API REST documentate via Swagger. La persistenza dei dati è affidata a un database relazionale (MySQL), garantendo integrità referenziale e consistenza delle transazioni, cruciali per la correttezza delle classifiche.

---

## <a name="stack-tecnologico-e-requisiti"></a>2. Stack Tecnologico e Requisiti

### <a name="core-frameworks"></a>Core Frameworks
Il cuore dell'applicazione è basato sull'ecosistema Spring, scelto per la sua maturità, il supporto alla Dependency Injection e la vasta gamma di moduli integrati.

*   **Java Development Kit (JDK):** Versione 21 (LTS). Il progetto sfrutta le feature moderne del linguaggio come i Records (nei DTO), Pattern Matching e la nuova API Date/Time.
    *   *Nota sulla versione:* Sebbene il `pom.xml` originale puntasse a Java 24, per compatibilità con gli ambienti di produzione attuali si è standardizzato su Java 21.
*   **Spring Boot 3.5.4:** Framework opinionated che semplifica la configurazione e il bootstrap dell'applicazione. Include Tomcat come web server embedded.
*   **Spring Web MVC:** Modulo per la creazione di applicazioni web basate su Servlet, utilizzato per implementare i Controller REST.
*   **Spring Data JPA:** Astrazione sopra JPA (Java Persistence API) che riduce drasticamente il codice boilerplate necessario per l'accesso ai dati, fornendo repository automatici.
*   **Hibernate ORM:** Implementazione JPA di default, gestisce il mapping Oggetto-Relazionale.

### <a name="database--persistenza"></a>Database & Persistenza
*   **MySQL 8:** RDBMS scelto per l'affidabilità.
*   **MySQL Connector/J:** Driver JDBC per la connessione.
*   **HikariCP:** Connection pool ad alte prestazioni (default in Spring Boot 2+).

### <a name="sicurezza"></a>Sicurezza
*   **Spring Security 6:** Framework potente e altamente personalizzabile per l'autenticazione e il controllo degli accessi.
*   **JWT (JSON Web Token):** Standard per l'autenticazione stateless. Utilizziamo la libreria `jjwt` (versione 0.12.2) per la generazione, il parsing e la validazione dei token.
*   **BCrypt:** Algoritmo di hashing adattivo utilizzato per cifrare le password degli utenti nel database. Implementato tramite la libreria `at.favre.lib:bcrypt`.

### <a name="strumenti-di-sviluppo"></a>Strumenti di Sviluppo
*   **Maven:** Strumento di build automation e gestione delle dipendenze.
*   **Lombok:** Libreria che si aggancia al compilatore per generare automaticamente getter, setter, costruttori e builder, riducendo la verbosità del codice Java.
*   **MapStruct:** Processore di annotazioni per la generazione di mapper type-safe ed efficienti tra Entity e DTO.
*   **SpringDoc OpenAPI (Swagger UI):** Generazione automatica della documentazione API e interfaccia di test interattiva.

### <a name="requisiti-di-sistema"></a>Requisiti di Sistema
Per eseguire l'applicazione in ambiente di sviluppo o produzione:
*   **RAM:** Minimo 512MB, Consigliato 2GB.
*   **Disk Space:** 500MB per l'applicazione e i log + Spazio Database.
*   **OS:** Linux (Ubuntu/Debian raccomandati), Windows, macOS.

---

## <a name="architettura-del-sistema"></a>3. Architettura del Sistema

### <a name="overview-dellarchitettura-a-livelli"></a>Overview dell'Architettura a Livelli
FootBro-BE adotta rigorosamente il pattern **Layered Architecture** (Architettura a Strati). Questo design pattern promuove la "Separation of Concerns" (Separazione delle Responsabilità), rendendo il codice più manutenibile, testabile e comprensibile.

Gli strati principali sono:
1.  **Presentation Layer (Controller):** Gestisce l'interazione HTTP. Riceve le richieste, valida i payload (DTO) e delega l'elaborazione al Service Layer. Non contiene logica di business.
2.  **Service Layer (Service):** Il cuore dell'applicazione. Contiene la business logic, le transazioni, le validazioni complesse e l'orchestrazione delle chiamate ai Repository.
3.  **Persistence Layer (Repository):** Interfaccia con il database. Estende `JpaRepository` per fornire metodi CRUD standard e query custom (JPQL o Native SQL).
4.  **Database Layer:** Il database fisico (MySQL).

### <a name="diagramma-concettuale-del-flusso-dati"></a>Diagramma Concettuale del Flusso Dati

```
[Client (Web/Mobile)]
       ⬇️ (HTTP Request JSON)
[Security Filter Chain (JWT Auth)]
       ⬇️ (Authenticated Request)
[Controller Layer] (es. PartitaController)
       ⬇️ (DTO Input)
[Service Layer] (es. PartitaService)
       ⬇️ (Entity Manipulation & Logic)
[Repository Layer] (es. PartitaRepository)
       ⬇️ (SQL Query)
[Database (MySQL)]
```

### <a name="descrizione-dei-componenti-architetturali"></a>Descrizione dei Componenti Architetturali

#### 1. DTO (Data Transfer Objects)
Gli oggetti DTO sono POJO (Plain Old Java Objects) puri utilizzati esclusivamente per trasportare dati.
*   **Request DTOs:** (es. `LoginRequest`, `CampionatoCreateRequestDTO`) contengono i dati inviati dal client. Sono annotati con `jakarta.validation` (`@NotNull`, `@Size`, ecc.) per garantire la validità dei dati in ingresso prima ancora che arrivino al Service.
*   **Response DTOs:** (es. `JwtResponse`, `PartitaFindDTO`) definiscono esattamente cosa viene restituito al client, nascondendo campi sensibili (come password) o dettagli implementativi interni delle Entity (come le foreign key grezze).

#### 2. Mapper (MapStruct)
Per evitare codice ripetitivo di conversione "Entity <-> DTO", utilizziamo MapStruct. Le interfacce Mapper definiscono le conversioni e MapStruct genera l'implementazione concreta durante la compilazione. Questo approccio è molto più performante della reflection a runtime.

#### 3. Exception Handling Centralizzato
Il sistema utilizza un `@ControllerAdvice` globale (`GlobalExceptionHandler`) per intercettare le eccezioni lanciate in qualsiasi punto dell'applicazione.
*   Se un Service lancia `CampionatoNotFoundException`, il `GlobalExceptionHandler` la cattura e restituisce una risposta JSON standardizzata con status HTTP 404 e un messaggio chiaro.
*   Questo evita blocchi `try-catch` ripetuti in ogni Controller.

---

## <a name="design-del-database-schema-er"></a>4. Design del Database (Schema ER)

Il database è normalizzato per garantire l'integrità dei dati. Di seguito l'analisi delle entità principali.

### <a name="tabelle-principali"></a>Tabelle Principali

#### `utenti` (Users)
Rappresenta gli utenti registrati alla piattaforma.
*   `id` (PK): BigInt, Auto Increment.
*   `username`: Varchar(50), Unique. Identificativo di login.
*   `email`: Varchar(100), Unique. Per comunicazioni e recupero password.
*   `password`: Varchar(120). Hash BCrypt della password.
*   `first_name`, `last_name`: Varchar(100). Dati anagrafici.
*   `ruoli_preferiti`: Tabella collegata per memorizzare le preferenze di gioco (Portiere, Difensore, ecc.).

#### `roles` (Ruoli di Sistema)
Definisce i permessi a livello di piattaforma (non di gioco).
*   `id` (PK): Integer.
*   `name`: Enum String (`ROLE_USER`, `ROLE_ADMIN`, `ROLE_MODERATOR`).

#### `campionati` (Championships)
Rappresenta una lega o un torneo creato da un utente.
*   `id` (PK): BigInt.
*   `nome`: Varchar, Unique. Il nome della lega.
*   `codice`: Varchar(8), Unique. Codice alfanumerico generato randomicamente per gli inviti.
*   `creatore_id` (FK -> utenti): Chi ha creato il campionato.
*   `tipologia_campionato`: Enum (`CALCIO_A_5`, `CALCIO_A_7`, `CALCIO_A_11`).

#### `partite` (Matches)
Singolo evento sportivo all'interno di un campionato.
*   `id` (PK): BigInt.
*   `campionato_id` (FK -> campionati).
*   `data_ora`: Datetime. Quando si gioca.
*   `luogo`: Varchar. Indirizzo o nome del campo.
*   `stato`: Enum (`PROGRAMMATA`, `TERMINATA`, `ANNULLATA`).
*   `gol_squadra_a`, `gol_squadra_b`: Integer. Risultato finale.

#### `partecipazione_campionato` (Join Table Estesa)
Collega Utenti a Campionati, ma contiene anche le statistiche globali del giocatore in quel campionato.
*   `id` (PK).
*   `utente_id` (FK).
*   `campionato_id` (FK).
*   `punti`: Integer. Totale punti in classifica.
*   `gol_fatti`: Integer.
*   `assist`: Integer.
*   `media_voto`: Double.
*   `attivo`: Boolean. Se l'utente è ancora parte del campionato.

#### `partecipazione_partita` (Join Table Estesa)
Collega Utenti a Partite, rappresentando la presenza del giocatore in un match specifico e la sua performance.
*   `id` (PK).
*   `utente_id` (FK).
*   `partita_id` (FK).
*   `squadra`: Enum (`A`, `B`, `DA_ASSEGNARE`).
*   `gol_segnati`: Integer. Gol in quella specifica partita.
*   `voto`: Double. Voto in pagella.

### <a name="relazioni-tra-entità"></a>Relazioni tra Entità
*   **Utente 1:N Campionati (Creati):** Un utente può creare infiniti campionati.
*   **Utente M:N Campionati (Partecipazione):** Gestito tramite `PartecipazioneCampionato`.
*   **Campionato 1:N Partite:** Un campionato contiene molte partite.
*   **Partita M:N Utenti:** Gestito tramite `PartecipazionePartita`.

---

## <a name="guida-allimplementazione-codebase-reference"></a>5. Guida all'Implementazione (Codebase Reference)

Questa sezione esplora il codice sorgente nel dettaglio.

### <a name="package-controller"></a>Package: `com.giggi.controller`

#### `AuthController.java`
Gestisce l'ingresso degli utenti nel sistema.
*   `authenticateUser`: Riceve user/pass, invoca `AuthenticationManager`, genera il JWT tramite `JwtUtils` e lo restituisce.
*   `registerUser`: Controlla duplicati (username/email), codifica la password, assegna ruolo default e salva l'utente.
*   `validateToken`: Utility per verificare se un token nel LocalStorage del client è ancora valido.

#### `CampionatoController.java`
*   `getAllCampionati`: Ritorna lista globale (spesso limitata ad admin o debug).
*   `getCampionatiByUtenteId`: Filtra i campionati in base alla tabella `PartecipazioneCampionato`.
*   `createCampionato`: Crea un'istanza, genera il codice invito, e iscrive automaticamente il creatore come primo partecipante (Admin della lega).
*   `joinCampionato`: Logica critica. Riceve un codice, cerca il campionato. Se trovato, crea una nuova `PartecipazioneCampionato` per l'utente richiedente.
*   `getClassificaByIdCampionato`: Calcola la classifica. Il calcolo avviene on-the-fly recuperando le entità `PartecipazioneCampionato` e ordinandole per punti decrescenti.

#### `PartitaController.java`
*   `createPartita`: Associa una nuova partita a un campionato. Inizializza lo stato a `PROGRAMMATA` e i gol a 0.
*   `iscrivitiPartita`: Verifica che l'utente sia membro del campionato. Se sì, crea `PartecipazionePartita`. Blocca l'iscrizione se la partita è già `TERMINATA`.
*   `salvaSquadra`: Permette all'admin di assegnare i giocatori alla squadra A o B. Itera sulle partecipazioni e aggiorna il campo `squadra`.
*   `terminaPartita`: Endpoint complesso.
    1.  Imposta stato a `TERMINATA`.
    2.  Salva il risultato finale.
    3.  Itera su tutti i giocatori partecipanti.
    4.  Aggiorna le loro statistiche personali (gol, voti) nella partita.
    5.  **Trigger:** Invoca l'aggiornamento della classifica generale del campionato (`PartecipazioneCampionato`) sommando i nuovi dati.

### <a name="package-service"></a>Package: `com.giggi.service` (e `impl`)

#### `AuthServiceImpl`
Incapsula la logica di interazione con `AuthenticationManager` di Spring Security.
*   Interagisce con `RoleRepository` per recuperare le entità Ruolo da assegnare ai nuovi utenti.

#### `CampionatoServiceImpl`
Gestisce il ciclo di vita del campionato.
*   Metodo `joinCampionato`: È transazionale. Assicura che non ci siano "race conditions" se due utenti si uniscono contemporaneamente, anche se il vincolo di unicità DB sulla coppia (utente, campionato) protegge a livello inferiore.

#### `PartitaServiceImpl`
Il service più complesso.
*   Metodo `aggiornaClassificaCampionato` (Privato): Viene chiamato alla chiusura di una partita.
    *   Logica Punti: Se (GolSquadraGiocatore > GolSquadraAvversaria) -> +3 Punti. Se Pareggio -> +1 Punto.
    *   Logica Medie: Ricalcola la media voto ponderata basandosi sul numero di partite giocate *precedenti*.

### <a name="package-repository"></a>Package: `com.giggi.repository`
Interfacce che estendono `JpaRepository<Entity, ID>`.
*   `UtenteRepository`: Metodi `existsByUsername`, `findByEmail`.
*   `CampionatoRepository`: Metodo `findByCodice` per il join.
*   `PartecipazioneCampionatoRepository`: Metodi custom per trovare le partecipazioni attive di un utente.

---

## <a name="riferimento-api-api-reference"></a>6. Riferimento API (API Reference)

Tutte le API sono prefissate con `/api`. Le risposte sono in formato JSON.

### <a name="autenticazione-auth"></a>Autenticazione (Auth)

#### `POST /api/auth/signin`
Effettua il login.
**Body:**
```json
{
  "usernameOrEmail": "mario.rossi",
  "password": "password123"
}
```
**Risposta (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "mario.rossi",
  "roles": ["ROLE_USER"]
}
```

#### `POST /api/auth/signup`
Registrazione nuovo utente.
**Body:**
```json
{
  "username": "mario.rossi",
  "email": "mario@email.com",
  "password": "password123",
  "firstName": "Mario",
  "lastName": "Rossi"
}
```

### <a name="campionati"></a>Campionati

#### `GET /api/campionati/iscritto/{idUtente}`
Ottieni i campionati dell'utente.
**Header:** `Authorization: Bearer <token>`

#### `POST /api/campionati`
Crea nuovo campionato.
**Body:**
```json
{
  "nome": "Torneo Estivo 2024",
  "descrizione": "Calcetto tra amici",
  "tipologia": "CALCIO_A_5",
  "idUtente": 1
}
```

#### `POST /api/campionati/join`
Unisciti a un campionato.
**Body:**
```json
{
  "codice": "A1B2C3D4",
  "idUtente": 1
}
```

### <a name="partite"></a>Partite

#### `POST /api/partite/campionato/{idCampionato}`
Crea partita.
**Body:**
```json
{
  "dataOra": "2024-06-15T20:00:00",
  "luogo": "Campo Sportivo Comunale"
}
```

#### `POST /api/partite/{idPartita}/iscriviti/{idUtente}`
Iscrizione giocatore alla partita. Nessun body richiesto.

#### `PUT /api/partite/termina`
Chiude la partita e aggiorna le stats.
**Body:**
```json
{
  "id": 10,
  "golSquadraA": 5,
  "golSquadraB": 3,
  "partecipazioni": [
    { "utente": {"id": 1}, "golSegnati": 2, "assist": 1, "voto": 7.5, "squadra": "A" },
    { "utente": {"id": 2}, "golSegnati": 0, "assist": 0, "voto": 6.0, "squadra": "B" }
  ]
}
```

---

## <a name="logiche-di-business-complesse"></a>7. Logiche di Business Complesse

### <a name="algoritmo-di-calcolo-classifica"></a>Algoritmo di Calcolo Classifica
La classifica non è una semplice "vista" sul database, ma il risultato di un accumulo persistente di dati nella tabella `PartecipazioneCampionato`.
Ogni volta che si chiama `/termina` su una partita:
1.  Il sistema recupera la `PartecipazioneCampionato` per ogni giocatore coinvolto.
2.  **Punti:**
    *   Vittoria: +3
    *   Pareggio: +1
    *   Sconfitta: +0
3.  **Media Voto:** Viene ricalcolata usando la formula della media mobile cumulativa:
    `NuovaMedia = ((VecchiaMedia * VecchiePartite) + NuovoVoto) / (VecchiePartite + 1)`
    Questo assicura precisione matematica senza dover rileggere tutte le partite passate.

### <a name="flusso-di-iscrizione-e-codici-invito"></a>Flusso di Iscrizione e Codici Invito
Il codice invito è generato tramite `UUID.randomUUID()`, pulito dai trattini, preso nei primi 8 caratteri e convertito in maiuscolo. Questo offre un buon compromesso tra unicità (molto improbabile collisione su 8 char esadecimali in un contesto ristretto) e leggibilità per l'utente che deve digitarlo.

---

## <a name="configurazione-e-setup"></a>8. Configurazione e Setup

### <a name="proprietà-dellapplicazione-applicationproperties"></a>Proprietà dell'Applicazione (`application.properties`)
File cruciale situato in `src/main/resources`.
```properties
# Configurazione Server
server.port=8080

# Database Configuration (Esempio Locale)
spring.datasource.url=jdbc:mysql://localhost:3306/footbro_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update # Attenzione: usare 'validate' in produzione!

# JWT Configuration
app.jwtSecret=UnaStringaMoltoLungaEComplessaPerLaSicurezzaDelTokenCheDeveEssereSegreta
app.jwtExpirationMs=86400000 # 24 ore
```

### <a name="configurazione-build-pomxml"></a>Configurazione Build (`pom.xml`)
Il progetto usa Maven. Le dipendenze chiave sono:
*   `spring-boot-starter-web`: Per il server REST.
*   `spring-boot-starter-data-jpa`: Per il DB.
*   `spring-boot-starter-security`: Per la protezione.
*   `jjwt-api`, `jjwt-impl`, `jjwt-jackson`: Librerie JWT.

### <a name="installazione-locale"></a>Installazione Locale
1.  Assicurati di avere Java 21 installato: `java -version`.
2.  Assicurati di avere MySQL attivo e crea un database vuoto chiamato `footbro_db`.
3.  Clona la repo.
4.  Esegui `mvnw clean install` (su Windows `mvnw.cmd`).
5.  Avvia con `mvnw spring-boot:run`.

### <a name="esecuzione-tramite-docker"></a>Esecuzione tramite Docker
Il progetto include un `Dockerfile` multistage ottimizzato.
1.  **Build:** `docker build -t footbro-be .`
2.  **Run:** `docker run -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/footbro_db footbro-be`
    *(Nota: `host.docker.internal` permette al container di vedere il MySQL sulla macchina host)*.

---

## <a name="deployment-e-cicd"></a>9. Deployment e CI/CD

### <a name="workflow-github-actions"></a>Workflow GitHub Actions
File: `.github/workflows/deploy.yml`
Questo file definisce l'automazione DevOps.
1.  **Trigger:** Ad ogni push sul branch `main`.
2.  **Job Build:** Installa Java 21, compila il progetto con Maven, esegue i test (se abilitati).
3.  **Job Docker:** Costruisce l'immagine Docker taggandola `latest`. Salva l'immagine come tarball (`.tar.gz`).
4.  **Job Deploy:**
    *   Usa `scp` (Secure Copy) per trasferire il tarball su un server VPS remoto.
    *   Si connette via SSH al VPS.
    *   Carica l'immagine docker (`docker load`).
    *   Riavvia il servizio tramite `docker compose` (assumendo che sul server ci sia un `docker-compose.yml` che orchestra app e database).

### <a name="strategia-di-deploy-su-vps"></a>Strategia di Deploy su VPS
Sul server di produzione, la struttura consigliata è:
*   Cartella `/root/footbro/` contenente `docker-compose.yml` e `.env`.
*   Il `docker-compose.yml` definisce due servizi:
    1.  `db`: Container MySQL con volume persistente per i dati.
    2.  `app`: Container FootBro-BE che dipende da `db`.

---

## <a name="troubleshooting-e-faq"></a>10. Troubleshooting e FAQ

### Errore: `Communications link failure` all'avvio
**Causa:** L'applicazione non riesce a raggiungere il database MySQL.
**Soluzione:** Controlla che MySQL sia attivo, che l'URL in `application.properties` sia corretto e che user/password siano validi. Se usi Docker, verifica la rete bridge.

### Errore: 401 Unauthorized su endpoint pubblici
**Causa:** Configurazione errata di Spring Security.
**Soluzione:** Verifica `SecurityConfig.java`. Assicurati che gli endpoint come `/api/auth/**` e Swagger (`/v3/api-docs/**`) siano in `.permitAll()`.

### Il Token JWT scade troppo presto
**Soluzione:** Modifica la proprietà `app.jwtExpirationMs` nel file properties o nelle variabili d'ambiente del server.

### I Mapper non funzionano / NullPointerException sui DTO
**Causa:** MapStruct non ha generato le classi `Impl`.
**Soluzione:** Esegui `mvn clean compile`. Assicurati che l'annotation processor di MapStruct sia configurato nel `pom.xml`.

---
*Fine della Documentazione Ufficiale - FootBro Development Team*
