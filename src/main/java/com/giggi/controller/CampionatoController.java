package com.giggi.controller;

import com.giggi.dto.request.campionato.CampionatoCreateRequestDTO;
import com.giggi.dto.request.campionato.CampionatoJoinRequestDTO;
import com.giggi.dto.response.campionato.CampionatoFindAllDTO;
import com.giggi.dto.response.campionato.CampionatoFindDTO;
import com.giggi.dto.response.campionato.ClassificaResponse;
import com.giggi.mapper.CampionatoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.giggi.service.CampionatoService;

@RestController
@RequestMapping("/api/campionati")
@RequiredArgsConstructor
@Tag(name = "Campionati", description = "API per la gestione dei campionati")
public class CampionatoController {
    private final CampionatoService campionatoService;
    private final CampionatoMapper campionatoMapper;

    @Operation(summary = "Ottieni tutti i campionati", description = "Restituisce l'elenco completo di tutti i campionati")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Campionati trovati con successo",
                content = { @Content(mediaType = "application/json", 
                schema = @Schema(implementation = CampionatoFindAllDTO.class)) }),
        @ApiResponse(responseCode = "401", description = "Non autorizzato", content = @Content)
    })
    @GetMapping
    public ResponseEntity<CampionatoFindAllDTO> getAllCampionati() {
        return ResponseEntity.ok(
                new CampionatoFindAllDTO(
                        campionatoMapper.convert(campionatoService.findAll()))
                );
    }

    @Operation(summary = "Ottieni campionati a cui è iscritto un utente", 
              description = "Restituisce l'elenco dei campionati a cui l'utente specificato è iscritto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Campionati trovati con successo",
                content = { @Content(mediaType = "application/json", 
                schema = @Schema(implementation = CampionatoFindAllDTO.class)) }),
        @ApiResponse(responseCode = "404", description = "Utente non trovato", content = @Content),
        @ApiResponse(responseCode = "401", description = "Non autorizzato", content = @Content)
    })
    @GetMapping("/iscritto/{idUtente}")
    public ResponseEntity<CampionatoFindAllDTO> getCampionatiByUtenteId(
            @Parameter(description = "ID dell'utente", required = true) @PathVariable Long idUtente) {
        return ResponseEntity.ok(
                new CampionatoFindAllDTO(
                        campionatoMapper.convert(campionatoService.findAllByPartecipazioneCampionatoUtenteId(idUtente)))
                );
    }

    @Operation(summary = "Crea un nuovo campionato", description = "Crea un nuovo campionato con i dati forniti")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Campionato creato con successo",
                content = { @Content(mediaType = "application/json", 
                schema = @Schema(implementation = CampionatoFindDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Dati del campionato non validi", content = @Content),
        @ApiResponse(responseCode = "401", description = "Non autorizzato", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CampionatoFindDTO> createCampionato(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dati per la creazione del campionato", 
                required = true, content = @Content(schema = @Schema(implementation = CampionatoCreateRequestDTO.class)))
            @RequestBody CampionatoCreateRequestDTO campionatoCreateRequestDTO) {
        return ResponseEntity.ok(campionatoMapper.convert(
                            campionatoService.save(campionatoCreateRequestDTO)));
    }

    @Operation(summary = "Iscriviti a un campionato", description = "Iscrive un utente a un campionato esistente usando il codice")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Iscrizione effettuata con successo",
                content = { @Content(mediaType = "application/json", 
                schema = @Schema(implementation = CampionatoFindDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Codice campionato non valido", content = @Content),
        @ApiResponse(responseCode = "404", description = "Campionato o utente non trovato", content = @Content),
        @ApiResponse(responseCode = "401", description = "Non autorizzato", content = @Content)
    })
    @PostMapping("join")
    public ResponseEntity<CampionatoFindDTO> joinCampionato(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dati per l'iscrizione al campionato", 
                required = true, content = @Content(schema = @Schema(implementation = CampionatoJoinRequestDTO.class)))
            @RequestBody CampionatoJoinRequestDTO campionatoJoinRequestDTO) {
        return ResponseEntity.ok(campionatoMapper.convert(
                campionatoService.joinCampionato(campionatoJoinRequestDTO)));
    }

    @GetMapping("/{idCampionato}/classifica")
    @Operation(summary = "Ottieni la classifica di un campionato", description = "Restituisce la classifica del campionato specificato")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Classifica trovata con successo",
                content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = CampionatoFindDTO.class)) }),
        @ApiResponse(responseCode = "404", description = "Campionato non trovato", content = @Content),
        @ApiResponse(responseCode = "401", description = "Non autorizzato", content = @Content)
    })
    public ResponseEntity<ClassificaResponse> getClassificaByIdCampionato(
            @Parameter(description = "ID del campionato", required = true) @PathVariable Long idCampionato) {
        return ResponseEntity.ok(campionatoService.getClassificaByIdCampionato(idCampionato));
    }

}