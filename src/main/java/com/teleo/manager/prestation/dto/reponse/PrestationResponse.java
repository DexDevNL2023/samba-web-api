package com.teleo.manager.prestation.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.reponse.BaseResponse;
import com.teleo.manager.prestation.enums.PrestationStatus;
import com.teleo.manager.prestation.enums.PrestationType;
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
public class PrestationResponse extends BaseResponse {
    private String numeroPrestation;
    private String label;
    private LocalDate datePrestation;
    private BigDecimal montant;
    private PrestationType type;
    private String description;
    private PrestationStatus status;
    private Long fournisseur;
    private Long souscription;
    private Long sinistre;
    private List<Long> financeurs = new ArrayList<>();
    private List<Long> documents = new ArrayList<>();
}
