package com.teleo.manager.parametre.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.reponse.BaseResponse;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BrancheResponse extends BaseResponse {
    private String code;
    private String ville;
    private Boolean isDefaut;
    private List<Long> registrants = new ArrayList<>();
}

