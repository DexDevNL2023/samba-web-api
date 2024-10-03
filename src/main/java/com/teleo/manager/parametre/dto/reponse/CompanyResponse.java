package com.teleo.manager.parametre.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.reponse.BaseResponse;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyResponse extends BaseResponse {
    private String name;
    private String sigle;
    private String email;
    private String telephone;
    private String site;
    private String telephone2;
    private String adresse;
    private String ville;
    private String bp;
    private String logo;
    private String enteteGauche;
    private String enteteDroite;
    private String piedPage;
}
