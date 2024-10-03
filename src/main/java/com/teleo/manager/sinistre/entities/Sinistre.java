package com.teleo.manager.sinistre.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.teleo.manager.assurance.entities.Souscription;
import com.teleo.manager.document.entities.Document;
import com.teleo.manager.prestation.entities.Prestation;
import com.teleo.manager.sinistre.dto.request.SinistreRequest;
import com.teleo.manager.sinistre.enums.SinistreStatus;
import jakarta.persistence.Column;
import com.teleo.manager.generic.entity.audit.BaseEntity;
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
@Table(name = "sinistre")
public class Sinistre extends BaseEntity<Sinistre, SinistreRequest> {

    private static final String ENTITY_NAME = "SINISTRE";
    private static final String MODULE_NAME = "SINISTRE_MODULE";

    @Column(nullable = false, unique = true)
    private String numeroSinistre;

    private String label;

    @Column(length = 5000)
    private String raison;

    @Column(nullable = false)
    private LocalDate dateSurvenance;

    @Column(nullable = false)
    private LocalDate dateDeclaration;

    private LocalDate dateReglement;
    private LocalDate datePaiement;
    private LocalDate dateCloture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SinistreStatus status;

    private BigDecimal montantSinistre;
    private BigDecimal montantAssure;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "souscription_id")
    private Souscription souscription;

    @OneToMany(mappedBy = "sinistre", fetch = FetchType.EAGER)
    private List<Prestation> prestations = new ArrayList<>();

    @OneToMany(mappedBy = "sinistre", fetch = FetchType.EAGER)
    private List<Document> documents = new ArrayList<>();

    @Override
    public void update(Sinistre source) {
        this.numeroSinistre = source.getNumeroSinistre();
        this.label = source.getLabel();
        this.raison = source.getRaison();
        this.dateSurvenance = source.getDateSurvenance();
        this.dateDeclaration = source.getDateDeclaration();
        this.dateReglement = source.getDateReglement();
        this.datePaiement = source.getDatePaiement();
        this.dateCloture = source.getDateCloture();
        this.status = source.getStatus();
        this.montantSinistre = source.getMontantSinistre();
        this.montantAssure = source.getMontantAssure();

        // Mise à jour de la souscription
        this.souscription = source.getSouscription();

        // Mise à jour des prestations
        this.prestations.clear();
        if (source.getPrestations() != null) {
            this.prestations.addAll(source.getPrestations());
        }

        // Mise à jour des documents
        this.documents.clear();
        if (source.getDocuments() != null) {
            this.documents.addAll(source.getDocuments());
        }
    }

    @Override
    public boolean equalsToDto(SinistreRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples
        boolean areFieldsEqual = numeroSinistre.equals(source.getNumeroSinistre()) &&
                (label == null ? source.getLabel() == null : label.equals(source.getLabel())) &&
                (raison == null ? source.getRaison() == null : raison.equals(source.getRaison())) &&
                (dateSurvenance == null ? source.getDateSurvenance() == null : dateSurvenance.equals(source.getDateSurvenance())) &&
                (dateDeclaration == null ? source.getDateDeclaration() == null : dateDeclaration.equals(source.getDateDeclaration())) &&
                (dateReglement == null ? source.getDateReglement() == null : dateReglement.equals(source.getDateReglement())) &&
                (datePaiement == null ? source.getDatePaiement() == null : datePaiement.equals(source.getDatePaiement())) &&
                (dateCloture == null ? source.getDateCloture() == null : dateCloture.equals(source.getDateCloture())) &&
                status == source.getStatus() &&
                (montantSinistre == null ? source.getMontantSinistre() == null : montantSinistre.equals(source.getMontantSinistre())) &&
                (montantAssure == null ? source.getMontantAssure() == null : montantAssure.equals(source.getMontantAssure()));

        // Comparaison de la souscription
        boolean isSouscriptionEqual = (souscription == null && source.getSouscription() == null) ||
                (souscription != null && souscription.getId().equals(source.getSouscription()));

        // Comparaison des listes (basée uniquement sur la taille ici)
        boolean arePrestationsEqual = (source.getPrestations() == null && prestations.isEmpty()) ||
                (source.getPrestations() != null && source.getPrestations().size() == prestations.size());

        boolean areDocumentsEqual = (source.getDocuments() == null && documents.isEmpty()) ||
                (source.getDocuments() != null && source.getDocuments().size() == documents.size());

        return areFieldsEqual && isSouscriptionEqual && arePrestationsEqual && areDocumentsEqual;
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
        return "Sinistre{" +
                "id=" + getId() +
                ", numeroSinistre='" + numeroSinistre + '\'' +
                ", label='" + label + '\'' +
                ", raison='" + raison + '\'' +
                ", dateSurvenance=" + dateSurvenance +
                ", dateDeclaration=" + dateDeclaration +
                ", dateReglement=" + dateReglement +
                ", datePaiement=" + datePaiement +
                ", dateCloture=" + dateCloture +
                ", status=" + status +
                ", montantSinistre=" + montantSinistre +
                ", montantAssure=" + montantAssure +
                ", souscription=" + (souscription != null ? souscription.getId() : "null") +
                ", prestations=" + prestations.stream()
                .map(prestation -> "Prestation{id=" + prestation.getId() + "'}")
                .collect(Collectors.joining(", ")) +
                ", documents=" + documents.stream()
                .map(document -> "Document{id=" + document.getId() + "'}")
                .collect(Collectors.joining(", ")) +
        '}';
    }
}
