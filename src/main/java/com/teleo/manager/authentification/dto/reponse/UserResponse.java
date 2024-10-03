package com.teleo.manager.authentification.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.assurance.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.teleo.manager.authentification.enums.Authority;
import com.teleo.manager.generic.dto.reponse.BaseResponse;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse extends BaseResponse {
    // Champ commun
    private boolean usingQr = false;
    private String email;
    private String telephone;
    private String adresse;
    private String imageUrl;
    private String langKey;
    private String login;

    // Admin et agent
    private boolean actived;
    private Authority authority;
    private String fullName;

    // Client
    private String numNiu;
    private String lastName;
    private String firstName;
    private LocalDate dateNaissance;
    private String numCni;
    private Gender sexe;
    private String signature;

    // Provider
    private String nom;
    private String servicesFournis;
}
