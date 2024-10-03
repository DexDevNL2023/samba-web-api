package com.teleo.manager.assurance.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.assurance.dto.request.PoliceAssuranceRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "polices_assurances")
public class PoliceAssurance extends BaseEntity<PoliceAssurance, PoliceAssuranceRequest> {

    private static final String ENTITY_NAME = "POLICE D'ASSURANCE";
    private static final String MODULE_NAME = "POLICE_ASSURANCE_MODULE";

    @Column(nullable = false, unique = true)
    private String numeroPolice;

    private String imageUrl;

    @Column(nullable = false)
    private String label;

    private Integer dureeCouverture;

    @Column(length = 5000)
    private String conditions;

    @Column(nullable = false)
    private BigDecimal montantSouscription;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assurance_id")
    private Assurance assurance;

    // Relation ManyToMany bidirectionnelle avec Garantie
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "police_garanties",
            joinColumns = @JoinColumn(name = "police_id"),
            inverseJoinColumns = @JoinColumn(name = "garantie_id"))
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Garantie> garanties = new ArrayList<>();

    @OneToMany(mappedBy = "police", fetch = FetchType.EAGER)
    private List<Souscription> souscriptions = new ArrayList<>();

    @Override
    public void update(PoliceAssurance source) {
        this.numeroPolice = source.getNumeroPolice();
        this.label = source.getLabel();
        this.dureeCouverture = source.getDureeCouverture();
        this.conditions = source.getConditions();
        this.montantSouscription = source.getMontantSouscription();

        // Mise à jour de l'entité assurance associée
        this.assurance = source.getAssurance();

        // Mise à jour des garanties associées
        this.garanties.clear();
        if (source.getGaranties() != null) {
            this.garanties.addAll(source.getGaranties());
        }

        // Mise à jour des souscriptions associées
        this.souscriptions.clear();
        if (source.getSouscriptions() != null) {
            this.souscriptions.addAll(source.getSouscriptions());
        }
    }

    @Override
    public boolean equalsToDto(PoliceAssuranceRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples
        boolean areFieldsEqual = numeroPolice.equals(source.getNumeroPolice()) &&
                label.equals(source.getLabel()) &&
                (dureeCouverture == null ? source.getDureeCouverture() == null : dureeCouverture.equals(source.getDureeCouverture())) &&
                (conditions == null ? source.getConditions() == null : conditions.equals(source.getConditions())) &&
                montantSouscription.equals(source.getMontantSouscription());

        // Comparaison de l'entité assurance
        boolean isAssuranceEqual = (assurance == null && source.getAssurance() == null) ||
                (assurance != null && assurance.getId().equals(source.getAssurance()));

        // Comparaison des garanties (basée uniquement sur la taille ici)
        boolean areGarantiesEqual = (source.getGaranties() == null && garanties.isEmpty()) ||
                (source.getGaranties() != null && source.getGaranties().size() == garanties.size());

        // Comparaison des souscriptions (basée uniquement sur la taille ici)
        boolean areSouscriptionsEqual = (source.getSouscriptions() == null && souscriptions.isEmpty()) ||
                (source.getSouscriptions() != null && source.getSouscriptions().size() == souscriptions.size());

        return areFieldsEqual && isAssuranceEqual && areGarantiesEqual && areSouscriptionsEqual;
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
        return "PoliceAssurance{" +
                "id=" + getId() +
                ", numeroPolice='" + numeroPolice + '\'' +
                ", label='" + label + '\'' +
                ", dureeCouverture='" + dureeCouverture + '\'' +
                ", montantSouscription=" + montantSouscription +
                ", assurance=" + (assurance != null ? assurance.getId() : null) +
                ", garanties=" + garanties.stream()
                .map(garantie -> garantie.getId())
                .collect(Collectors.toList()) +
                ", souscriptions=" + souscriptions.stream()
                .map(souscription -> souscription.getId())
                .collect(Collectors.toList()) +
        '}';
    }
}
