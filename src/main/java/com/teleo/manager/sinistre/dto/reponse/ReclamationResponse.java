package com.teleo.manager.sinistre.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.reponse.BaseResponse;
import com.teleo.manager.sinistre.enums.StatutReclamation;
import com.teleo.manager.sinistre.enums.TypeReclamation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReclamationResponse extends BaseResponse {
    private String numeroReclamation;
    private TypeReclamation type;
    private LocalDate dateReclamation;
    private StatutReclamation status;
    private String description;
    private Double montantReclame;
    private Double montantApprouve;
    private LocalDate dateEvaluation;
    private String agentEvaluateur;
    private String justification;
    private Long souscription;
    private Long sinistre;
    private Long prestation;
    private List<Long> paiements = new ArrayList<>();
}
