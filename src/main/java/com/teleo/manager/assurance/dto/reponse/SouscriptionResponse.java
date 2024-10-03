package com.teleo.manager.assurance.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.assurance.enums.PaymentFrequency;
import com.teleo.manager.assurance.enums.SouscriptionStatus;
import com.teleo.manager.generic.dto.reponse.BaseResponse;
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
public class SouscriptionResponse extends BaseResponse {
    private String numeroSouscription;
    private LocalDate dateSouscription;
    private LocalDate dateExpiration;
    private BigDecimal montantCotisation;
    private SouscriptionStatus status;
    private PaymentFrequency frequencePaiement;
    private Long assure;
    private Long police;
    private List<Long> contrats = new ArrayList<>();
    private List<Long> paiements = new ArrayList<>();
    private List<Long> sinistres = new ArrayList<>();
    private List<Long> reclamations = new ArrayList<>();
    private List<Long> prestations = new ArrayList<>();
}
