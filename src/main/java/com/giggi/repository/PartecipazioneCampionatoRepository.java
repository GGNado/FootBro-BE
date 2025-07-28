package com.giggi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.giggi.entity.PartecipazioneCampionato;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface PartecipazioneCampionatoRepository extends JpaRepository<PartecipazioneCampionato, Long> {
}