package com.giggi.dto.request.campionato;

import com.giggi.entity.enums.TipologiaCampionato;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CampionatoCreateRequestDTO {
    private String nome;
    private String descrizione;
    private Long idUtente;
    private TipologiaCampionato tipologiaCampionato;

}