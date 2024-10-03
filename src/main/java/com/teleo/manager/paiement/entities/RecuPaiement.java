package com.teleo.manager.paiement.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.paiement.dto.request.RecuPaiementRequest;
import com.teleo.manager.paiement.enums.RecuPaymentType;
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
@Table(name = "recu_paiements")
public class RecuPaiement extends BaseEntity<RecuPaiement, RecuPaiementRequest> {

    private static final String ENTITY_NAME = "RECU DE PAIEMENT";
    private static final String MODULE_NAME = "RECU_PAIEMENT_MODULE";

    @Column(nullable = false, unique = true)
    private String numeroRecu;

    @Column(nullable = false)
    private LocalDate dateEmission;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecuPaymentType type;

    @Column(nullable = false)
    private BigDecimal montant;

    @Column(length = 5000)
    private String details;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paiement_id")
    private Paiement paiement;

    @Override
    public void update(RecuPaiement source) {
        this.numeroRecu = source.getNumeroRecu();
        this.dateEmission = source.getDateEmission();
        this.montant = source.getMontant();
        this.details = source.getDetails();

        // Mise à jour de l'entité associée
        this.paiement = source.getPaiement();
    }

    @Override
    public boolean equalsToDto(RecuPaiementRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples
        boolean areFieldsEqual = numeroRecu.equals(source.getNumeroRecu()) &&
                dateEmission.equals(source.getDateEmission()) &&
                montant.equals(source.getMontant()) &&
                (details == null ? source.getDetails() == null : details.equals(source.getDetails()));

        // Comparaison de l'entité associée
        boolean isPaiementEqual = (paiement == null && source.getPaiement() == null) ||
                (paiement != null && paiement.getId().equals(source.getPaiement()));

        return areFieldsEqual && isPaiementEqual;
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
        return "RecuPaiement{" +
                "id=" + getId() +
                ", numeroRecu='" + numeroRecu + '\'' +
                ", dateEmission=" + dateEmission +
                ", montant=" + montant +
                ", details='" + details + '\'' +
        '}';
    }
}
