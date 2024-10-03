package com.teleo.manager.sinistre.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.reponse.BaseResponse;
import com.teleo.manager.sinistre.enums.SinistreStatus;
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
public class SinistreResponse extends BaseResponse {
    private String numeroSinistre;
    private String label;
    private String raison;
    private LocalDate dateSurvenance;
    private LocalDate dateDeclaration;
    private LocalDate dateReglement;
    private LocalDate datePaiement;
    private LocalDate dateCloture;
    private SinistreStatus status;
    private BigDecimal montantSinistre;
    private BigDecimal montantAssure;
    private Long souscription;
    private List<Long> prestations = new ArrayList<>();
    private List<Long> documents = new ArrayList<>();
}
