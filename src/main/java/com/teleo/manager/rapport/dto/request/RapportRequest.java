package com.teleo.manager.rapport.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.validators.enumaration.EnumValidator;
import com.teleo.manager.rapport.enums.RapportType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RapportRequest extends BaseRequest {
    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    private LocalDate dateGeneration;

    @NotBlank(message = "L'URL est obligatoire")
    private String url;

    private String description;

    @EnumValidator(enumClass = RapportType.class, message = "Le type de rapport est invalide")
    private RapportType type;
}
