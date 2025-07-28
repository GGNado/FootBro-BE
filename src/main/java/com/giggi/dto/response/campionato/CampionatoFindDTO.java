package com.giggi.dto.response.campionato;

import com.giggi.dto.response.utente.UtenteFindDTO;
import com.giggi.dto.response.utente.UtenteFindDTOSmall;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CampionatoFindDTO {
    private Long id;
    private String nome;
    private String descrizione;
    private String codice;
    private UtenteFindDTOSmall creatore;
    // Altri campi
}