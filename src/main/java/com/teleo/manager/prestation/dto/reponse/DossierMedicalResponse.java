package com.teleo.manager.prestation.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.reponse.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DossierMedicalResponse extends BaseResponse {
    private String numDossierMedical;
    private Long patient;
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
