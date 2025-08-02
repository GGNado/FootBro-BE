package com.giggi.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PartecipazioniCampionato")
public class PartecipazioneCampionato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campionato_id", nullable = false)
    private Campionato campionato;

    // Statistiche del partecipante nel campionato
    @Column(nullable = false)
    @Builder.Default
    private Integer partiteGiocate = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer partiteVinte = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer partitePerse = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer partitePareggiate = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer punti = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer golFatti = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer assist = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer golSubiti = 0;

    @Column(nullable = false)
    @Builder.Default
    private Double mediaVoto = 0.0;

    // Metadati
    @Column(nullable = false)
    private LocalDateTime dataIscrizione;

    @Column
    private LocalDateTime dataUltimaPartita;

    @Column(nullable = false)
    @Builder.Default
    private Boolean attivo = true;

    // Costruttore personalizzato per inizializzare la data di iscrizione
    @PrePersist
    public void prePersist() {
        if (dataIscrizione == null) {
            dataIscrizione = LocalDateTime.now();
        }
    }

    // Metodi di utilità per calcolare statistiche derivate
    public Double getPercentualeVittorie() {
        if (partiteGiocate == 0) return 0.0;
        return (partiteVinte.doubleValue() / partiteGiocate.doubleValue()) * 100;
    }

    public Integer getDifferenzaReti() {
        return golFatti - golSubiti;
    }

    public Double getMediaGolFatti() {
        if (partiteGiocate == 0) return 0.0;
        return golFatti.doubleValue() / partiteGiocate.doubleValue();
    }

    public Double getMediaGolSubiti() {
        if (partiteGiocate == 0) return 0.0;
        return golSubiti.doubleValue() / partiteGiocate.doubleValue();
    }
}