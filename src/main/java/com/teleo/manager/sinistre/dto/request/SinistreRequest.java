package com.teleo.manager.sinistre.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.validators.enumaration.EnumValidator;
import com.teleo.manager.sinistre.enums.SinistreStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SinistreRequest extends BaseRequest {
    @NotBlank(message = "Le numéro de sinistre est obligatoire")
    private String numeroSinistre;

    private String label;
    private String raison;

    @NotNull(message = "La date de survenance est obligatoire")
    private LocalDate dateSurvenance;

    @NotNull(message = "La date de declaration est obligatoire")
    private LocalDate dateDeclaration;

    private LocalDate dateReglement;
    private LocalDate datePaiement;
    private LocalDate dateCloture;

    @EnumValidator(enumClass = SinistreStatus.class, message = "Le status du sinistre est invalide")
    private SinistreStatus status;

    private BigDecimal montantSinistre;
    private BigDecimal montantAssure;

    @NotNull(message = "La souscription est obligatoire")
    private Long souscription;

    private List<Long> prestations = new ArrayList<>();
    private List<Long> documents = new ArrayList<>();
}
