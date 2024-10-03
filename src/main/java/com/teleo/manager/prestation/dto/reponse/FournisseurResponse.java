package com.teleo.manager.prestation.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.reponse.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FournisseurResponse extends BaseResponse {
    private String nom;
    private String telephone;
    private String email;
    private String adresse;
    private String servicesFournis;
    private Long account;
    private List<Long> prestations = new ArrayList<>();
    private List<Long> branches = new ArrayList<>();
    private List<Long> registrants = new ArrayList<>();
}
