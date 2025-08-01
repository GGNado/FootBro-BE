package com.giggi.service;

import com.giggi.dto.response.stats.QuickStatsResponseDTO;
import com.giggi.entity.Utente;

import java.util.List;

public interface UtenteService {
    Utente save(Utente utente);

    Utente update(Utente utente);

    void deleteById(Long id);

    List<Utente> findAll();

    Utente findById(Long id);

    QuickStatsResponseDTO getQuickStatsByUtenteId(Long idUtente);
}