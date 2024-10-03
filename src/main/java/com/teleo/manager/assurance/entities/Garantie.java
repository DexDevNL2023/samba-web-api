package com.teleo.manager.assurance.entities;

import com.teleo.manager.assurance.dto.request.GarantieRequest;
import com.teleo.manager.assurance.enums.GarantieStatus;
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
@Table(name = "garanties")
public class Garantie extends BaseEntity<Garantie, GarantieRequest> {

    private static final String ENTITY_NAME = "GARANTIE";
    private static final String MODULE_NAME = "GARANTIE_MODULE";

    @Column(nullable = false, unique = true)
    private String numeroGarantie;

    @Column(nullable = false)
    private String label;

    @Column(nullable = false)
    private Double percentage;

    @Column(length = 5000)
    private String termes;

    @Column(nullable = false)
    private BigDecimal plafondAssure;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GarantieStatus status;

    // Relation ManyToMany bidirectionnelle avec Police d'assurance
    @ManyToMany(mappedBy = "garanties", fetch = FetchType.LAZY)
    private List<PoliceAssurance> polices = new ArrayList<>();

    @Override
    public void update(Garantie source) {
        this.numeroGarantie = source.getNumeroGarantie();
        this.label = source.getLabel();
        this.percentage = source.getPercentage();
        this.termes = source.getTermes();
        this.plafondAssure = source.getPlafondAssure();
        this.dateDebut = source.getDateDebut();
        this.dateFin = source.getDateFin();
        this.status = source.getStatus();

        // Mise à jour des polices d'assurance associées
        this.polices.clear();
        if (source.getPolices() != null) {
            this.polices.addAll(source.getPolices());
        }
    }

    @Override
    public boolean equalsToDto(GarantieRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples
        boolean areFieldsEqual = numeroGarantie.equals(source.getNumeroGarantie()) &&
                label.equals(source.getLabel()) &&
                percentage.equals(source.getPercentage()) &&
                (termes == null ? source.getTermes() == null : termes.equals(source.getTermes())) &&
                plafondAssure.equals(source.getPlafondAssure()) &&
                (dateDebut == null ? source.getDateDebut() == null : dateDebut.equals(source.getDateDebut())) &&
                (dateFin == null ? source.getDateFin() == null : dateFin.equals(source.getDateFin())) &&
                status.equals(source.getStatus());

        // Comparaison des listes de polices (basée uniquement sur la taille ici)
        boolean arePolicesEqual = (source.getPolices() == null && polices.isEmpty()) ||
                (source.getPolices() != null && source.getPolices().size() == polices.size());

        return areFieldsEqual && arePolicesEqual;
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
        return "Garantie{" +
                "id=" + getId() +
                ", numeroGarantie='" + numeroGarantie + '\'' +
                ", label='" + label + '\'' +
                ", percentage=" + percentage +
                ", termes='" + termes + '\'' +
                ", plafondAssure=" + plafondAssure +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", status=" + status +
                ", polices=" + polices.stream()
                .map(police -> "Police{id=" + police.getId() + "'}")
                .collect(Collectors.joining(", ")) +
        '}';
    }
}
