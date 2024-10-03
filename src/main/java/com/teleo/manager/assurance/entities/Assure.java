package com.teleo.manager.assurance.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.teleo.manager.assurance.dto.request.AssureRequest;
import com.teleo.manager.assurance.enums.Gender;
import com.teleo.manager.authentification.entities.Account;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.prestation.entities.DossierMedical;
import com.teleo.manager.prestation.entities.Registrant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "assures")
public class Assure extends BaseEntity<Assure, AssureRequest> {

    private static final String ENTITY_NAME = "ASSURE";

    private static final String MODULE_NAME = "ASSURE_MODULE";

    @Column(nullable = false, unique = true)
    private String numNiu;

    private String lastName;

    private String firstName;

    private LocalDate dateNaissance;

    @Column(nullable = false, unique = true)
    private String numCni;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender sexe;

    private String email;

    private String telephone;

    @Column(nullable = false)
    private String adresse;

    private String signature;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "registrant_id")
    private Registrant registrant;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "patient", fetch = FetchType.EAGER)
    private List<DossierMedical> dossiers = new ArrayList<>();

    @OneToMany(mappedBy = "assure", fetch = FetchType.EAGER)
    private List<Souscription> souscriptions = new ArrayList<>();

    // Ajouter des champs d'audit pour suivre les valeurs précédentes (si nécessaire)
    private String oldNumNiu;
    private LocalDate oldDateNaissance;
    private String oldNumCni;
    private String oldEmail;
    private String oldTelephone;
    private String oldSignature;

    @PreUpdate
    @PrePersist
    public void checkForChanges() {
        // Stocker les anciennes valeurs pour vérifier les changements
        oldNumNiu = this.numNiu;
        oldDateNaissance = this.dateNaissance;
        oldNumCni = this.numCni;
        oldEmail = this.email;
        oldTelephone = this.telephone;
        oldSignature = this.signature;
    }

    @Override
    public void update(Assure source) {
        this.numNiu = source.getNumNiu();
        this.lastName = source.getLastName();
        this.firstName = source.getFirstName();
        this.dateNaissance = source.getDateNaissance();
        this.numCni = source.getNumCni();
        this.sexe = source.getSexe();
        this.email = source.getEmail();
        this.telephone = source.getTelephone();
        this.adresse = source.getAdresse();
        this.signature = source.getSignature();

        // Mise à jour des entités associées
        this.registrant = source.getRegistrant();
        this.account = source.getAccount();

        // Mise à jour des dossiers médicaux
        this.dossiers.clear();
        if (source.getDossiers() != null) {
            this.dossiers.addAll(source.getDossiers());
        }

        // Mise à jour des souscriptions
        this.souscriptions.clear();
        if (source.getSouscriptions() != null) {
            this.souscriptions.addAll(source.getSouscriptions());
        }
    }

    @Override
    public boolean equalsToDto(AssureRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples
        boolean areFieldsEqual = numNiu.equals(source.getNumNiu()) &&
                lastName.equals(source.getLastName()) &&
                firstName.equals(source.getFirstName()) &&
                dateNaissance.equals(source.getDateNaissance()) &&
                numCni.equals(source.getNumCni()) &&
                sexe.equals(source.getSexe()) &&
                email.equals(source.getEmail()) &&
                telephone.equals(source.getTelephone()) &&
                adresse.equals(source.getAdresse()) &&
                signature.equals(source.getSignature());

        // Comparaison des entités associées
        boolean isRegistrantEqual = (registrant == null && source.getRegistrant() == null) ||
                (registrant != null && registrant.getId().equals(source.getRegistrant()));

        boolean isAccountEqual = (account == null && source.getAccount() == null) ||
                (account != null && account.getId().equals(source.getAccount()));

        // Comparaison des listes (basée uniquement sur la taille ici)
        boolean areDossiersEqual = (source.getDossiers() == null && dossiers.isEmpty()) ||
                (source.getDossiers() != null && source.getDossiers().size() == dossiers.size());

        boolean areSouscriptionsEqual = (source.getSouscriptions() == null && souscriptions.isEmpty()) ||
                (source.getSouscriptions() != null && source.getSouscriptions().size() == souscriptions.size());

        return areFieldsEqual && isRegistrantEqual && isAccountEqual && areDossiersEqual && areSouscriptionsEqual;
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
        return "Assure{" +
                "id=" + getId() +
                ", numNiu='" + numNiu + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", numCni='" + numCni + '\'' +
                ", sexe=" + sexe +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", addresse='" + adresse + '\'' +
                ", signature='" + signature + '\'' +
                ", account='" + account + '\'' +
                ", registrant=" + registrant +
                ", dossiers=" + dossiers.stream()
                .map(dossier -> "DossierMedical{id=" + dossier.getId() + "}")
                .collect(Collectors.joining(", ")) +
                ", souscriptions=" + souscriptions.stream()
                .map(souscription -> "Souscription{id=" + souscription.getId() + "}")
                .collect(Collectors.joining(", ")) +
        '}';
    }
}

