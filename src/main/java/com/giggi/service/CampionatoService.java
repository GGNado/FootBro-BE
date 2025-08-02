package com.giggi.service;

import java.util.List;

import com.giggi.dto.request.campionato.CampionatoCreateRequestDTO;
import com.giggi.dto.request.campionato.CampionatoJoinRequestDTO;
import com.giggi.dto.response.campionato.ClassificaResponse;
import com.giggi.entity.Campionato;

public interface CampionatoService {
    Campionato save(CampionatoCreateRequestDTO campionatoCreateRequestDTO);

    Campionato update(Campionato campionato);

    void deleteById(Long id);

    List<Campionato> findAll();

    List<Campionato> findAllByPartecipazioneCampionatoUtenteId(Long id);

    Campionato findById(Long id);

    Campionato joinCampionato(CampionatoJoinRequestDTO campionatoJoinRequestDTO);

    ClassificaResponse getClassificaByIdCampionato(Long idCampionato);
}