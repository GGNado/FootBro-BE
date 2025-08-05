package com.giggi.dto.request.partita;

import com.giggi.entity.enums.Squadra;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SalvaSquadraRequestDTO {
    private List<Long> idUtenti;
    private Squadra squadra;
}
