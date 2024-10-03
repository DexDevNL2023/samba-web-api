package com.teleo.manager.assurance.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.assurance.enums.GarantieStatus;
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
public class GarantieResponse extends BaseResponse {
    private String numeroGarantie;
    private String label;
    private Double percentage;
    private String termes;
    private BigDecimal plafondAssure;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private GarantieStatus status;
    private List<Long> polices = new ArrayList<>();
}
