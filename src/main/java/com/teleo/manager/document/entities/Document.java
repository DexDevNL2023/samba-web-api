package com.teleo.manager.document.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.teleo.manager.document.dto.request.DocumentRequest;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.prestation.entities.Prestation;
import com.teleo.manager.sinistre.entities.Sinistre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "documents")
public class Document extends BaseEntity<Document, DocumentRequest> {

    private static final String ENTITY_NAME = "DOCUMENT";

    private static final String MODULE_NAME = "DOCUMENT_MODULE";

    @Column(nullable = false, unique = true)
    private String numeroDocument;

    @Column(nullable = false)
    private String nom;

    @Column(length = 5000)
    private String description;

    @Column(nullable = false)
    private String url;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sinistre_id")
    private Sinistre sinistre;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prestation_id")
    private Prestation prestation;

    @Override
    public void update(Document source) {
        this.numeroDocument = source.getNumeroDocument();
        this.nom = source.getNom();
        this.description = source.getDescription();
        this.url = source.getUrl();

        // Mise à jour des entités associées
        this.sinistre = source.getSinistre();
        this.prestation = source.getPrestation();
    }

    @Override
    public boolean equalsToDto(DocumentRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples
        boolean areFieldsEqual = numeroDocument.equals(source.getNumeroDocument()) &&
                nom.equals(source.getNom()) &&
                ((description == null && source.getDescription() == null) ||
                        (description != null && description.equals(source.getDescription()))) &&
                url.equals(source.getUrl());

        // Comparaison des entités associées
        boolean isSinistreEqual = (sinistre == null && source.getSinistre() == null) ||
                (sinistre != null && sinistre.getId().equals(source.getSinistre()));

        boolean isPrestationEqual = (prestation == null && source.getPrestation() == null) ||
                (prestation != null && prestation.getId().equals(source.getPrestation()));

        return areFieldsEqual && isSinistreEqual && isPrestationEqual;
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
        return "Document{" +
                "id=" + getId() +
                ", numeroDocument='" + numeroDocument + '\'' +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", sinistre=" + sinistre.getId() +
                ", prestation=" + prestation.getId() +
                '}';
    }
}
