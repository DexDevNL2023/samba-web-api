package com.teleo.manager.paiement.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.validators.enumaration.EnumValidator;
import com.teleo.manager.paiement.enums.PaymentMode;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PublicPaiementRequest extends BaseRequest {

    @EnumValidator(enumClass = PaymentMode.class, message = "Le mode de paiement est invalide")
    private PaymentMode mode;

    @NotNull(message = "Le montant est obligatoire")
    private BigDecimal montant;

    @NotNull(message = "L'identifiant du compte est obligatoire")
    private Long account;

    private Long souscription;
}
