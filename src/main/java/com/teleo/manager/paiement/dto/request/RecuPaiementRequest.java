package com.teleo.manager.paiement.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.validators.enumaration.EnumValidator;
import com.teleo.manager.paiement.enums.RecuPaymentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecuPaiementRequest extends BaseRequest {
    @NotBlank(message = "Le numéro du reçu est obligatoire")
    private String numeroRecu;

    @NotNull(message = "La date d'émission est obligatoire")
    private LocalDate dateEmission;

    @EnumValidator(enumClass = RecuPaymentType.class, message = "Le type de recu de paiement est invalide")
    private RecuPaymentType type;

    @NotNull(message = "Le montant est obligatoire")
    private BigDecimal montant;

    private String details;

    private Long paiement;
}
