package com.giggi.dto.response.PartecipazioneCampionato;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PartecipazioneCampionatoFindAllDTO {
    private List<PartecipazioneCampionatoFindDTO> PartecipazioneCampionatoFindAllDTO;
}