package com.teleo.manager.prestation.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.teleo.manager.assurance.entities.Assure;
import com.teleo.manager.generic.entity.audit.BaseEntity;
import com.teleo.manager.prestation.dto.request.DossierMedicalRequest;
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
@Table(name = "dossiers_medicaux")
public class DossierMedical extends BaseEntity<DossierMedical, DossierMedicalRequest> {

    private static final String ENTITY_NAME = "DOSSIER_MEDICAL";
    private static final String MODULE_NAME = "DOSSIER_MEDICAUX_MODULE";

    @Column(nullable = false, unique = true)
    private String numDossierMedical;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assure_id")
    private Assure patient;

    private LocalDate dateUpdated;

    @Column(length = 5000)
    private String maladiesChroniques;

    @Column(length = 5000)
    private String maladiesHereditaires;

    @Column(length = 5000)
    private String interventionsChirurgicales;

    @Column(length = 5000)
    private String hospitalisations;

    @Column(length = 5000)
    private String allergies;

    @Column(length = 5000)
    private String vaccins;

    @Column(length = 5000)
    private String habitudesAlimentaires;

    @Column(length = 5000)
    private String consommationAlcool;

    @Column(length = 5000)
    private String consommationTabac;

    @Column(length = 5000)
    private String niveauActivitePhysique;

    @Column(length = 5000)
    private BigDecimal revenusAnnuels;

    @Column(length = 5000)
    private BigDecimal chargesFinancieres;

    @Column(length = 5000)
    private Boolean declarationBonneSante;

    @Column(length = 5000)
    private Boolean consentementCollecteDonnees;

    @Column(length = 5000)
    private Boolean declarationNonFraude;

    @Override
    public void update(DossierMedical source) {
        this.numDossierMedical = source.getNumDossierMedical();
        this.patient = source.getPatient();
        this.dateUpdated = source.getDateUpdated();
        this.maladiesChroniques = source.getMaladiesChroniques();
        this.maladiesHereditaires = source.getMaladiesHereditaires();
        this.interventionsChirurgicales = source.getInterventionsChirurgicales();
        this.hospitalisations = source.getHospitalisations();
        this.allergies = source.getAllergies();
        this.vaccins = source.getVaccins();
        this.habitudesAlimentaires = source.getHabitudesAlimentaires();
        this.consommationAlcool = source.getConsommationAlcool();
        this.consommationTabac = source.getConsommationTabac();
        this.niveauActivitePhysique = source.getNiveauActivitePhysique();
        this.revenusAnnuels = source.getRevenusAnnuels();
        this.chargesFinancieres = source.getChargesFinancieres();
        this.declarationBonneSante = source.getDeclarationBonneSante();
        this.consentementCollecteDonnees = source.getConsentementCollecteDonnees();
        this.declarationNonFraude = source.getDeclarationNonFraude();
    }

    @Override
    public boolean equalsToDto(DossierMedicalRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples
        boolean areFieldsEqual = numDossierMedical.equals(source.getNumDossierMedical()) &&
                (maladiesChroniques == null ? source.getMaladiesChroniques() == null : maladiesChroniques.equals(source.getMaladiesChroniques())) &&
                (maladiesHereditaires == null ? source.getMaladiesHereditaires() == null : maladiesHereditaires.equals(source.getMaladiesHereditaires())) &&
                (interventionsChirurgicales == null ? source.getInterventionsChirurgicales() == null : interventionsChirurgicales.equals(source.getInterventionsChirurgicales())) &&
                (hospitalisations == null ? source.getHospitalisations() == null : hospitalisations.equals(source.getHospitalisations())) &&
                (allergies == null ? source.getAllergies() == null : allergies.equals(source.getAllergies())) &&
                (vaccins == null ? source.getVaccins() == null : vaccins.equals(source.getVaccins())) &&
                (habitudesAlimentaires == null ? source.getHabitudesAlimentaires() == null : habitudesAlimentaires.equals(source.getHabitudesAlimentaires())) &&
                (consommationAlcool == null ? source.getConsommationAlcool() == null : consommationAlcool.equals(source.getConsommationAlcool())) &&
                (consommationTabac == null ? source.getConsommationTabac() == null : consommationTabac.equals(source.getConsommationTabac())) &&
                (niveauActivitePhysique == null ? source.getNiveauActivitePhysique() == null : niveauActivitePhysique.equals(source.getNiveauActivitePhysique())) &&
                (revenusAnnuels == null ? source.getRevenusAnnuels() == null : revenusAnnuels.equals(source.getRevenusAnnuels())) &&
                (chargesFinancieres == null ? source.getChargesFinancieres() == null : chargesFinancieres.equals(source.getChargesFinancieres())) &&
                declarationBonneSante.equals(source.getDeclarationBonneSante()) &&
                consentementCollecteDonnees.equals(source.getConsentementCollecteDonnees()) &&
                declarationNonFraude.equals(source.getDeclarationNonFraude());

        // Comparaison de l'entité associée (patient)
        boolean isPatientEqual = (patient == null && source.getPatient() == null) ||
                (patient != null && patient.getId().equals(source.getPatient()));

        return areFieldsEqual && isPatientEqual;
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
        return "DossierMedical{" +
            "id=" + getId() +
            ", numDossierMedical='" + numDossierMedical + '\'' +
            ", patient=" + (patient != null ? "Assure{id=" + patient.getId() + '}' : "null") +
            ", dateUpdated=" + dateUpdated +
            ", maladiesChroniques='" + maladiesChroniques + '\'' +
            ", maladiesHereditaires='" + maladiesHereditaires + '\'' +
            ", interventionsChirurgicales='" + interventionsChirurgicales + '\'' +
            ", hospitalisations='" + hospitalisations + '\'' +
            ", allergies='" + allergies + '\'' +
            ", vaccins='" + vaccins + '\'' +
            ", habitudesAlimentaires='" + habitudesAlimentaires + '\'' +
            ", consommationAlcool='" + consommationAlcool + '\'' +
            ", consommationTabac='" + consommationTabac + '\'' +
            ", niveauActivitePhysique='" + niveauActivitePhysique + '\'' +
            ", revenusAnnuels=" + revenusAnnuels +
            ", chargesFinancieres=" + chargesFinancieres +
            ", declarationBonneSante=" + declarationBonneSante +
            ", consentementCollecteDonnees=" + consentementCollecteDonnees +
            ", declarationNonFraude=" + declarationNonFraude +
        '}';
    }
}

