package com.giggi.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Campionati")
public class Campionato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private String descrizione;

    @ManyToOne(optional = false)
    @JoinColumn(name = "creatore_id", nullable = false)
    private Utente creatore;

    @Column(nullable = false, unique = true)
    private String codice;

    @OneToMany(mappedBy = "campionato", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PartecipazioneCampionato> partecipazioni = new HashSet<>();

    // Metodi di utilità per gestire le partecipazioni
    public void aggiungiPartecipante(Utente utente) {
        PartecipazioneCampionato partecipazione = PartecipazioneCampionato.builder()
                .utente(utente)
                .campionato(this)
                .build();
        partecipazioni.add(partecipazione);
    }

    public void rimuoviPartecipante(Utente utente) {
        partecipazioni.removeIf(p -> p.getUtente().equals(utente));
        utente.getPartecipazioni().removeIf(p -> p.getCampionato().equals(this));
    }

    public Set<Utente> getPartecipanti() {
        return partecipazioni.stream()
                .filter(PartecipazioneCampionato::getAttivo)
                .map(PartecipazioneCampionato::getUtente)
                .collect(java.util.stream.Collectors.toSet());
    }

    public Integer getNumeroPartecipanti() {
        return (int) partecipazioni.stream()
                .filter(PartecipazioneCampionato::getAttivo)
                .count();
    }

    public String generaCodiceUnivoco() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}