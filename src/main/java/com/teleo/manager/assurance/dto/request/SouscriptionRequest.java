package com.teleo.manager.assurance.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.assurance.enums.PaymentFrequency;
import com.teleo.manager.assurance.enums.SouscriptionStatus;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.validators.enumaration.EnumValidator;
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
public class SouscriptionRequest extends BaseRequest {
    @NotBlank(message = "Le numéro de souscription est obligatoire")
    private String numeroSouscription;

    @NotNull(message = "La date de souscription est obligatoire")
    private LocalDate dateSouscription;

    private LocalDate dateExpiration;
    private BigDecimal montantCotisation;

    @EnumValidator(enumClass = SouscriptionStatus.class)
    private SouscriptionStatus status;

    @EnumValidator(enumClass = PaymentFrequency.class, message = "La fréquence de paiement est invalide")
    private PaymentFrequency frequencePaiement;

    @NotNull(message = "L'assuré est obligatoire")
    private Long assure;

    @NotNull(message = "La police est obligatoire")
    private Long police;

    private List<Long> contrats = new ArrayList<>();
    private List<Long> paiements = new ArrayList<>();
    private List<Long> sinistres = new ArrayList<>();
    private List<Long> reclamations = new ArrayList<>();
    private List<Long> prestations = new ArrayList<>();
}
