package com.giggi.dto.response.Partita;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PartitaFindSmallDTO {
    private Long id;
    private String luogo;
    private LocalDateTime dataOra;
}
