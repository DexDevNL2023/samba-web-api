package com.teleo.manager.document.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.document.enums.TypeDocument;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.validators.enumaration.EnumValidator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentRequest extends BaseRequest {
    @NotBlank(message = "Le numéro de document est obligatoire")
    private String numeroDocument;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @EnumValidator(enumClass = TypeDocument.class, message = "Le type de document est invalide")
    private TypeDocument type;

    private String description;

    @NotBlank(message = "L'URL est obligatoire")
    private String url;

    private Long sinistre;
    private Long prestation;
}
