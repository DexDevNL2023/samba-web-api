package com.teleo.manager.paiement.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.reponse.BaseResponse;
import com.teleo.manager.paiement.enums.PaymentMode;
import com.teleo.manager.paiement.enums.PaymentType;
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
public class PaiementResponse extends BaseResponse {
    private String numeroPaiement;
    private LocalDate datePaiement;
    private PaymentMode mode;
    private BigDecimal montant;
    private PaymentType type;
    private Long souscription;
    private Long reclamation;
    private List<Long> recuPaiements = new ArrayList<>();
}
