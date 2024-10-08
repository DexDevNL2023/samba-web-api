package com.teleo.manager.prestation.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.document.dto.request.DocumentFileRequest;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.validators.enumaration.EnumValidator;
import com.teleo.manager.prestation.enums.PrestationType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PublicPrestationRequest extends BaseRequest {

    private String label;

    private LocalDate datePrestation;

    @NotNull(message = "Le montant est obligatoire")
    private BigDecimal montant;

    @EnumValidator(enumClass = PrestationType.class, message = "Le type de prestation est invalide")
    private PrestationType type;

    private String description;

    private List<Long> financeurs;

    @NotNull(message = "L'identifiant du compte est obligatoire")
    private Long account;

    private Long sinistre;
    private List<DocumentFileRequest> documents;
}
