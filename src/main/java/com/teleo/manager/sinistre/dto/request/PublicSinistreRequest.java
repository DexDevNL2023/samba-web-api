package com.teleo.manager.sinistre.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.document.dto.request.DocumentFileRequest;
import com.teleo.manager.generic.dto.request.BaseRequest;
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
public class PublicSinistreRequest extends BaseRequest {

    @NotNull(message = "Le libellé est obligatoire")
    private String label;

    private String raison;

    private LocalDate dateSurvenance;

    @NotNull(message = "Le montant du sinistre est obligatoire")
    private BigDecimal montantSinistre;

    @NotNull(message = "L'identifiant du compte est obligatoire")
    private Long account;

    private Long souscription;
    private List<DocumentFileRequest> documents;
}
