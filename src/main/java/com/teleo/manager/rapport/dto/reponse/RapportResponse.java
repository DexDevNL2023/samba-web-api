package com.teleo.manager.rapport.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.reponse.BaseResponse;
import com.teleo.manager.rapport.enums.RapportType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RapportResponse extends BaseResponse {
    private String titre;
    private String description;
    private RapportType type;
    private LocalDate dateGeneration;
    private String url;
}
