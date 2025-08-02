package com.giggi.dto.response.PartecipazioneCampionato;

import com.giggi.dto.response.utente.UtenteFindDTO;
import com.giggi.dto.response.utente.UtenteFindDTOSmall;
import com.giggi.entity.enums.Squadra;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PartecipazioneCampionatoFindDTO {
    private Long id;
    private Squadra squadra;
    private UtenteFindDTOSmall utente;
    // Altri campi
}