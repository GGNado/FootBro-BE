package com.giggi.repository;

import com.giggi.entity.enums.StatoPartita;
import org.springframework.data.jpa.repository.JpaRepository;
import com.giggi.entity.Partita;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RepositoryRestResource(exported = false)
public interface PartitaRepository extends JpaRepository<Partita, Long> {
    List<Partita> findAllByCampionatoId(Long campionatoId);

    Long id(Long id);

    List<Partita> findAllByCampionatoIdAndStato(Long idCampionato, StatoPartita statoPartita);
}