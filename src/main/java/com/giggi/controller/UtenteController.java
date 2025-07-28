package com.giggi.controller;

import com.giggi.dto.response.utente.UtenteFindAllDTO;
import com.giggi.mapper.UtenteMapper;
import com.giggi.service.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utentes")
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
}