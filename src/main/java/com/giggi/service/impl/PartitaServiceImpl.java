package com.giggi.service.impl;

import com.giggi.dto.request.partita.PartitaCompletataRequestDTO;
import com.giggi.dto.request.partita.PartitaCreateRequestDTO;
import com.giggi.dto.request.partita.SalvaSquadraRequestDTO;
import com.giggi.dto.response.Partita.PartecipazionePartitaFindDTO;
import com.giggi.entity.*;
import com.giggi.entity.enums.Squadra;
import com.giggi.entity.enums.StatoPartita;
import com.giggi.exception.campionato.CampionatoNotFoundException;
import com.giggi.exception.partita.PartitaErrorException;
import com.giggi.exception.partita.PartitaNotFoundException;
import com.giggi.exception.user.UserGiaIscrittoPartitaException;
import com.giggi.exception.user.UserIllegalIscrizionePartitaException;
import com.giggi.exception.user.UserNonIscrittoException;
import com.giggi.exception.user.UserNotFoundException;
import com.giggi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.giggi.service.PartitaService;

@Service
@Transactional
@RequiredArgsConstructor
public class PartitaServiceImpl implements PartitaService {

    private final PartitaRepository partitaRepository;
    private final CampionatoRepository campionatoRepository;
    private final UtenteRepository utenteRepository;
    private final PartecipazionePartitaRepository partecipazionePartitaRepository;
    private final PartecipazioneCampionatoRepository partecipazioneCampionatoRepository;

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

    @Override
    @Transactional
    public Partita salvaSquadra(Long idPartita, SalvaSquadraRequestDTO salvaSquadraRequestDTO) {
        Partita partita = partitaRepository.findById(idPartita)
                .orElseThrow(() -> new PartitaNotFoundException("Partita non trovata con id: " + idPartita));

        Squadra squadra = salvaSquadraRequestDTO.getSquadra();

        for (PartecipazionePartita partecipazione : partita.getPartecipazioni()) {
            if (salvaSquadraRequestDTO.getIdUtenti().contains(partecipazione.getUtente().getId())) {
                partecipazione.setSquadra(squadra);
                partecipazionePartitaRepository.save(partecipazione);
            }
        }

        return partita;
    }

    @Override
    public Partita disiscrivitiPartita(Long idPartita, Long idUtente) {
        Partita partita = partitaRepository.findById(idPartita)
                .orElseThrow(() -> new PartitaNotFoundException("Partita non trovata con id: " + idPartita));

        Utente utente = utenteRepository.findById(idUtente)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato con id: " + idUtente));

        PartecipazionePartita partecipazionePartita = partecipazionePartitaRepository.findByPartitaIdAndUtenteId(idPartita, idUtente)
                .orElseThrow(() -> new UserNonIscrittoException("Utente non iscritto a questa partita"));


        partecipazionePartitaRepository.delete(partecipazionePartita);
        return partita;
    }

    @Override
    @Transactional
    public void terminaPartita(PartitaCompletataRequestDTO partitaCompletataRequestDTO) {
        // Trova la partita da completare
        Partita partita = partitaRepository.findById(partitaCompletataRequestDTO.getId())
                .orElseThrow(() -> new PartitaNotFoundException("Partita non trovata con id: " + partitaCompletataRequestDTO.getId()));

        // Verifica che la partita sia ancora programmata
        if (partita.getStato() == StatoPartita.TERMINATA) {
            throw new PartitaErrorException("La partita è già stata completata");
        }

        // Aggiorna i dati della partita
        partita.setGolSquadraA(partitaCompletataRequestDTO.getGolSquadraA());
        partita.setGolSquadraB(partitaCompletataRequestDTO.getGolSquadraB());
        partita.setLuogo(partitaCompletataRequestDTO.getLuogo());
        partita.setDataOra(partitaCompletataRequestDTO.getDataOra());
        partita.setStato(StatoPartita.TERMINATA);

        // Salva la partita aggiornata
        partitaRepository.save(partita);

        // Aggiorna le partecipazioni partita con le statistiche individuali
        for (PartecipazionePartitaFindDTO partecipazioneDTO : partitaCompletataRequestDTO.getPartecipazioni()) {
            PartecipazionePartita partecipazione = partecipazionePartitaRepository
                    .findByPartitaIdAndUtenteId(partita.getId(), partecipazioneDTO.getUtente().getId())
                    .orElseThrow(() -> new UserNonIscrittoException("Utente non trovato nelle partecipazioni"));

            // Aggiorna le statistiche individuali della partita
            partecipazione.setGolSegnati(partecipazioneDTO.getGolSegnati());
            partecipazione.setAssist(partecipazioneDTO.getAssist());
            partecipazione.setVoto(partecipazioneDTO.getVoto());
            partecipazione.setSquadra(partecipazioneDTO.getSquadra());

            System.out.println("Aggiorno statistiche individuali");
            partecipazionePartitaRepository.save(partecipazione);

            // Aggiorna le statistiche nella classifica del campionato
            System.out.println("Aggiorno classifica campionato");
            aggiornaClassificaCampionato(partita, partecipazione, partitaCompletataRequestDTO);
        }
    }

    /**
     * Aggiorna le statistiche nella classifica del campionato per un partecipante
     */
    private void aggiornaClassificaCampionato(Partita partita, PartecipazionePartita partecipazione,
                                              PartitaCompletataRequestDTO requestDTO) {
        // Trova la partecipazione al campionato dell'utente
        PartecipazioneCampionato classificaCampionato = partecipazioneCampionatoRepository
                .findByUtenteIdAndCampionatoId(partecipazione.getUtente().getId(), partita.getCampionato().getId())
                .orElseThrow(() -> new RuntimeException("Partecipazione campionato non trovata"));

        // Incrementa partite giocate
        classificaCampionato.setPartiteGiocate(classificaCampionato.getPartiteGiocate() + 1);

        // Aggiorna gol fatti e assist
        classificaCampionato.setGolFatti(classificaCampionato.getGolFatti() + partecipazione.getGolSegnati());
        classificaCampionato.setAssist(classificaCampionato.getAssist() + partecipazione.getAssist());

        // Calcola e aggiorna la media voto
        double nuovaMediaVoto = ((classificaCampionato.getMediaVoto() * (classificaCampionato.getPartiteGiocate() - 1))
                + partecipazione.getVoto()) / classificaCampionato.getPartiteGiocate();
        classificaCampionato.setMediaVoto(Math.round(nuovaMediaVoto * 100.0) / 100.0); // Arrotonda a 2 decimali

        // Determina il risultato della squadra del giocatore
        Squadra squadraGiocatore = partecipazione.getSquadra();
        int golSquadraGiocatore = (squadraGiocatore == Squadra.A) ? requestDTO.getGolSquadraA() : requestDTO.getGolSquadraB();
        int golSquadraAvversaria = (squadraGiocatore == Squadra.A) ? requestDTO.getGolSquadraB() : requestDTO.getGolSquadraA();

        // Calcola i punti e aggiorna vittorie/pareggi/sconfitte
        if (golSquadraGiocatore > golSquadraAvversaria) {
            // Vittoria
            classificaCampionato.setPartiteVinte(classificaCampionato.getPartiteVinte() + 1);
            classificaCampionato.setPunti(classificaCampionato.getPunti() + 3);
        } else if (golSquadraGiocatore == golSquadraAvversaria) {
            // Pareggio
            classificaCampionato.setPartitePareggiate(classificaCampionato.getPartitePareggiate() + 1);
            classificaCampionato.setPunti(classificaCampionato.getPunti() + 1);
        } else {
            // Sconfitta
            classificaCampionato.setPartitePerse(classificaCampionato.getPartitePerse() + 1);
            // Nessun punto per la sconfitta
        }

        // Aggiorna i gol subiti (gol della squadra avversaria)
        classificaCampionato.setGolSubiti(classificaCampionato.getGolSubiti() + golSquadraAvversaria);

        // Aggiorna la data dell'ultima partita
        classificaCampionato.setDataUltimaPartita(requestDTO.getDataOra());

        // Salva le statistiche aggiornate
        System.out.println("AIIIIIOOO");
        partecipazioneCampionatoRepository.save(classificaCampionato);
    }

}