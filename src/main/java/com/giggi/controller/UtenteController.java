package com.giggi.controller;

import com.giggi.dto.response.stats.QuickStatsResponseDTO;
import com.giggi.dto.response.utente.UtenteFindAllDTO;
import com.giggi.mapper.UtenteMapper;
import com.giggi.service.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utenti")
@RequiredArgsConstructor
public class UtenteController {
    private final UtenteService utenteService;
    private final UtenteMapper utenteMapper;

    @GetMapping
    public ResponseEntity<UtenteFindAllDTO> getAllUtentes() {
        return ResponseEntity.ok(
                new UtenteFindAllDTO(
                        utenteService
                                .findAll().stream()
                                .map(utenteMapper::conver)
                                .toList())
        );
    }

    @GetMapping("{idUtente}/quickStats")
    public ResponseEntity<QuickStatsResponseDTO> getQuickStatsByUtenteId(
            @PathVariable Long idUtente) {
        return ResponseEntity.ok(
                utenteService.getQuickStatsByUtenteId(idUtente));
    }
}