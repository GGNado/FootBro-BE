package com.giggi.entity;

import com.giggi.entity.enums.Squadra;
import com.giggi.entity.enums.StatoPartita;
import com.giggi.entity.enums.TipologiaCampionato;
import lombok.*;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Partite")
public class Partita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campionato_id", nullable = false)
    private Campionato campionato;

    @Column(nullable = false)
    private LocalDateTime dataOra;

    @Column(nullable = false)
    private String luogo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatoPartita stato = StatoPartita.PROGRAMMATA;

    @Column
    private Integer golSquadraA;

    @Column
    private Integer golSquadraB;

    // Relazioni con le squadre (set di utenti)
    @OneToMany(mappedBy = "partita", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<PartecipazionePartita> partecipazioni = new HashSet<>();

    // Metodi di utilità
    public Set<Utente> getSquadraA() {
        return partecipazioni.stream()
                .filter(p -> "A".equals(p.getSquadra()))
                .map(PartecipazionePartita::getUtente)
                .collect(java.util.stream.Collectors.toSet());
    }

    public Set<Utente> getSquadraB() {
        return partecipazioni.stream()
                .filter(p -> "B".equals(p.getSquadra()))
                .map(PartecipazionePartita::getUtente)
                .collect(java.util.stream.Collectors.toSet());
    }

    public void aggiungiGiocatoreSquadraA(Utente utente) {
        PartecipazionePartita partecipazione = PartecipazionePartita.builder()
                .utente(utente)
                .partita(this)
                .squadra(Squadra.A)
                .build();
        partecipazioni.add(partecipazione);
    }

    public void aggiungiGiocatoreSquadraB(Utente utente) {
        PartecipazionePartita partecipazione = PartecipazionePartita.builder()
                .utente(utente)
                .partita(this)
                .squadra(Squadra.B)
                .build();
        partecipazioni.add(partecipazione);
    }



}