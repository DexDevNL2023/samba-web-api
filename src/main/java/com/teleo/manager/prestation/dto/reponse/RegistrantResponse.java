package com.teleo.manager.prestation.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.reponse.BaseResponse;
import com.teleo.manager.parametre.dto.reponse.BrancheResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrantResponse extends BaseResponse {
    private String numeroRegistrant;
    private BrancheResponse branche;
    private FournisseurResponse fournisseur;
}
