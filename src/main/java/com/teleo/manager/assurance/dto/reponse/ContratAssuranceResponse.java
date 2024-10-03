package com.teleo.manager.assurance.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.assurance.enums.ContratType;
import com.teleo.manager.generic.dto.reponse.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContratAssuranceResponse extends BaseResponse {
    private String numeroContrat;
    private LocalDate dateContrat;
    private ContratType typeContrat;
    private String couverture;
    private BigDecimal franchise;
    private String conditions;
    private String exclusions;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Long souscription;
}
