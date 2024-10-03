package com.teleo.manager.assurance.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.assurance.enums.InsuranceType;
import com.teleo.manager.generic.dto.reponse.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssuranceResponse extends BaseResponse {
    private String nom;
    private String description;
    private InsuranceType type;
    private List<Long> polices = new ArrayList<>();
}
