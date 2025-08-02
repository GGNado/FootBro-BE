package com.giggi.dto.response.campionato;

import com.giggi.dto.response.PartecipazioneCampionato.PartecipazioneCampionatoFindDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ClassificaResponse {
    List<PartecipazioneCampionatoFindDTO> partecipazioni;
}
