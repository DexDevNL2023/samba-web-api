package com.teleo.manager.assurance.entities;

import com.teleo.manager.assurance.dto.request.AssuranceRequest;
import com.teleo.manager.assurance.enums.InsuranceType;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "assurances")
public class Assurance extends BaseEntity<Assurance, AssuranceRequest> {

    private static final String ENTITY_NAME = "ASSURANCE";
    private static final String MODULE_NAME = "ASSURANCE_MODULE";

    @Column(nullable = false)
    private String nom;

    @Column(length = 5000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private InsuranceType type;

    @OneToMany(mappedBy = "assurance", fetch = FetchType.EAGER)
    private List<PoliceAssurance> polices = new ArrayList<>();

    @Override
    public void update(Assurance source) {
        this.nom = source.getNom();
        this.description = source.getDescription();
        this.type = source.getType();

        // Mise à jour de la liste des polices
        this.polices.clear();
        if (source.getPolices() != null) {
            this.polices.addAll(source.getPolices());
        }
    }

    @Override
    public boolean equalsToDto(AssuranceRequest source) {
        if (source == null) {
            return false;
        }

        // Comparer les champs simples
        boolean areFieldsEqual = nom.equals(source.getNom()) &&
                type.equals(source.getType()) &&
                description.equals(source.getDescription());

        // Comparer les polices (par exemple, en comparant la taille de la liste)
        if (source.getPolices() == null && this.polices != null) {
            return false;
        }

        boolean arePolicesEqual = (source.getPolices() == null && this.polices.isEmpty()) ||
                (source.getPolices() != null && source.getPolices().size() == this.polices.size());

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
        return "Assurance{" +
                "id=" + getId() +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", polices=" + polices.stream()
                .map(police -> "Police{id=" + police.getId() + "'}")
                .collect(Collectors.joining(", ")) +
        '}';
    }
}

