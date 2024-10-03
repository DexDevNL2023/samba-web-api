package com.teleo.manager.prestation.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.teleo.manager.assurance.entities.Souscription;
import com.teleo.manager.document.entities.Document;
import com.teleo.manager.prestation.dto.request.PrestationRequest;
import com.teleo.manager.prestation.enums.PrestationStatus;
import com.teleo.manager.prestation.enums.PrestationType;
import com.teleo.manager.sinistre.entities.Sinistre;
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
@Table(name = "prestations")
public class Prestation extends BaseEntity<Prestation, PrestationRequest> {

    private static final String ENTITY_NAME = "PRESTATION";
    private static final String MODULE_NAME = "PRESTATION_MODULE";

    @Column(nullable = false, unique = true)
    private String numeroPrestation;

    @Column(nullable = false)
    private String label;

    @Column(nullable = false)
    private LocalDate datePrestation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrestationType type;

    @Column(length = 5000)
    private String description;

    @Column(nullable = false)
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrestationStatus status;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "souscription_id")
    private Souscription souscription;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sinistre_id")
    private Sinistre sinistre;

    // Relation ManyToMany bidirectionnelle avec Financeur
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "prestation_financeurs",
            joinColumns = @JoinColumn(name = "prestation_id"),
            inverseJoinColumns = @JoinColumn(name = "financeur_id")
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Financeur> financeurs = new ArrayList<>();

    @OneToMany(mappedBy = "prestation", fetch = FetchType.EAGER)
    private List<Document> documents = new ArrayList<>();

    @Override
    public void update(Prestation source) {
        this.numeroPrestation = source.getNumeroPrestation();
        this.label = source.getLabel();
        this.datePrestation = source.getDatePrestation();
        this.type = source.getType();
        this.description = source.getDescription();
        this.montant = source.getMontant();
        this.status = source.getStatus();

        // Mise à jour des entités associées
        this.fournisseur = source.getFournisseur();
        this.souscription = source.getSouscription();
        this.sinistre = source.getSinistre();

        // Mise à jour des financeurs
        this.financeurs.clear();
        if (source.getFinanceurs() != null) {
            this.financeurs.addAll(source.getFinanceurs());
        }

        // Mise à jour des documents
        this.documents.clear();
        if (source.getDocuments() != null) {
            this.documents.addAll(source.getDocuments());
        }
    }

    @Override
    public boolean equalsToDto(PrestationRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples
        boolean areFieldsEqual = numeroPrestation.equals(source.getNumeroPrestation()) &&
                label.equals(source.getLabel()) &&
                datePrestation.equals(source.getDatePrestation()) &&
                type.equals(source.getType()) &&
                (description == null ? source.getDescription() == null : description.equals(source.getDescription())) &&
                montant.equals(source.getMontant()) &&
                status.equals(source.getStatus());

        // Comparaison des entités associées
        boolean isFournisseurEqual = (fournisseur == null && source.getFournisseur() == null) ||
                (fournisseur != null && fournisseur.getId().equals(source.getFournisseur()));

        boolean isSouscriptionEqual = (souscription == null && source.getSouscription() == null) ||
                (souscription != null && souscription.getId().equals(source.getSouscription()));

        boolean isSinistreEqual = (sinistre == null && source.getSinistre() == null) ||
                (sinistre != null && sinistre.getId().equals(source.getSinistre()));

        // Comparaison des listes (basée uniquement sur la taille ici)
        boolean areFinanceursEqual = (source.getFinanceurs() == null && financeurs.isEmpty()) ||
                (source.getFinanceurs() != null && source.getFinanceurs().size() == financeurs.size());

        boolean areDocumentsEqual = (source.getDocuments() == null && documents.isEmpty()) ||
                (source.getDocuments() != null && source.getDocuments().size() == documents.size());

        return areFieldsEqual && isFournisseurEqual && isSouscriptionEqual && isSinistreEqual && areFinanceursEqual && areDocumentsEqual;
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
        return "Prestation{" +
                "id=" + getId() +
                ", numeroPrestation='" + numeroPrestation + '\'' +
                ", label='" + label + '\'' +
                ", datePrestation=" + datePrestation +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", montant=" + montant +
                ", status=" + status +
                ", fournisseur=" + (fournisseur != null ? fournisseur.getId() : "null") +
                ", sinistre=" + (sinistre != null ? sinistre.getId() : "null") +
                ", financeurs=" + financeurs.stream()
                .map(financeur -> "Financeur{id=" + financeur.getId() + "'}")
                .collect(Collectors.joining(", ")) +
                ", documents=" + documents.stream()
                .map(document -> "document{id=" + document.getId() + "'}")
                .collect(Collectors.joining(", ")) +
        '}';
    }
}
