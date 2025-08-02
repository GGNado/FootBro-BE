package com.giggi.service.impl;

import com.giggi.entity.Campionato;
import com.giggi.entity.PartecipazionePartita;
import com.giggi.entity.Utente;
import com.giggi.entity.enums.Squadra;
import com.giggi.entity.enums.StatoPartita;
import com.giggi.exception.campionato.CampionatoNotFoundException;
import com.giggi.exception.partita.PartitaErrorException;
import com.giggi.exception.partita.PartitaNotFoundException;
import com.giggi.exception.user.UserGiaIscrittoPartitaException;
import com.giggi.exception.user.UserIllegalIscrizionePartitaException;
import com.giggi.exception.user.UserNotFoundException;
import com.giggi.repository.CampionatoRepository;
import com.giggi.repository.PartecipazionePartitaRepository;
import com.giggi.repository.UtenteRepository;
import com.giggi.service.CampionatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.giggi.entity.Partita;
import com.giggi.repository.PartitaRepository;
import com.giggi.service.PartitaService;

@Service
@Transactional
@RequiredArgsConstructor
public class PartitaServiceImpl implements PartitaService {

    private final PartitaRepository partitaRepository;
    private final CampionatoRepository campionatoRepository;
    private final UtenteRepository utenteRepository;
    private final PartecipazionePartitaRepository partecipazionePartitaRepository;

    @Override
    public Partita save(Long idCampioanto, Partita partita) {
        Campionato campionato = campionatoRepository.findById(idCampioanto).orElseThrow(() -> new CampionatoNotFoundException("Campionato not found with id: " + idCampioanto));

        partita.setCampionato(campionato);
        partita.setStato(StatoPartita.PROGRAMMATA);
        partita.setGolSquadraA(0);
        partita.setGolSquadraB(0);

        return partitaRepository.save(partita);
    }

    @Override
    public Partita update(Partita partita) {
        return partitaRepository.save(partita);
    }

    @Override
    public void deleteById(Long id) {
        partitaRepository.deleteById(id);
    }

    @Override
    public List<Partita> findAll() {
        return partitaRepository.findAll();
    }

    @Override
    public List<Partita> findByIdCampionato(Long idCampionato) {
        return partitaRepository.findAllByCampionatoId(idCampionato);
    }

    @Override
    public List<Partita> findByIdCampionatoAndProgrammate(Long idCampionato) {
        return partitaRepository.findAllByCampionatoIdAndStato(idCampionato, StatoPartita.PROGRAMMATA);
    }

    @Override
    public Partita findById(Long id) {
        return partitaRepository.findById(id).orElse(null);
    }

    @Override
    public Partita iscrivitiPartita(Long idPartita, Long idUtente) {
        Partita partita = partitaRepository.findById(idPartita)
                .orElseThrow(() -> new PartitaNotFoundException("Partita non trovata con id: " + idPartita));

        Utente utente = utenteRepository.findById(idUtente)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato con id: " + idUtente));

        if (!partita.getCampionato().getPartecipanti().contains(utente)) {
            throw new UserIllegalIscrizionePartitaException("Utente non partecipa al campionato di questa partita");
        }

        if (partecipazionePartitaRepository.existsByPartitaIdAndUtenteId(idPartita, idUtente)) {
            throw new UserGiaIscrittoPartitaException("Utente già iscritto a questa partita");
        }


        if (partita.getStato() != StatoPartita.PROGRAMMATA) {
            throw new PartitaErrorException("Non è possibile iscriversi a una partita non programmata");
        }

        PartecipazionePartita partecipazione = PartecipazionePartita.builder()
                .utente(utente)
                .partita(partita)
                .squadra(Squadra.DA_ASSEGNARE)
                .golSegnati(0)
                .assist(0)
                .presente(true)
                .build();

        return partecipazionePartitaRepository.save(partecipazione).getPartita();

    }
}