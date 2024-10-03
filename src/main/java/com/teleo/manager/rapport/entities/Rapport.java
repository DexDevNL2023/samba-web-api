package com.teleo.manager.rapport.entities;

import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.rapport.dto.request.RapportRequest;
import com.teleo.manager.rapport.enums.RapportType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rapports")
public class Rapport extends BaseEntity<Rapport, RapportRequest> {

    private static final String ENTITY_NAME = "RAPPORT";
    private static final String MODULE_NAME = "REPORTS_MODULE";

    @Column(nullable = false)
    private String titre;

    @Column(length = 5000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RapportType type;

    private LocalDate dateGeneration;

    @Column(nullable = false)
    private String url;

    @Override
    public void update(Rapport source) {
        this.titre = source.getTitre();
        this.description = source.getDescription();
        this.type = source.getType();
        this.dateGeneration = source.getDateGeneration();
        this.url = source.getUrl();
    }

    @Override
    public boolean equalsToDto(RapportRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples
        boolean areFieldsEqual = titre.equals(source.getTitre()) &&
                (description == null ? source.getDescription() == null : description.equals(source.getDescription())) &&
                type == source.getType() &&
                (dateGeneration == null ? source.getDateGeneration() == null : dateGeneration.equals(source.getDateGeneration())) &&
                url.equals(source.getUrl());

        return areFieldsEqual;
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
        return "Rapport{" +
                "id=" + getId() +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", dateGeneration=" + dateGeneration +
                ", url='" + url + '\'' +
        '}';
    }
}
