package com.teleo.manager.prestation.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.teleo.manager.parametre.entities.Branche;
import com.teleo.manager.prestation.dto.request.RegistrantRequest;
import jakarta.persistence.Column;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "registrants")
public class Registrant extends BaseEntity<Registrant, RegistrantRequest> {

    private static final String ENTITY_NAME = "FOURNISSEUR";
    private static final String MODULE_NAME = "FOURNISSEUR_MODULE";

    @Column(nullable = false)
    private String numeroRegistrant;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "branche_id")
    private Branche branche;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    @Override
    public void update(Registrant source) {
        this.numeroRegistrant = source.getNumeroRegistrant();

        // Mise à jour des entités associées
        this.branche = source.getBranche();
        this.fournisseur = source.getFournisseur();
    }

    @Override
    public boolean equalsToDto(RegistrantRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples
        boolean areFieldsEqual = numeroRegistrant.equals(source.getNumeroRegistrant());

        // Comparaison des entités associées
        boolean isBrancheEqual = (branche == null && source.getBranche() == null) ||
                (branche != null && branche.getId().equals(source.getBranche()));

        boolean isFournisseurEqual = (fournisseur == null && source.getFournisseur() == null) ||
                (fournisseur != null && fournisseur.getId().equals(source.getFournisseur()));

        return areFieldsEqual && isBrancheEqual && isFournisseurEqual;
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
        return "Registrant{" +
                "id=" + getId() +
                ", numeroRegistrant='" + numeroRegistrant + '\'' +
                ", branche=" + (branche != null ? "Branche{id=" + branche.getId() + "}" : "null") +
                ", fournisseur=" + (fournisseur != null ? "Fournisseur{id=" + fournisseur.getId() + "}" : "null") +
        '}';
    }
}
