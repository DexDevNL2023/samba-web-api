package com.teleo.manager.parametre.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyRequest extends BaseRequest {
    @NotBlank(message = "Le nom est obligatoire")
    private String name;

    private String sigle;

    @NotBlank(message = "L'email est obligatoire")
    private String email;

    private String telephone;

    private String site;

    private String telephone2;

    private String adresse;

    private String ville;

    private String bp;

    private String logo;

    @NotBlank(message = "L'entête gauche est obligatoire")
    private String enteteGauche;

    @NotBlank(message = "L'entête droite est obligatoire")
    private String enteteDroite;

    @NotBlank(message = "Le pied de page est obligatoire")
    private String piedPage;
}
