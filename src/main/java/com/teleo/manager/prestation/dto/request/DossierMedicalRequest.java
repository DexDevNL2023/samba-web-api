package com.teleo.manager.prestation.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DossierMedicalRequest extends BaseRequest {
    @NotBlank(message = "Le numéro de dossier médical est obligatoire")
    private String numDossierMedical;

    @NotNull(message = "Le patient est obligatoire")
    private Long patient;

    @NotNull(message = "La date de dernière mise à jour est obligatoire")
    private LocalDate dateUpdated;

    private String maladiesChroniques;
    private String maladiesHereditaires;
    private String interventionsChirurgicales;
    private String hospitalisations;
    private String allergies;
    private String vaccins;
    private String habitudesAlimentaires;
    private String consommationAlcool;
    private String consommationTabac;
    private String niveauActivitePhysique;
    private BigDecimal revenusAnnuels;
    private BigDecimal chargesFinancieres;
    private Boolean declarationBonneSante;
    private Boolean consentementCollecteDonnees;
    private Boolean declarationNonFraude;
}
