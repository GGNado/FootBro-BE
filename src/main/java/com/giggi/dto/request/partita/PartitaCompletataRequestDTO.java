package com.giggi.dto.request.partita;

import com.giggi.dto.response.Partita.PartecipazionePartitaFindDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class PartitaCompletataRequestDTO {
    private Long id;
    private String luogo;
    private LocalDateTime dataOra;
    private Integer golSquadraB;
    private Integer golSquadraA;
    private Set<PartecipazionePartitaFindDTO> partecipazioni;
}
