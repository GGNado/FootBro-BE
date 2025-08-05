package com.giggi.dto.response.Partita;

import com.giggi.dto.response.PartecipazioneCampionato.PartecipazioneCampionatoFindDTO;
import com.giggi.entity.PartecipazionePartita;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
public class PartitaFindDTO {
    private Long id;
    private String luogo;
    private LocalDateTime dataOra;
    private Integer golSquadraB;
    private Integer golSquadraA;
    private Set<PartecipazionePartitaFindDTO> partecipazioni;

}