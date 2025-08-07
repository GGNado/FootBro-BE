package com.giggi.controller;

import com.giggi.dto.request.partita.PartitaCreateRequestDTO;
import com.giggi.dto.request.partita.SalvaSquadraRequestDTO;
import com.giggi.dto.response.Partita.PartitaFindAllDTO;
import com.giggi.dto.response.Partita.PartitaFindAllSmallDTO;
import com.giggi.dto.response.Partita.PartitaFindDTO;
import com.giggi.mapper.PartitaMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.giggi.entity.Partita;
import com.giggi.service.PartitaService;

@RestController
@RequestMapping("/api/partite")
@RequiredArgsConstructor
@Tag(name = "Partite", description = "API per la gestione delle partite di calcio")
public class PartitaController {
    private final PartitaService partitaService;
    private final PartitaMapper partitaMapper;

    @Operation(summary = "Ottieni tutte le partite", description = "Restituisce l'elenco completo di tutte le partite")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Partite trovate con successo",
                content = { @Content(mediaType = "application/json", 
                schema = @Schema(implementation = Partita.class)) }),
        @ApiResponse(responseCode = "401", description = "Non autorizzato", content = @Content)
    })
    @GetMapping
    public List<Partita> getAllPartite() {
        return partitaService.findAll();
    }

    @Operation(summary = "Ottieni partite di un campionato", 
              description = "Restituisce tutte le partite associate a un campionato specifico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Partite trovate con successo",
                content = { @Content(mediaType = "application/json", 
                schema = @Schema(implementation = PartitaFindAllDTO.class)) }),
        @ApiResponse(responseCode = "404", description = "Campionato non trovato", content = @Content),
        @ApiResponse(responseCode = "401", description = "Non autorizzato", content = @Content)
    })
    @GetMapping("/campionato/{idCampionato}/all")
    public ResponseEntity<PartitaFindAllDTO> getPartiteByCampionato(
            @Parameter(description = "ID del campionato", required = true) @PathVariable Long idCampionato) {
        return ResponseEntity.ok(
                new PartitaFindAllDTO(
                        partitaMapper.convert(
                                partitaService.findByIdCampionato(idCampionato))
                )
        );
    }

    @Operation(summary = "Ottieni partite programmate di un campionato (formato ridotto)", 
              description = "Restituisce le partite programmate di un campionato in formato ridotto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Partite trovate con successo",
                content = { @Content(mediaType = "application/json", 
                schema = @Schema(implementation = PartitaFindAllSmallDTO.class)) }),
        @ApiResponse(responseCode = "404", description = "Campionato non trovato", content = @Content),
        @ApiResponse(responseCode = "401", description = "Non autorizzato", content = @Content)
    })
    @GetMapping("/campionato/{idCampionato}/programmateSmall")
    public ResponseEntity<PartitaFindAllSmallDTO> getPartiteProgrammateByCampionatoSmall(
            @Parameter(description = "ID del campionato", required = true) @PathVariable Long idCampionato) {
        return ResponseEntity.ok(
                new PartitaFindAllSmallDTO(
                        partitaMapper.convertSmall(
                                partitaService.findByIdCampionato(idCampionato))
                )
        );
    }

    @Operation(summary = "Ottieni partite programmate di un campionato (dettagliato)", 
              description = "Restituisce le partite programmate di un campionato con tutti i dettagli")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Partite trovate con successo",
                content = { @Content(mediaType = "application/json", 
                schema = @Schema(implementation = PartitaFindAllDTO.class)) }),
        @ApiResponse(responseCode = "404", description = "Campionato non trovato", content = @Content),
        @ApiResponse(responseCode = "401", description = "Non autorizzato", content = @Content)
    })
    @GetMapping("/campionato/{idCampionato}/programmateDetails")
    public ResponseEntity<PartitaFindAllDTO> getPartiteProgrammateByCampionatoDetails(
            @Parameter(description = "ID del campionato", required = true) @PathVariable Long idCampionato) {
        return ResponseEntity.ok(
                new PartitaFindAllDTO(
                        partitaMapper.convert(
                                partitaService.findByIdCampionato(idCampionato))
                )
        );
    }

    @Operation(summary = "Crea una nuova partita", 
              description = "Crea una nuova partita all'interno di un campionato specifico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Partita creata con successo",
                content = { @Content(mediaType = "application/json", 
                schema = @Schema(implementation = PartitaFindDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Dati della partita non validi", content = @Content),
        @ApiResponse(responseCode = "404", description = "Campionato non trovato", content = @Content),
        @ApiResponse(responseCode = "401", description = "Non autorizzato", content = @Content)
    })
    @PostMapping("/campionato/{idCampionato}")
    public ResponseEntity<PartitaFindDTO> createPartita(
            @Parameter(description = "ID del campionato", required = true) @PathVariable Long idCampionato,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dati per la creazione della partita", 
                required = true, content = @Content(schema = @Schema(implementation = PartitaCreateRequestDTO.class)))
            @RequestBody PartitaCreateRequestDTO partitaCreateRequestDTO) {

        return ResponseEntity.ok(
                partitaMapper.convert(
                        partitaService.save(idCampionato, partitaMapper.convert(partitaCreateRequestDTO))
                )
        );
    }

    @Operation(summary = "Iscrivi un utente a una partita", 
              description = "Registra un utente come partecipante a una partita specifica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Iscrizione effettuata con successo",
                content = { @Content(mediaType = "application/json", 
                schema = @Schema(implementation = PartitaFindDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Utente già iscritto o partita non programmata", content = @Content),
        @ApiResponse(responseCode = "404", description = "Partita o utente non trovato", content = @Content),
        @ApiResponse(responseCode = "401", description = "Non autorizzato", content = @Content),
        @ApiResponse(responseCode = "403", description = "Utente non partecipante al campionato", content = @Content)
    })
    @PostMapping("/{idPartita}/iscriviti/{idUtente}")
    public ResponseEntity<PartitaFindDTO> iscrivitiPartita(
            @Parameter(description = "ID della partita", required = true) @PathVariable Long idPartita,
            @Parameter(description = "ID dell'utente da iscrivere", required = true) @PathVariable Long idUtente)
    {
        return ResponseEntity.ok(
                partitaMapper.convert(
                        partitaService.iscrivitiPartita(idPartita, idUtente)
                )
        );
    }

    @PostMapping("/{idPartita}/disiscriviti/{idUtente}")
    public ResponseEntity<PartitaFindDTO> disiscrivitiPartita(
            @Parameter(description = "ID della partita", required = true) @PathVariable Long idPartita,
            @Parameter(description = "ID dell'utente da disiscrivere", required = true) @PathVariable Long idUtente
    ){
        return ResponseEntity.ok(
                partitaMapper.convert(
                        partitaService.disiscrivitiPartita(idPartita, idUtente)
                )
        );
    }

    @Operation(summary = "Salva la squadra di una partita",
              description = "Aggiorna le informazioni della squadra in una partita esistente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Squadra salvata con successo",
                content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = PartitaFindDTO.class)) }),
        @ApiResponse(responseCode = "404", description = "Partita non trovata", content = @Content),
        @ApiResponse(responseCode = "401", description = "Non autorizzato", content = @Content)
    })
    @PostMapping("/{idPartita}/salvaSquadra")
    public ResponseEntity<PartitaFindDTO> salvaSquadra(
            @Parameter(description = "ID della partita", required = true) @PathVariable Long idPartita,
            @RequestBody SalvaSquadraRequestDTO salvaSquadraRequestDTO) {

        return ResponseEntity.ok(
                partitaMapper.convert(
                        partitaService.salvaSquadra(idPartita, salvaSquadraRequestDTO)
                )
        );
    }


}