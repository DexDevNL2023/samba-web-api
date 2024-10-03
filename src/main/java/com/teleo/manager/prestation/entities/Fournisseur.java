package com.teleo.manager.prestation.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.teleo.manager.authentification.entities.Account;
import com.teleo.manager.prestation.dto.request.FournisseurRequest;
import jakarta.persistence.Column;
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
@Table(name = "fournisseurs")
public class Fournisseur extends BaseEntity<Fournisseur, FournisseurRequest> {

    private static final String ENTITY_NAME = "FOURNISSEUR";
    private static final String MODULE_NAME = "FOURNISSEUR_MODULE";

    @Column(nullable = false)
    private String nom;

    private String telephone;

    @Column(nullable = false, unique = true)
    private String email;

    private String adresse;

    @Column(length = 5000)
    private String servicesFournis;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "fournisseur", fetch = FetchType.EAGER)
    private List<Prestation> prestations = new ArrayList<>();

    @OneToMany(mappedBy = "fournisseur", fetch = FetchType.EAGER)
    private List<Registrant> registrants = new ArrayList<>();

    // Nouvelle méthode pour ajouter des registrants à un fournisseur
    public void addRegistrants(List<Registrant> newRegistrants) {
        for (Registrant newRegistrant : newRegistrants) {
            // Vérifier si le rôle est déjà présent
            if (!this.registrants.contains(newRegistrant)) {
                // Associer le rôle à ce compte
                newRegistrant.setFournisseur(this);
                this.registrants.add(newRegistrant);
            }
        }
    }

    @Override
    public void update(Fournisseur source) {
        this.nom = source.getNom();
        this.telephone = source.getTelephone();
        this.email = source.getEmail();
        this.adresse = source.getAdresse();
        this.servicesFournis = source.getServicesFournis();

        // Mise à jour des prestations
        this.prestations.clear();
        if (source.getPrestations() != null) {
            this.prestations.addAll(source.getPrestations());
        }

        // Mise à jour des registrants
        this.registrants.clear();
        if (source.getRegistrants() != null) {
            this.registrants.addAll(source.getRegistrants());
        }
    }

    @Override
    public boolean equalsToDto(FournisseurRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples
        boolean areFieldsEqual = nom.equals(source.getNom()) &&
                (telephone == null ? source.getTelephone() == null : telephone.equals(source.getTelephone())) &&
                email.equals(source.getEmail()) &&
                (adresse == null ? source.getAdresse() == null : adresse.equals(source.getAdresse())) &&
                (servicesFournis == null ? source.getServicesFournis() == null : servicesFournis.equals(source.getServicesFournis()));

        // Comparaison des prestations (basée uniquement sur la taille ici)
        boolean arePrestationsEqual = (source.getPrestations() == null && prestations.isEmpty()) ||
                (source.getPrestations() != null && source.getPrestations().size() == prestations.size());

        // Comparaison des registrants (basée uniquement sur la taille ici)
        boolean areRegistrantsEqual = (source.getRegistrants() == null && registrants.isEmpty()) ||
                (source.getRegistrants() != null && source.getRegistrants().size() == registrants.size());

        return areFieldsEqual && arePrestationsEqual && areRegistrantsEqual;
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
        return "Fournisseur{" +
                "id=" + getId() +
                ", nom='" + nom + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", adresse='" + adresse + '\'' +
                ", servicesFournis='" + servicesFournis + '\'' +
                ", prestations=" + prestations.stream()
                .map(prestation -> "Prestation{id=" + prestation.getId() + "'}")
                .collect(Collectors.joining(", ")) +
                ", registrants=" + registrants.stream()
                .map(registrant -> "Registrant{id=" + registrant.getId() + "'}")
                .collect(Collectors.joining(", ")) +
        '}';
    }
}
