package com.giggi.dto.response.Partita;

import com.giggi.dto.response.utente.UtenteFindDTOSmall;
import com.giggi.entity.enums.Squadra;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartecipazionePartitaFindDTO {
    private UtenteFindDTOSmall utente;
    private Integer golSegnati;
    private Integer assist;
    private Double voto;
    private Squadra squadra;
}
