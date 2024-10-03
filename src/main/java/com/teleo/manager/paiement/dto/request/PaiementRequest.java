package com.teleo.manager.paiement.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.validators.enumaration.EnumValidator;
import com.teleo.manager.paiement.enums.PaymentMode;
import com.teleo.manager.paiement.enums.PaymentType;
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
public class PaiementRequest extends BaseRequest {
    @NotBlank(message = "Le numéro de paiement est obligatoire")
    private String numeroPaiement;

    @NotNull(message = "La date de paiement est obligatoire")
    private LocalDate datePaiement;

    @EnumValidator(enumClass = PaymentMode.class, message = "Le status du paiement est invalide")
    private PaymentMode mode;

    @NotNull(message = "Le montant est obligatoire")
    private BigDecimal montant;

    @EnumValidator(enumClass = PaymentType.class, message = "Le type de paiement est invalide")
    private PaymentType type;

    @NotNull(message = "La souscription est obligatoire")
    private Long souscription;

    private Long reclamation;
    private List<Long> recuPaiements = new ArrayList<>();
}
