package com.giggi.service;

import java.util.List;

import com.giggi.dto.request.partita.PartitaCreateRequestDTO;
import com.giggi.entity.Partita;

public interface PartitaService {
    Partita save(Long idCampionato, Partita partita);

    Partita update(Partita partita);

    void deleteById(Long id);

    List<Partita> findAll();
    List<Partita> findByIdCampionato(Long idCampionato);
    List<Partita> findByIdCampionatoAndProgrammate(Long idUtente);

    Partita findById(Long id);

    Partita iscrivitiPartita(Long idPartita, Long idUtente);
}