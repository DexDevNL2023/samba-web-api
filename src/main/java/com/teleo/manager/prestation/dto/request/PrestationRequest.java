package com.teleo.manager.prestation.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.validators.enumaration.EnumValidator;
import com.teleo.manager.prestation.enums.PrestationStatus;
import com.teleo.manager.prestation.enums.PrestationType;
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
public class PrestationRequest extends BaseRequest {
    @NotBlank(message = "Le numéro de prestation est obligatoire")
    private String numeroPrestation;

    @NotBlank(message = "Le label est obligatoire")
    private String label;

    @NotNull(message = "La date de prestation est obligatoire")
    private LocalDate datePrestation;

    @NotNull(message = "Le montant est obligatoire")
    private BigDecimal montant;

    @EnumValidator(enumClass = PrestationType.class, message = "Le type de prestation est invalide")
    private PrestationType type;

    private String description;

    @EnumValidator(enumClass = PrestationStatus.class, message = "Le status de la prestation est invalide")
    private PrestationStatus status;

    @NotNull(message = "Le fournisseur est obligatoire")
    private Long fournisseur;

    @NotNull(message = "La souscription est obligatoire")
    private Long souscription;

    private Long sinistre;
    private List<Long> financeurs = new ArrayList<>();
    private List<Long> documents = new ArrayList<>();
}
