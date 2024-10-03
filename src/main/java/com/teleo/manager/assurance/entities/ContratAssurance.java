package com.teleo.manager.assurance.entities;

import com.teleo.manager.assurance.dto.request.ContratAssuranceRequest;
import com.teleo.manager.assurance.enums.ContratType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contrats_assurance")
public class ContratAssurance extends BaseEntity<ContratAssurance, ContratAssuranceRequest> {

    private static final String ENTITY_NAME = "CONTRAT ASSURANCE";
    private static final String MODULE_NAME = "CONTRAT_MODULE";

    @Column(nullable = false, unique = true)
    private String numeroContrat;

    @Column(nullable = false)
    private LocalDate dateContrat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContratType typeContrat;

    @Column(nullable = false, length = 5000)
    private String couverture;

    private BigDecimal franchise = BigDecimal.ZERO;

    @Column(nullable = false, length = 5000)
    private String conditions;

    @Column(length = 5000)
    private String exclusions = "";

    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalDate dateFin;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "souscription_id")
    private Souscription souscription;

    @Override
    public void update(ContratAssurance source) {
        this.numeroContrat = source.getNumeroContrat();
        this.dateContrat = source.getDateContrat();
        this.typeContrat = source.getTypeContrat();
        this.couverture = source.getCouverture();
        this.franchise = source.getFranchise();
        this.conditions = source.getConditions();
        this.exclusions = source.getExclusions();
        this.dateDebut = source.getDateDebut();
        this.dateFin = source.getDateFin();

        // Mise à jour de la souscription associée
        this.souscription = source.getSouscription();
    }

    @Override
    public boolean equalsToDto(ContratAssuranceRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples
        boolean areFieldsEqual = numeroContrat.equals(source.getNumeroContrat()) &&
                dateContrat.equals(source.getDateContrat()) &&
                typeContrat.equals(source.getTypeContrat()) &&
                couverture.equals(source.getCouverture()) &&
                (franchise == null ? source.getFranchise() == null : franchise.equals(source.getFranchise())) &&
                conditions.equals(source.getConditions()) &&
                (exclusions == null ? source.getExclusions() == null : exclusions.equals(source.getExclusions())) &&
                dateDebut.equals(source.getDateDebut()) &&
                dateFin.equals(source.getDateFin());

        // Comparaison de l'entité associée (Souscription)
        boolean isSouscriptionEqual = (souscription == null && source.getSouscription() == null) ||
                (souscription != null && souscription.getId().equals(source.getSouscription()));

        return areFieldsEqual && isSouscriptionEqual;
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
        return "ContratAssurance{" +
                "id=" + getId() +
                ", numeroContrat='" + numeroContrat + '\'' +
                ", dateContrat=" + dateContrat +
                ", typeContrat=" + typeContrat +
                ", couverture='" + couverture + '\'' +
                ", franchise=" + franchise +
                ", conditions='" + conditions + '\'' +
                ", exclusions='" + exclusions + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", souscription=" + (souscription != null ? "Souscription{id=" + souscription.getId() + "}" : "null") +
        '}';
    }
}
