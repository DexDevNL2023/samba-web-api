package com.teleo.manager.sinistre.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.teleo.manager.assurance.entities.Souscription;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.paiement.entities.Paiement;
import com.teleo.manager.prestation.entities.Prestation;
import com.teleo.manager.sinistre.dto.request.ReclamationRequest;
import com.teleo.manager.sinistre.enums.StatutReclamation;
import com.teleo.manager.sinistre.enums.TypeReclamation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reclamations")
public class Reclamation extends BaseEntity<Reclamation, ReclamationRequest> {

    private static final String ENTITY_NAME = "RECLAMATION";
    private static final String MODULE_NAME = "RECLAMATION_MODULE";

    @Column(unique = true, nullable = false)
    private String numeroReclamation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeReclamation type;

    private LocalDate dateReclamation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutReclamation status;

    @Column(length = 5000)
    private String description;

    private BigDecimal montantReclame;
    private BigDecimal montantApprouve;

    private LocalDate dateEvaluation;

    private String agentEvaluateur;

    @Column(length = 5000)
    private String justification;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "souscription_id")
    private Souscription souscription;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sinistre_id")
    private Sinistre sinistre;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prestation_id")
    private Prestation prestation;

    @OneToMany(mappedBy = "reclamation", fetch = FetchType.EAGER)
    private List<Paiement> paiements = new ArrayList<>();

    @Override
    public void update(Reclamation source) {
        this.numeroReclamation = source.getNumeroReclamation();
        this.type = source.getType();
        this.dateReclamation = source.getDateReclamation();
        this.status = source.getStatus();
        this.description = source.getDescription();
        this.montantReclame = source.getMontantReclame();
        this.montantApprouve = source.getMontantApprouve();
        this.dateEvaluation = source.getDateEvaluation();
        this.agentEvaluateur = source.getAgentEvaluateur();
        this.justification = source.getJustification();

        // Mise à jour des entités associées
        this.souscription = source.getSouscription();
        this.sinistre = source.getSinistre();
        this.prestation = source.getPrestation();

        // Mise à jour des paiements
        this.paiements.clear();
        if (source.getPaiements() != null) {
            this.paiements.addAll(source.getPaiements());
        }
    }

    @Override
    public boolean equalsToDto(ReclamationRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples
        boolean areFieldsEqual = numeroReclamation.equals(source.getNumeroReclamation()) &&
                type == source.getType() &&
                (dateReclamation == null ? source.getDateReclamation() == null : dateReclamation.equals(source.getDateReclamation())) &&
                status == source.getStatus() &&
                (description == null ? source.getDescription() == null : description.equals(source.getDescription())) &&
                (montantReclame == null ? source.getMontantReclame() == null : montantReclame.equals(source.getMontantReclame())) &&
                (montantApprouve == null ? source.getMontantApprouve() == null : montantApprouve.equals(source.getMontantApprouve())) &&
                (dateEvaluation == null ? source.getDateEvaluation() == null : dateEvaluation.equals(source.getDateEvaluation())) &&
                (agentEvaluateur == null ? source.getAgentEvaluateur() == null : agentEvaluateur.equals(source.getAgentEvaluateur())) &&
                (justification == null ? source.getJustification() == null : justification.equals(source.getJustification()));

        // Comparaison des entités associées
        boolean isSouscriptionEqual = (souscription == null && source.getSouscription() == null) ||
                (souscription != null && souscription.getId().equals(source.getSouscription()));

        boolean isSinistreEqual = (sinistre == null && source.getSinistre() == null) ||
                (sinistre != null && sinistre.getId().equals(source.getSinistre()));

        boolean isPrestationEqual = (prestation == null && source.getPrestation() == null) ||
                (prestation != null && prestation.getId().equals(source.getPrestation()));

        // Comparaison des paiements (basée uniquement sur la taille ici)
        boolean arePaiementsEqual = (source.getPaiements() == null && paiements.isEmpty()) ||
                (source.getPaiements() != null && source.getPaiements().size() == paiements.size());

        return areFieldsEqual && isSouscriptionEqual && isSinistreEqual && isPrestationEqual && arePaiementsEqual;
    }

    @Override
    public String getEntityName() {
        return ENTITY_NAME;
    }

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + getId() +
                ", numeroReclamation='" + numeroReclamation + '\'' +
                ", type=" + type +
                ", dateReclamation=" + dateReclamation +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", montantReclame=" + montantReclame +
                ", montantApprouve=" + montantApprouve +
                ", dateEvaluation=" + dateEvaluation +
                ", agentEvaluateur='" + agentEvaluateur + '\'' +
                ", justification='" + justification + '\'' +
                ", souscription=" + (souscription != null ? "Souscription{id=" + souscription.getId() + '}' : "null") +
                ", sinistre=" + (sinistre != null ? "Sinistre{id=" + sinistre.getId() + '}' : "null") +
                ", prestation=" + (prestation != null ? "Prestation{id=" + prestation.getId() + '}' : "null") +
        '}';
    }
}
