package com.teleo.manager.assurance.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.reponse.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PoliceAssuranceResponse extends BaseResponse {
    private String numeroPolice;
    private String imageUrl;
    private String label;
    private Integer dureeCouverture;
    private String conditions;
    private BigDecimal montantSouscription;
    private Long assurance;
    private List<Long> garanties = new ArrayList<>();
    private List<Long> souscriptions = new ArrayList<>();
}
