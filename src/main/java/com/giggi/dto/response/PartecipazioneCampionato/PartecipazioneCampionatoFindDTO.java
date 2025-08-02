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
    private UtenteFindDTOSmall utente;
    private Integer punti;
    private Integer golFatti;
    private Integer assist;
    private Integer partiteGiocate;
    private Integer partiteVinte;
    private Integer partitePerse;
    private Integer partitePareggiate;
    private Double mediaVoto;
}