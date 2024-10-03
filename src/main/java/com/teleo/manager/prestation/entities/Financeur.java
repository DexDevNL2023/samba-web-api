package com.teleo.manager.prestation.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.prestation.dto.request.FinanceurRequest;
import com.teleo.manager.prestation.enums.FinanceurType;
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
@Table(name = "financeurs")
public class Financeur extends BaseEntity<Financeur, FinanceurRequest> {

    private static final String ENTITY_NAME = "FINANCEUR";
    private static final String MODULE_NAME = "FINANCEUR_MODULE";

    @Column(nullable = false)
    private String nom;

    @Column(length = 5000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FinanceurType type;

    private String adresse;

    private String telephone;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany(mappedBy = "financeurs", fetch = FetchType.EAGER)
    private List<Prestation> prestations = new ArrayList<>();

    @Override
    public void update(Financeur source) {
        this.nom = source.getNom();
        this.description = source.getDescription();
        this.type = source.getType();
        this.adresse = source.getAdresse();
        this.telephone = source.getTelephone();
        this.email = source.getEmail();

        // Mise à jour des prestations
        this.prestations.clear();
        if (source.getPrestations() != null) {
            this.prestations.addAll(source.getPrestations());
        }
    }

    @Override
    public boolean equalsToDto(FinanceurRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples
        boolean areFieldsEqual = nom.equals(source.getNom()) &&
                (description == null ? source.getDescription() == null : description.equals(source.getDescription())) &&
                type.equals(source.getType()) &&
                (adresse == null ? source.getAdresse() == null : adresse.equals(source.getAdresse())) &&
                (telephone == null ? source.getTelephone() == null : telephone.equals(source.getTelephone())) &&
                email.equals(source.getEmail());

        // Comparaison des prestations (basée uniquement sur la taille ici)
        boolean arePrestationsEqual = (source.getPrestations() == null && prestations.isEmpty()) ||
                (source.getPrestations() != null && source.getPrestations().size() == prestations.size());

        return areFieldsEqual && arePrestationsEqual;
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
        return "Financeur{" +
                "id=" + getId() +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", adresse='" + adresse + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", prestations=" + prestations.stream()
                .map(prestation -> "Prestation{id=" + prestation.getId() + "'}")
                .collect(Collectors.joining(", ")) +
        '}';
    }
}
