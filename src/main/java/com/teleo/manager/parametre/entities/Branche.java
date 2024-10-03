package com.teleo.manager.parametre.entities;

import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.parametre.dto.request.BrancheRequest;
import com.teleo.manager.prestation.entities.Registrant;
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
@Table(name = "branches")
public class Branche extends BaseEntity<Branche, BrancheRequest> {

    private static final String ENTITY_NAME = "BRANCHE";
    private static final String MODULE_NAME = "BRANCHE_MODULE";

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String ville;

    @Column(name = "is_defaut")
    private Boolean isDefaut;

    @OneToMany(mappedBy = "branche", fetch = FetchType.EAGER)
    private List<Registrant> registrants = new ArrayList<>();

    @Override
    public void update(Branche source) {
        this.code = source.getCode();
        this.ville = source.getVille();
        this.isDefaut = source.getIsDefaut();

        // Mise à jour de la liste des registrants
        this.registrants.clear();
        if (source.getRegistrants() != null) {
            this.registrants.addAll(source.getRegistrants());
        }
    }

    @Override
    public boolean equalsToDto(BrancheRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples
        boolean areFieldsEqual = code.equals(source.getCode()) &&
                ville.equals(source.getVille()) &&
                (isDefaut == null ? source.getIsDefaut() == null : isDefaut.equals(source.getIsDefaut()));

        // Comparaison de la liste des registrants (basée uniquement sur la taille ici)
        boolean areRegistrantsEqual = (source.getRegistrants() == null && registrants.isEmpty()) ||
                (source.getRegistrants() != null && source.getRegistrants().size() == registrants.size());

        return areFieldsEqual && areRegistrantsEqual;
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
        return "Branche{" +
                "id=" + getId() +
                ", code='" + code + '\'' +
                ", ville='" + ville + '\'' +
                ", isDefaut=" + isDefaut +
                ", registrants=" + registrants.stream()
                .map(registrant -> "Registrant{id=" + registrant.getId() + "'}")
                .collect(Collectors.joining(", ")) +
        '}';
    }
}

