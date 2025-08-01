package com.giggi.repository;

import com.giggi.dto.response.stats.QuickStatsResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import com.giggi.entity.PartecipazioneCampionato;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface PartecipazioneCampionatoRepository extends JpaRepository<PartecipazioneCampionato, Long> {
    @Query("SELECT new com.giggi.dto.response.stats.QuickStatsResponseDTO(" +
            "COALESCE(SUM(p.partiteGiocate), 0), " +
            "COALESCE(SUM(p.golFatti), 0), " +
            "COALESCE(SUM(p.assist), 0)) " +
            "FROM PartecipazioneCampionato p " +
            "WHERE p.utente.id = :utenteId AND p.attivo = true")
    QuickStatsResponseDTO getQuickStatsByUtenteId(@Param("utenteId") Long utenteId);
}