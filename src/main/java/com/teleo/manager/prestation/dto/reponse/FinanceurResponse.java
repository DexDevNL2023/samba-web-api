package com.teleo.manager.prestation.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.reponse.BaseResponse;
import com.teleo.manager.prestation.enums.FinanceurType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceurResponse extends BaseResponse {
    private String nom;
    private String description;
    private FinanceurType type;
    private String adresse;
    private String telephone;
    private String email;
    private List<Long> prestations = new ArrayList<>();
}
