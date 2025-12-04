# ⚽ FootBro-BE - Documentazione Tecnica Ufficiale Completa

**Versione:** 1.0.0-SNAPSHOT
**Data di Generazione:** 03 Dicembre 2024
**Autore:** Jules (AI Assistant) per il team di sviluppo FootBro
**Repository:** https://github.com/giggi/Footbro-BE (fittizio)

---

## 📋 Indice dei Contenuti

1. [Introduzione](#introduzione)
2. [Stack Tecnologico e Requisiti](#stack-tecnologico-e-requisiti)
3. [Architettura del Sistema](#architettura-del-sistema)
4. [Design del Database (Schema ER)](#design-del-database-schema-er)
5. [Sicurezza e Autenticazione (Deep Dive)](#sicurezza-e-autenticazione-deep-dive)
    - [Panoramica](#panoramica)
    - [Flusso JWT](#flusso-jwt)
    - [Filtri di Sicurezza](#filtri-di-sicurezza)
    - [Configurazione Password](#configurazione-password)
    - [CORS](#cors)
6. [Guida all'Implementazione (Codebase Reference)](#guida-allimplementazione-codebase-reference)
7. [Riferimento API (API Reference)](#riferimento-api-api-reference)
    - [Autenticazione (Auth)](#api-autenticazione)
    - [Campionati](#api-campionati)
    - [Partite](#api-partite)
    - [Utenti](#api-utenti)
8. [Logiche di Business Complesse](#logiche-di-business-complesse)
9. [Configurazione e Setup](#configurazione-e-setup)
10. [Deployment e CI/CD](#deployment-e-cicd)
11. [Troubleshooting e FAQ](#troubleshooting-e-faq)

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
*   **Spring Boot 3.5.4:** Framework opinionated che semplifica la configurazione e il bootstrap dell'applicazione. Include Tomcat come web server embedded.
*   **Spring Web MVC:** Modulo per la creazione di applicazioni web basate su Servlet, utilizzato per implementare i Controller REST.
*   **Spring Data JPA:** Astrazione sopra JPA (Java Persistence API) che riduce drasticamente il codice boilerplate necessario per l'accesso ai dati, fornendo repository automatici.
*   **Hibernate ORM:** Implementazione JPA di default, gestisce il mapping Oggetto-Relazionale.

### <a name="database--persistenza"></a>Database & Persistenza
*   **MySQL 8:** RDBMS scelto per l'affidabilità.
*   **MySQL Connector/J:** Driver JDBC per la connessione.
*   **HikariCP:** Connection pool ad alte prestazioni (default in Spring Boot 2+).

### <a name="strumenti-di-sviluppo"></a>Strumenti di Sviluppo
*   **Maven:** Strumento di build automation e gestione delle dipendenze.
*   **Lombok:** Libreria che si aggancia al compilatore per generare automaticamente getter, setter, costruttori e builder, riducendo la verbosità del codice Java.
*   **MapStruct:** Processore di annotazioni per la generazione di mapper type-safe ed efficienti tra Entity e DTO.
*   **SpringDoc OpenAPI (Swagger UI):** Generazione automatica della documentazione API e interfaccia di test interattiva.

---

## <a name="architettura-del-sistema"></a>3. Architettura del Sistema

### <a name="overview-dellarchitettura-a-livelli"></a>Overview dell'Architettura a Livelli
FootBro-BE adotta rigorosamente il pattern **Layered Architecture** (Architettura a Strati). Questo design pattern promuove la "Separation of Concerns" (Separazione delle Responsabilità), rendendo il codice più manutenibile, testabile e comprensibile.

Gli strati principali sono:
1.  **Presentation Layer (Controller):** Gestisce l'interazione HTTP. Riceve le richieste, valida i payload (DTO) e delega l'elaborazione al Service Layer. Non contiene logica di business.
2.  **Service Layer (Service):** Il cuore dell'applicazione. Contiene la business logic, le transazioni, le validazioni complesse e l'orchestrazione delle chiamate ai Repository.
3.  **Persistence Layer (Repository):** Interfaccia con il database. Estende `JpaRepository` per fornire metodi CRUD standard e query custom (JPQL o Native SQL).
4.  **Database Layer:** Il database fisico (MySQL).

---

## <a name="design-del-database-schema-er"></a>4. Design del Database (Schema ER)

Il database è normalizzato per garantire l'integrità dei dati. Di seguito l'analisi delle entità principali.

### <a name="tabelle-principali"></a>Tabelle Principali

#### `utenti` (Users)
Rappresenta gli utenti registrati alla piattaforma.
*   `id` (PK): BigInt, Auto Increment.
*   `username`: Varchar(50), Unique.
*   `email`: Varchar(100), Unique.
*   `password`: Varchar(120). Hash BCrypt.
*   `first_name`, `last_name`: Varchar(100).
*   `ruoli_preferiti`: Tabella collegata per memorizzare le preferenze di gioco (Portiere, Difensore, ecc.).

#### `roles` (Ruoli di Sistema)
Definisce i permessi a livello di piattaforma (non di gioco).
*   `id` (PK): Integer.
*   `name`: Enum String (`ROLE_USER`, `ROLE_ADMIN`, `ROLE_MODERATOR`).

#### `campionati` (Championships)
Rappresenta una lega o un torneo creato da un utente.
*   `id` (PK): BigInt.
*   `nome`: Varchar, Unique.
*   `codice`: Varchar(8), Unique. Codice invito.
*   `creatore_id` (FK -> utenti).
*   `tipologia_campionato`: Enum (`CALCIO_A_5`, `CALCIO_A_7`, `CALCIO_A_11`).

#### `partite` (Matches)
Singolo evento sportivo all'interno di un campionato.
*   `id` (PK): BigInt.
*   `campionato_id` (FK -> campionati).
*   `data_ora`: Datetime.
*   `luogo`: Varchar.
*   `stato`: Enum (`PROGRAMMATA`, `TERMINATA`, `ANNULLATA`).
*   `gol_squadra_a`, `gol_squadra_b`: Integer.

#### `partecipazione_campionato` (Join Table Estesa)
Collega Utenti a Campionati e contiene stats globali.
*   `punti`: Integer.
*   `gol_fatti`: Integer.
*   `media_voto`: Double.
*   `attivo`: Boolean.

#### `partecipazione_partita` (Join Table Estesa)
Collega Utenti a Partite e contiene stats del singolo match.
*   `squadra`: Enum (`A`, `B`, `DA_ASSEGNARE`).
*   `gol_segnati`: Integer.
*   `voto`: Double.

---

## <a name="sicurezza-e-autenticazione-deep-dive"></a>5. Sicurezza e Autenticazione (Deep Dive)

La sicurezza è un componente critico di FootBro-BE. Il sistema utilizza un approccio **stateless** basato su standard industriali per garantire che ogni richiesta sia autenticata e autorizzata correttamente.

### <a name="panoramica"></a>Panoramica
L'implementazione si trova nel package `com.giggi.security` e utilizza **Spring Security 6**.
- **Autenticazione:** Verifica *chi* è l'utente (Login).
- **Autorizzazione:** Verifica *cosa* può fare l'utente (Ruoli).

### <a name="flusso-jwt"></a>Flusso JWT (JSON Web Token)
Il cuore dell'autenticazione è il token JWT. Ecco come funziona il ciclo di vita:

1.  **Generazione (`JwtUtils.generateJwtToken`)**:
    Quando un utente effettua il login con successo, il server genera un token firmato digitalmente usando l'algoritmo **HMAC SHA-256** (HS256).
    Il token contiene un **Payload** (Claims) con:
    - `sub` (Subject): Lo username.
    - `id`: L'ID utente nel database.
    - `email`, `firstName`, `lastName`: Dati anagrafici per evitare query ridondanti lato frontend.
    - `authorities`: La lista dei ruoli (es. `ROLE_USER`).
    - `iat` (Issued At): Data di creazione.
    - `exp` (Expiration): Data di scadenza (configurata a 24 ore).

2.  **Validazione (`JwtUtils.validateJwtToken`)**:
    Ad ogni richiesta successiva, il token viene analizzato. La firma viene ricalcolata usando il segreto (`app.jwtSecret`). Se la firma non corrisponde (token manomesso) o se il token è scaduto (`ExpiredJwtException`), la richiesta viene rifiutata.

### <a name="filtri-di-sicurezza"></a>Filtri di Sicurezza
La protezione delle API avviene tramite una catena di filtri (`SecurityFilterChain`).
Il componente chiave è `AuthTokenFilter` (estende `OncePerRequestFilter`), che intercetta ogni richiesta HTTP **prima** che raggiunga i Controller.

**Logica del Filtro:**
1.  Estrae l'header HTTP `Authorization`.
2.  Controlla se inizia con `Bearer `.
3.  Estrae la stringa del token.
4.  Chiama `jwtUtils.validateJwtToken(token)`.
5.  Se valido, estrae lo username e i ruoli.
6.  Costruisce un oggetto `UsernamePasswordAuthenticationToken`.
7.  Lo inserisce nel `SecurityContextHolder` di Spring.
    *Da questo momento, Spring considera l'utente autenticato per il thread corrente.*

### <a name="configurazione-password"></a>Configurazione Password
Le password non sono **mai** salvate in chiaro.
Viene utilizzato il bean `PasswordEncoder` implementato con **BCrypt**.
- **Registrazione:** `passwordEncoder.encode("password123")` -> Hash nel DB.
- **Login:** `passwordEncoder.matches("password123", hashDalDb)` -> Verifica.

### <a name="cors"></a>CORS (Cross-Origin Resource Sharing)
Per permettere al frontend (che potrebbe girare su `localhost:3000` o `localhost:4200`) di chiamare il backend (`localhost:8080`), è configurato un bean `CorsConfigurationSource` che abilita esplicitamente le origini fidate e i metodi HTTP (GET, POST, PUT, DELETE, OPTIONS).

---

## <a name="guida-allimplementazione-codebase-reference"></a>6. Guida all'Implementazione (Codebase Reference)

(Vedi sezione 5 della versione precedente per dettagli su Service e Repository).

---

## <a name="riferimento-api-api-reference"></a>7. Riferimento API (API Reference)

Di seguito la documentazione esaustiva di tutti gli endpoint disponibili.

### <a name="api-autenticazione"></a>🔐 Autenticazione (Auth)

Base URL: `/api/auth`

#### 1. Registrazione Utente
Crea un nuovo account nel sistema.
*   **Method:** `POST`
*   **URL:** `/signup`
*   **Request Body (JSON):**
    ```json
    {
      "username": "mario.rossi",      // Obbligatorio, min 3 char
      "email": "mario@test.com",      // Obbligatorio, formato email valido
      "password": "Password123!",     // Obbligatorio, min 6 char
      "firstName": "Mario",           // Obbligatorio
      "lastName": "Rossi",            // Obbligatorio
      "roles": ["user"],              // Opzionale, default "user"
      "ruoliPreferiti": ["PORTIERE"]  // Opzionale. Enum: PORTIERE, DIFENSORE, CENTROCAMPISTA, ATTACCANTE
    }
    ```
*   **Response Body (JSON - 200 OK):**
    ```json
    {
      "message": "User registered successfully!",
      "success": true
    }
    ```
*   **Errori Comuni:** `400 Bad Request` (Username/Email già esistenti).

#### 2. Login
Autentica l'utente e restituisce il token JWT.
*   **Method:** `POST`
*   **URL:** `/signin`
*   **Request Body (JSON):**
    ```json
    {
      "usernameOrEmail": "mario.rossi", // Username o Email
      "password": "Password123!"
    }
    ```
*   **Response Body (JSON - 200 OK):**
    ```json
    {
      "token": "eyJhbGciOiJIUzI1NiJ9...", // JWT Token completo
      "type": "Bearer",
      "id": 1,
      "username": "mario.rossi",
      "email": "mario@test.com",
      "firstName": "Mario",
      "lastName": "Rossi",
      "roles": ["ROLE_USER"],
      "expiresIn": 86400000
    }
    ```

#### 3. Validazione Token
Verifica se un token salvato è ancora valido (utile per l'auto-login frontend).
*   **Method:** `POST`
*   **URL:** `/validate`
*   **Headers:** `Authorization: Bearer <token>`
*   **Response Body (JSON - 200 OK):**
    ```json
    {
      "message": "Token valido",
      "success": true
    }
    ```

---

### <a name="api-campionati"></a>🏆 Campionati

Base URL: `/api/campionati`
*Richiedono Header `Authorization: Bearer <token>`*

#### 1. Crea Campionato
*   **Method:** `POST`
*   **URL:** `/`
*   **Request Body (JSON):**
    ```json
    {
      "nome": "Torneo Aziendale 2024",
      "descrizione": "Torneo di calcetto post-lavoro",
      "idUtente": 1,                    // ID del creatore
      "tipologiaCampionato": "CALCIO_A_5" // Enum: CALCIO_A_5, CALCIO_A_7, CALCIO_A_11
    }
    ```
*   **Response Body (JSON - 200 OK):**
    ```json
    {
      "id": 10,
      "nome": "Torneo Aziendale 2024",
      "descrizione": "Torneo di calcetto post-lavoro",
      "codice": "A1B2C3D4",            // Codice generato per inviti
      "tipologiaCampionato": "CALCIO_A_5",
      "numeroPartecipanti": 1
    }
    ```

#### 2. Unisciti a Campionato (Join)
*   **Method:** `POST`
*   **URL:** `/join`
*   **Request Body (JSON):**
    ```json
    {
      "codice": "A1B2C3D4",  // Codice fornito dall'amministratore
      "idUtente": 2          // ID dell'utente che vuole unirsi
    }
    ```
*   **Response Body (JSON):** Stesso oggetto `CampionatoFindDTO` della creazione.

#### 3. Ottieni Campionati Utente
*   **Method:** `GET`
*   **URL:** `/iscritto/{idUtente}`
*   **Response Body (JSON - Lista):**
    ```json
    {
      "campionati": [
        {
          "id": 10,
          "nome": "Torneo Aziendale 2024",
          "codice": "A1B2C3D4",
          "tipologiaCampionato": "CALCIO_A_5",
          "numeroPartecipanti": 5
        }
      ]
    }
    ```

#### 4. Ottieni Classifica
*   **Method:** `GET`
*   **URL:** `/{idCampionato}/classifica`
*   **Response Body (JSON):**
    ```json
    {
      "classifica": [
        {
          "utente": {
            "id": 1,
            "username": "mario.rossi",
            "firstName": "Mario",
            "lastName": "Rossi"
          },
          "punti": 15,
          "partiteGiocate": 5,
          "partiteVinte": 5,
          "partitePareggiate": 0,
          "partitePerse": 0,
          "golFatti": 12,
          "golSubiti": 3,
          "assist": 5,
          "mediaVoto": 7.5
        }
        // ... altri utenti ordinati per punti
      ]
    }
    ```

---

### <a name="api-partite"></a>⚽ Partite

Base URL: `/api/partite`
*Richiedono Header `Authorization: Bearer <token>`*

#### 1. Crea Partita
*   **Method:** `POST`
*   **URL:** `/campionato/{idCampionato}`
*   **Request Body (JSON):**
    ```json
    {
      "dataOra": "2024-10-15T20:30:00",
      "luogo": "Centro Sportivo 'La Pinetina'"
    }
    ```
*   **Response Body (JSON):**
    ```json
    {
      "id": 55,
      "dataOra": "2024-10-15T20:30:00",
      "luogo": "Centro Sportivo 'La Pinetina'",
      "stato": "PROGRAMMATA",
      "golSquadraA": 0,
      "golSquadraB": 0
    }
    ```

#### 2. Iscriviti a Partita
*   **Method:** `POST`
*   **URL:** `/{idPartita}/iscriviti/{idUtente}`
*   **Request Body:** Vuoto.
*   **Response:** Restituisce l'oggetto Partita aggiornato con la lista dei partecipanti.

#### 3. Disiscriviti da Partita
*   **Method:** `POST`
*   **URL:** `/{idPartita}/disiscriviti/{idUtente}`
*   **Response:** Oggetto Partita aggiornato.

#### 4. Salva Squadre (Admin)
Assegna i giocatori iscritti alla Squadra A o B.
*   **Method:** `POST`
*   **URL:** `/{idPartita}/salvaSquadra`
*   **Request Body (JSON):**
    ```json
    {
      "idUtenti": [1, 2, 3],  // Lista ID utenti da spostare
      "squadra": "A"          // Destinazione: "A" o "B"
    }
    ```

#### 5. Termina Partita e Salva Voti
Chiude il match e calcola le statistiche.
*   **Method:** `PUT`
*   **URL:** `/termina`
*   **Request Body (JSON):**
    ```json
    {
      "id": 55,
      "golSquadraA": 5,
      "golSquadraB": 4,
      "luogo": "Centro Sportivo (Aggiornato)",
      "dataOra": "2024-10-15T20:30:00",
      "partecipazioni": [
        {
          "utente": { "id": 1 },
          "golSegnati": 3,
          "assist": 1,
          "voto": 8.0,
          "squadra": "A"
        },
        {
          "utente": { "id": 2 },
          "golSegnati": 2,
          "assist": 0,
          "voto": 7.0,
          "squadra": "B"
        }
        // ... lista completa partecipanti
      ]
    }
    ```

---

### <a name="api-utenti"></a>👤 Utenti

Base URL: `/api/utenti`

#### 1. Statistiche Rapide (QuickStats)
Restituisce un riassunto delle performance dell'utente.
*   **Method:** `GET`
*   **URL:** `/{idUtente}/quickStats`
*   **Response Body (JSON):**
    ```json
    {
      "totalePartiteGiocate": 25,
      "totaleGolFatti": 10,
      "mediaVotoGenerale": 6.8,
      "percentualeVittorie": 60.5
    }
    ```

---

## <a name="logiche-di-business-complesse"></a>8. Logiche di Business Complesse

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

## <a name="configurazione-e-setup"></a>9. Configurazione e Setup

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

## <a name="deployment-e-cicd"></a>10. Deployment e CI/CD

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

## <a name="troubleshooting-e-faq"></a>11. Troubleshooting e FAQ

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
