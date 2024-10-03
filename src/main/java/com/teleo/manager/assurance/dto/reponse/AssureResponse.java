package com.teleo.manager.assurance.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.assurance.enums.Gender;
import com.teleo.manager.generic.dto.reponse.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssureResponse extends BaseResponse {
    private String numNiu;
    private String lastName;
    private String firstName;
    private LocalDate dateNaissance;
    private String numCni;
    private Gender sexe;
    private String email;
    private String telephone;
    private String adresse;
    private String signature;
    private Long registrant;
    private Long account;
    private List<Long> dossiers = new ArrayList<>();
    private List<Long> souscriptions = new ArrayList<>();
}

