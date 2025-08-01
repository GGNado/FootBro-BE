package com.giggi.controller;

import com.giggi.dto.request.campionato.CampionatoCreateRequestDTO;
import com.giggi.dto.request.campionato.CampionatoJoinRequestDTO;
import com.giggi.dto.response.campionato.CampionatoFindAllDTO;
import com.giggi.dto.response.campionato.CampionatoFindDTO;
import com.giggi.mapper.CampionatoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.giggi.service.CampionatoService;

@RestController
@RequestMapping("/api/campionati")
@RequiredArgsConstructor
public class CampionatoController {
    private final CampionatoService campionatoService;
    private final CampionatoMapper campionatoMapper;

    @GetMapping
    public ResponseEntity<CampionatoFindAllDTO> getAllCampionati() {
        return ResponseEntity.ok(
                new CampionatoFindAllDTO(
                        campionatoMapper.convert(campionatoService.findAll()))
                );
    }

    @GetMapping("/iscritto/{idUtente}")
    public ResponseEntity<CampionatoFindAllDTO> getCampionatiByUtenteId(@PathVariable Long idUtente) {
        return ResponseEntity.ok(
                new CampionatoFindAllDTO(
                        campionatoMapper.convert(campionatoService.findAllByPartecipazioneCampionatoUtenteId(idUtente)))
                );
    }

    @PostMapping
    public ResponseEntity<CampionatoFindDTO> createCampionato(
            @RequestBody CampionatoCreateRequestDTO campionatoCreateRequestDTO) {
        return ResponseEntity.ok(campionatoMapper.convert(
                            campionatoService.save(campionatoCreateRequestDTO)));
    }

    @PostMapping("join")
    public ResponseEntity<CampionatoFindDTO> joinCampionato(
            @RequestBody CampionatoJoinRequestDTO campionatoJoinRequestDTO) {
        return ResponseEntity.ok(campionatoMapper.convert(
                campionatoService.joinCampionato(campionatoJoinRequestDTO)));
    }

}