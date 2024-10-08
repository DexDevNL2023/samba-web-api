package com.teleo.manager.sinistre.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.validators.enumaration.EnumValidator;
import com.teleo.manager.sinistre.enums.TypeReclamation;
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
public class PublicReclamationRequest extends BaseRequest {

    @EnumValidator(enumClass = TypeReclamation.class, message = "Le type de réclamation est invalide")
    private TypeReclamation type;

    private LocalDate dateReclamation;
    private String description;

    @NotNull(message = "Le montant réclamé est obligatoire")
    private BigDecimal montantReclame;

    @NotNull(message = "L'identifiant du compte est obligatoire")
    private Long account;

    private Long sinistre;
    private Long prestation;
}
