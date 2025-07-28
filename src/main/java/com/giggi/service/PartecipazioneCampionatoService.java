package com.giggi.service;

import java.util.List;

import com.giggi.entity.PartecipazioneCampionato;

public interface PartecipazioneCampionatoService {
    PartecipazioneCampionato save(PartecipazioneCampionato partecipazioneCampionato);

    PartecipazioneCampionato update(PartecipazioneCampionato partecipazioneCampionato);

    void deleteById(Long id);

    List<PartecipazioneCampionato> findAll();

    PartecipazioneCampionato findById(Long id);
}