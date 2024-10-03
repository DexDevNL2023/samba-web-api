package com.teleo.manager.paiement.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.reponse.BaseResponse;
import com.teleo.manager.paiement.enums.RecuPaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecuPaiementResponse extends BaseResponse {
    private String numeroRecu;
    private LocalDate dateEmission;
    private RecuPaymentType type;
    private BigDecimal montant;
    private String details;
    private Long paiement;
}
