package com.teleo.manager.paiement.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.teleo.manager.assurance.entities.Souscription;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.paiement.dto.request.PaiementRequest;
import com.teleo.manager.paiement.enums.PaymentMode;
import com.teleo.manager.paiement.enums.PaymentType;
import com.teleo.manager.sinistre.entities.Reclamation;
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
@Table(name = "paiements")
public class Paiement extends BaseEntity<Paiement, PaiementRequest> {

    private static final String ENTITY_NAME = "PAIEMENT";
    private static final String MODULE_NAME = "PAIEMENT_MODULE";

    @Column(nullable = false)
    private String numeroPaiement;

    @Column(nullable = false)
    private LocalDate datePaiement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMode mode;

    @Column(nullable = false)
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType type;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "souscription_id")
    private Souscription souscription;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reclamation_id")
    private Reclamation reclamation;

    @OneToMany(mappedBy = "paiement", fetch = FetchType.EAGER)
    private List<RecuPaiement> recuPaiements = new ArrayList<>();


    @Override
    public void update(Paiement source) {
        this.numeroPaiement = source.getNumeroPaiement();
        this.datePaiement = source.getDatePaiement();
        this.mode = source.getMode();
        this.montant = source.getMontant();
        this.type = source.getType();

        // Mise à jour des entités associées
        this.souscription = source.getSouscription();
        this.reclamation = source.getReclamation();

        // Mise à jour des reçus de paiement
        this.recuPaiements.clear();
        if (source.getRecuPaiements() != null) {
            this.recuPaiements.addAll(source.getRecuPaiements());
        }
    }

    @Override
    public boolean equalsToDto(PaiementRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples
        boolean areFieldsEqual = numeroPaiement.equals(source.getNumeroPaiement()) &&
                datePaiement.equals(source.getDatePaiement()) &&
                mode.equals(source.getMode()) &&
                montant.equals(source.getMontant()) &&
                type.equals(source.getType());

        // Comparaison des entités associées
        boolean isSouscriptionEqual = (souscription == null && source.getSouscription() == null) ||
                (souscription != null && souscription.getId().equals(source.getSouscription()));

        boolean isReclamationEqual = (reclamation == null && source.getReclamation() == null) ||
                (reclamation != null && reclamation.getId().equals(source.getReclamation()));

        // Comparaison du reçu de paiement
        boolean isRecuPaiementsEqual = (source.getRecuPaiements() == null && recuPaiements.isEmpty()) ||
                (source.getRecuPaiements() != null && source.getRecuPaiements().size() == recuPaiements.size());

        return areFieldsEqual && isSouscriptionEqual && isReclamationEqual && isRecuPaiementsEqual;
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
        return "Paiement{" +
                "id=" + getId() +
                ", numeroPaiement='" + numeroPaiement + '\'' +
                ", datePaiement=" + datePaiement +
                ", mode=" + mode +
                ", montant=" + montant +
                ", type=" + type +
                ", souscription=" + (souscription != null ? souscription.getId() : null) +
                ", reclamation=" + (reclamation != null ? reclamation.getId() : null) +
                ", recuPaiements=" + recuPaiements.stream()
                .map(recuPaiement -> "RecuPaiement{id=" + recuPaiement.getId() + "'}")
                .collect(Collectors.joining(", ")) +
        '}';
    }
}
