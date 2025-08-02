package com.giggi.controller;

import com.giggi.dto.response.stats.QuickStatsResponseDTO;
import com.giggi.dto.response.utente.UtenteFindAllDTO;
import com.giggi.mapper.UtenteMapper;
import com.giggi.service.UtenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utenti")
@RequiredArgsConstructor
@Tag(name = "Utenti", description = "API per la gestione degli utenti")
public class UtenteController {
    private final UtenteService utenteService;
    private final UtenteMapper utenteMapper;

    @Operation(summary = "Ottieni tutti gli utenti", description = "Restituisce l'elenco completo di tutti gli utenti registrati")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utenti trovati con successo",
                content = { @Content(mediaType = "application/json", 
                schema = @Schema(implementation = UtenteFindAllDTO.class)) }),
        @ApiResponse(responseCode = "401", description = "Non autorizzato", content = @Content),
        @ApiResponse(responseCode = "403", description = "Accesso vietato", content = @Content)
    })
    @GetMapping
    public ResponseEntity<UtenteFindAllDTO> getAllUtentes() {
        return ResponseEntity.ok(
                new UtenteFindAllDTO(
                        utenteService
                                .findAll().stream()
                                .map(utenteMapper::convert)
                                .toList())
        );
    }

    @Operation(summary = "Ottieni statistiche rapide dell'utente", description = "Restituisce le statistiche principali di un utente specifico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistiche trovate con successo",
                content = { @Content(mediaType = "application/json", 
                schema = @Schema(implementation = QuickStatsResponseDTO.class)) }),
        @ApiResponse(responseCode = "404", description = "Utente non trovato", content = @Content),
        @ApiResponse(responseCode = "401", description = "Non autorizzato", content = @Content)
    })
    @GetMapping("{idUtente}/quickStats")
    public ResponseEntity<QuickStatsResponseDTO> getQuickStatsByUtenteId(
            @Parameter(description = "ID dell'utente", required = true) @PathVariable Long idUtente) {
        return ResponseEntity.ok(
                utenteService.getQuickStatsByUtenteId(idUtente));
    }
}