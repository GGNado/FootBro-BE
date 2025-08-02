package com.giggi.dto.request.partita;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Schema(description = "DTO per la creazione di una nuova partita")
public class PartitaCreateRequestDTO {
    @Schema(description = "Data e ora di inizio della partita", example = "2025-09-15T18:30:00", required = true)
    private LocalDateTime dataOra;

    @Schema(description = "Luogo dove si svolgerà la partita", example = "Campo Sportivo Roma Nord", required = true)
    private String luogo;
}