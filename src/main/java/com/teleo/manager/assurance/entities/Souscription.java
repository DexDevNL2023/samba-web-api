package com.teleo.manager.assurance.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.teleo.manager.assurance.dto.request.SouscriptionRequest;
import com.teleo.manager.assurance.enums.PaymentFrequency;
import com.teleo.manager.assurance.enums.SouscriptionStatus;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.paiement.entities.Paiement;
import com.teleo.manager.prestation.entities.Prestation;
import com.teleo.manager.sinistre.entities.Reclamation;
import com.teleo.manager.sinistre.entities.Sinistre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "souscriptions")
public class Souscription extends BaseEntity<Souscription, SouscriptionRequest> {

    private static final String ENTITY_NAME = "SOUSCRIPTION";
    private static final String MODULE_NAME = "SOUSCRIPTION_MODULE";

    @Column(nullable = false, unique = true)
    private String numeroSouscription;

    @Column(nullable = false)
    private LocalDate dateSouscription;

    private LocalDate dateExpiration;
    private BigDecimal montantCotisation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SouscriptionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentFrequency frequencePaiement;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assure_id")
    private Assure assure;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "police_id")
    private PoliceAssurance police;

    @OneToMany(mappedBy = "souscription", fetch = FetchType.EAGER)
    private List<ContratAssurance> contrats = new ArrayList<>();

    @OneToMany(mappedBy = "souscription", fetch = FetchType.EAGER)
    private List<Paiement> paiements = new ArrayList<>();

    @OneToMany(mappedBy = "souscription", fetch = FetchType.EAGER)
    private List<Sinistre> sinistres = new ArrayList<>();

    @OneToMany(mappedBy = "souscription", fetch = FetchType.EAGER)
    private List<Reclamation> reclamations = new ArrayList<>();

    @OneToMany(mappedBy = "souscription", fetch = FetchType.EAGER)
    private List<Prestation> prestations = new ArrayList<>();

    // Override update method
    @Override
    public void update(Souscription source) {
        this.numeroSouscription = source.getNumeroSouscription();
        this.dateSouscription = source.getDateSouscription();
        this.dateExpiration = source.getDateExpiration();
        this.montantCotisation = source.getMontantCotisation();
        this.status = source.getStatus();
        this.frequencePaiement = source.getFrequencePaiement();

        // Mise à jour des entités associées
        this.assure = source.getAssure();
        this.police = source.getPolice();

        // Mise à jour des contrats d'assurance
        this.contrats.clear();
        if (source.getContrats() != null) {
            this.contrats.addAll(source.getContrats());
        }

        // Mise à jour des paiements
        this.paiements.clear();
        if (source.getPaiements() != null) {
            this.paiements.addAll(source.getPaiements());
        }

        // Mise à jour des sinistres
        this.sinistres.clear();
        if (source.getSinistres() != null) {
            this.sinistres.addAll(source.getSinistres());
        }

        // Mise à jour des réclamations
        this.reclamations.clear();
        if (source.getReclamations() != null) {
            this.reclamations.addAll(source.getReclamations());
        }

        // Mise à jour des prestations
        this.prestations.clear();
        if (source.getPrestations() != null) {
            this.prestations.addAll(source.getPrestations());
        }
    }

    @Override
    public boolean equalsToDto(SouscriptionRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples
        boolean areFieldsEqual = numeroSouscription.equals(source.getNumeroSouscription()) &&
                dateSouscription.equals(source.getDateSouscription()) &&
                (dateExpiration == null ? source.getDateExpiration() == null : dateExpiration.equals(source.getDateExpiration())) &&
                montantCotisation.equals(source.getMontantCotisation()) &&
                status.equals(source.getStatus()) &&
                frequencePaiement.equals(source.getFrequencePaiement());

        // Comparaison des entités associées
        boolean isAssureEqual = (assure == null && source.getAssure() == null) ||
                (assure != null && assure.getId().equals(source.getAssure()));

        boolean isPoliceEqual = (police == null && source.getPolice() == null) ||
                (police != null && police.getId().equals(source.getPolice()));

        // Comparaison des contrats d'assurance (basée uniquement sur la taille ici)
        boolean areContratsEqual = (source.getContrats() == null && contrats.isEmpty()) ||
                (source.getContrats() != null && source.getContrats().size() == contrats.size());

        // Comparaison des paiements (basée uniquement sur la taille ici)
        boolean arePaiementsEqual = (source.getPaiements() == null && paiements.isEmpty()) ||
                (source.getPaiements() != null && source.getPaiements().size() == paiements.size());

        // Comparaison des sinistres (basée uniquement sur la taille ici)
        boolean areSinistresEqual = (source.getSinistres() == null && sinistres.isEmpty()) ||
                (source.getSinistres() != null && source.getSinistres().size() == sinistres.size());

        // Comparaison des réclamations (basée uniquement sur la taille ici)
        boolean areReclamationsEqual = (source.getReclamations() == null && reclamations.isEmpty()) ||
                (source.getReclamations() != null && source.getReclamations().size() == reclamations.size());

        // Comparaison des prestations (basée uniquement sur la taille ici)
        boolean arePrestationsEqual = (source.getPrestations() == null && prestations.isEmpty()) ||
                (source.getPrestations() != null && source.getPrestations().size() == prestations.size());

        return areFieldsEqual && isAssureEqual && isPoliceEqual && areContratsEqual &&
                arePaiementsEqual && areSinistresEqual && areReclamationsEqual && arePrestationsEqual;
    }

    @Override
    public String getEntityName() {
        return ENTITY_NAME;
    }

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    // Custom toString method
    @Override
    public String toString() {
        return "Souscription{" +
                "id=" + getId() +
                ", numeroSouscription='" + numeroSouscription + '\'' +
                ", dateSouscription=" + dateSouscription +
                ", dateExpiration=" + dateExpiration +
                ", montantCotisation=" + montantCotisation +
                ", status=" + status +
                ", frequencePaiement=" + frequencePaiement +
                ", assure=" + (assure != null ? assure.getId() : "null") +
                ", police=" + (police != null ? police.getId() : "null") +
                ", contrats=" + contrats.stream()
                .map(contrat -> "Contrat{id=" + contrat.getId() + "'}")
                .collect(Collectors.joining(", ")) +
                ", paiements=" + paiements.stream()
                .map(paiement -> "Paiement{id=" + paiement.getId() + "'}")
                .collect(Collectors.joining(", ")) +
                ", sinistres=" + sinistres.stream()
                .map(sinistre -> "Sinistre{id=" + sinistre.getId() + "'}")
                .collect(Collectors.joining(", ")) +
                ", reclamations=" + reclamations.stream()
                .map(reclamation -> "Reclamation{id=" + reclamation.getId() + "'}")
                .collect(Collectors.joining(", ")) +
                ", prestations=" + prestations.stream()
                .map(prestation -> "Prestation{id=" + prestation.getId() + "'}")
                .collect(Collectors.joining(", ")) +
        '}';
    }
}
