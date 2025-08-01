package com.giggi.dto.response.stats;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuickStatsResponseDTO {
    private Long partiteGiocateTotali;
    private Long goalFattiTotali;
    private Long assistTotali;
}
