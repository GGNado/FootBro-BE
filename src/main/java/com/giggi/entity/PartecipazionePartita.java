package com.giggi.entity;

import com.giggi.entity.enums.RuoliPartita;
import com.giggi.entity.enums.Squadra;
import lombok.*;
import jakarta.persistence.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PartecipazioniPartita")
public class PartecipazionePartita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partita_id", nullable = false)
    private Partita partita;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Squadra squadra;

    @Enumerated(EnumType.STRING)
    private RuoliPartita ruolo;

    // Statistiche individuali della partita
    @Column
    @Builder.Default
    private Integer golSegnati = 0;

    @Column
    @Builder.Default
    private Integer assist = 0;

    @Column(nullable = false)
    @Builder.Default
    private Double voto = 6.0;

    @Column
    @Builder.Default
    private Boolean presente = true;
}