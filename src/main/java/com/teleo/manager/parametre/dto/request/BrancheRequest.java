package com.teleo.manager.parametre.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BrancheRequest extends BaseRequest {
    @NotBlank(message = "Le code est obligatoire")
    private String code;

    @NotBlank(message = "La ville est obligatoire")
    private String ville;

    private Boolean isDefaut;

    private List<Long> registrants = new ArrayList<>();
}

