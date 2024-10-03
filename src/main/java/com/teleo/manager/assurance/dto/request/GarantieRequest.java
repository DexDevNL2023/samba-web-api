package com.teleo.manager.assurance.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.assurance.enums.GarantieStatus;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.validators.enumaration.EnumValidator;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GarantieRequest extends BaseRequest {
    @NotBlank(message = "Le numéro de garantie est obligatoire")
    private String numeroGarantie;

    @NotBlank(message = "Le label est obligatoire")
    private String label;

    @NotNull(message = "Le pourcentage est obligatoire")
    private Double percentage;

    private String termes;

    @NotNull(message = "Le plafond assuré est obligatoire")
    private BigDecimal plafondAssure;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    @EnumValidator(enumClass = GarantieStatus.class, message = "Le status est invalide")
    private GarantieStatus status;

    private List<Long> polices = new ArrayList<>();
}
