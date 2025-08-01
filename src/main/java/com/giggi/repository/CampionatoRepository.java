package com.giggi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.giggi.entity.Campionato;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RepositoryRestResource(exported = false)
public interface CampionatoRepository extends JpaRepository<Campionato, Long> {
    Optional<Campionato> findByCodice(String codice);

    List<Campionato> findAllByPartecipazioniUtenteId(Long utenteId);
}