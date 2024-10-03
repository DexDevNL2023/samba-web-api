package com.teleo.manager.assurance.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.assurance.enums.ContratType;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.validators.enumaration.EnumValidator;
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
public class ContratAssuranceRequest extends BaseRequest {

    @NotBlank(message = "Le numéro de contrat est obligatoire")
    private String numeroContrat;

    @NotNull(message = "La date du contrat est obligatoire")
    private LocalDate dateContrat;

    @EnumValidator(enumClass = ContratType.class, message = "Le type de contrat est invalide")
    private ContratType typeContrat;

    @NotBlank(message = "La couverture est obligatoire")
    private String couverture;

    private BigDecimal franchise;

    @NotBlank(message = "Les conditions sont obligatoires")
    private String conditions;

    private String exclusions;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;

    @NotNull(message = "La souscription est obligatoire")
    private Long souscription;
}
