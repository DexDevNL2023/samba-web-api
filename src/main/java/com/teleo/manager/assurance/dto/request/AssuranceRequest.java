package com.teleo.manager.assurance.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.assurance.enums.InsuranceType;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.validators.enumaration.EnumValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssuranceRequest extends BaseRequest {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    private String description;

    @EnumValidator(enumClass = InsuranceType.class, message = "Type d'assurance invalide")
    private InsuranceType type;

    private List<Long> polices = new ArrayList<>();
}


