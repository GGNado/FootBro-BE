package com.giggi.service.impl;
import com.giggi.dto.request.campionato.CampionatoCreateRequestDTO;
import com.giggi.dto.request.campionato.CampionatoJoinRequestDTO;
import com.giggi.entity.Utente;
import com.giggi.exception.campionato.CampionatoNotFoundException;
import com.giggi.mapper.CampionatoMapper;
import com.giggi.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

import com.giggi.entity.Campionato;
import com.giggi.repository.CampionatoRepository;
import com.giggi.service.CampionatoService;

@Service
@Transactional
@RequiredArgsConstructor
public class CampionatoServiceImpl implements CampionatoService {

    private final CampionatoRepository campionatoRepository;
    private final UtenteRepository utenteRepository;
    private final CampionatoMapper campionatoMapper;

    @Override
    public Campionato save(CampionatoCreateRequestDTO campionatoCreateRequestDTO) {
        Utente creatore = utenteRepository.findById(campionatoCreateRequestDTO.getIdUtente()).orElseThrow();
        Campionato campionato = campionatoMapper.convert(campionatoCreateRequestDTO);

        campionato.setCreatore(creatore);
        campionato.setCodice(campionato.generaCodiceUnivoco());
        campionato.setPartecipazioni(new HashSet<>());
        campionato.aggiungiPartecipante(creatore);


        return campionatoRepository.save(campionato);
    }

    @Override
    public Campionato update(Campionato campionato) {
        return campionatoRepository.save(campionato);
    }

    @Override
    public void deleteById(Long id) {
        campionatoRepository.deleteById(id);
    }

    @Override
    public List<Campionato> findAll() {
        return campionatoRepository.findAll();
    }

    @Override
    public List<Campionato> findAllByPartecipazioneCampionatoUtenteId(Long id) {
        return campionatoRepository.findAllByPartecipazioniUtenteId(id);
    }

    @Override
    public Campionato findById(Long id) {
        return campionatoRepository.findById(id).orElse(null);
    }

    @Override
    public Campionato joinCampionato(CampionatoJoinRequestDTO campionatoJoinRequestDTO) {
        Campionato campionato = campionatoRepository.findByCodice(campionatoJoinRequestDTO.getCodice()).orElseThrow(
                () -> new CampionatoNotFoundException("Campionato non trovato con codice: " + campionatoJoinRequestDTO.getCodice())
        );

        Utente utente = utenteRepository.findById(campionatoJoinRequestDTO.getIdUtente()).orElseThrow(
                () -> new IllegalArgumentException("Utente non trovato con ID: " + campionatoJoinRequestDTO.getIdUtente())
        );

        if (campionato.getPartecipanti().contains(utente)) {
            return campionato;
        }

        campionato.aggiungiPartecipante(utente);
        return campionatoRepository.save(campionato);
    }
}