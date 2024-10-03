package com.teleo.manager.prestation.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.validators.enumaration.EnumValidator;
import com.teleo.manager.prestation.enums.FinanceurType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceurRequest extends BaseRequest {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    private String description;

    @EnumValidator(enumClass = FinanceurType.class)
    private FinanceurType type;

    private String adresse;

    private String telephone;

    @NotBlank(message = "L'email est obligatoire")
    private String email;

    private List<Long> prestations = new ArrayList<>();
}
