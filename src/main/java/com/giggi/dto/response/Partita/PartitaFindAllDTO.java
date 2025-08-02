package com.giggi.dto.response.Partita;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PartitaFindAllDTO {
    private List<PartitaFindDTO> PartitaFindAllDTO;
}