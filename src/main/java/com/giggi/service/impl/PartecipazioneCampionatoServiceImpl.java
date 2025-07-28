package com.giggi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.giggi.entity.PartecipazioneCampionato;
import com.giggi.repository.PartecipazioneCampionatoRepository;
import com.giggi.service.PartecipazioneCampionatoService;

@Service
@Transactional
@RequiredArgsConstructor
public class PartecipazioneCampionatoServiceImpl implements PartecipazioneCampionatoService {

    private final PartecipazioneCampionatoRepository partecipazioneCampionatoRepository;

    @Override
    public PartecipazioneCampionato save(PartecipazioneCampionato partecipazioneCampionato) {
        return partecipazioneCampionatoRepository.save(partecipazioneCampionato);
    }

    @Override
    public PartecipazioneCampionato update(PartecipazioneCampionato partecipazioneCampionato) {
        return partecipazioneCampionatoRepository.save(partecipazioneCampionato);
    }

    @Override
    public void deleteById(Long id) {
        partecipazioneCampionatoRepository.deleteById(id);
    }

    @Override
    public List<PartecipazioneCampionato> findAll() {
        return partecipazioneCampionatoRepository.findAll();
    }

    @Override
    public PartecipazioneCampionato findById(Long id) {
        return partecipazioneCampionatoRepository.findById(id).orElse(null);
    }
}