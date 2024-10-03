package com.teleo.manager.rapport.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BilanDTO {
    private BigDecimal totalActifs;
    private BigDecimal totalPassifs;
    private BigDecimal totalImmobilisations;
    private BigDecimal totalInvestissements;
    private BigDecimal totalCreances;
    private BigDecimal totalTresorerie;
    private BigDecimal totalProvisionsTechniques;
    private BigDecimal totalDettes;
}
