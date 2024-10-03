package com.teleo.manager.assurance.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.assurance.enums.PaymentFrequency;
import com.teleo.manager.assurance.enums.SouscriptionStatus;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.validators.enumaration.EnumValidator;
import com.teleo.manager.paiement.enums.PaymentMode;
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
public class PublicSouscriptionRequest extends BaseRequest {

    @EnumValidator(enumClass = PaymentFrequency.class, message = "La fréquence de paiement est invalide")
    private PaymentFrequency frequencePaiement;

    @NotNull(message = "L'assuré est obligatoire")
    private Long account;

    @NotNull(message = "La police est obligatoire")
    private Long police;

    @EnumValidator(enumClass = PaymentMode.class, message = "Le status du paiement est invalide")
    private PaymentMode mode;

    @NotNull(message = "Le montant est obligatoire")
    private BigDecimal montant;
}
