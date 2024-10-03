package com.teleo.manager.prestation.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrantRequest extends BaseRequest {
    @NotBlank(message = "Le numéro du registrant est obligatoire")
    private String numeroRegistrant;

    @NotNull(message = "La branche est obligatoire")
    private Long branche;

    @NotNull(message = "Le fournisseur est obligatoire")
    private Long fournisseur;
}
