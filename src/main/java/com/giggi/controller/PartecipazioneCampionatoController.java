package com.giggi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.giggi.entity.PartecipazioneCampionato;
import com.giggi.service.PartecipazioneCampionatoService;

@RestController
@RequestMapping("/api/partecipazioneCampionatos")
@RequiredArgsConstructor
public class PartecipazioneCampionatoController {
    private final PartecipazioneCampionatoService partecipazioneCampionatoService;

    @GetMapping
    public List<PartecipazioneCampionato> getAllPartecipazioneCampionatos() {
        return partecipazioneCampionatoService.findAll();
    }
    // CRUD endpoints qui
}