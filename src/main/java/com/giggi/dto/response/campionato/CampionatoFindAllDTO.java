package com.giggi.dto.response.campionato;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CampionatoFindAllDTO {
    private List<CampionatoFindDTO> CampionatoFindAllDTO;
}