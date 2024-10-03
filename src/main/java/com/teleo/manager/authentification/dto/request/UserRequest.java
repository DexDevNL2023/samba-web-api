package com.teleo.manager.authentification.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.assurance.enums.Gender;
import com.teleo.manager.authentification.enums.Authority;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.validators.enumaration.EnumValidator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest extends BaseRequest {
    // Champ commun
    private boolean usingQr = false;
	@NotBlank(message = "L'email est obligatoire")
    private String email;
    private String telephone;
    private String adresse;
    @Pattern(regexp = "^data:image/(png|jpg|jpeg);base64,[A-Za-z0-9+/=]*$", message = "Format d'image non valide")
    private String imageUrl;
    private String langKey;
    private String login;

    // Admin et agent
    private boolean actived;
    @EnumValidator(enumClass = Authority.class, message = "L'authorisation est invalide")
    private Authority authority;
    @NotBlank(message = "Le nom est obligatoire")
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
