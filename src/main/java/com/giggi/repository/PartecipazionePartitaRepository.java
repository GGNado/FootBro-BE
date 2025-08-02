package com.giggi.repository;

import com.giggi.entity.PartecipazionePartita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface PartecipazionePartitaRepository extends JpaRepository<PartecipazionePartita, Long> {

    boolean existsByPartitaIdAndUtenteId(Long idPartita, Long idUtente);
}
