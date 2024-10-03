package com.teleo.manager.assurance.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.assurance.enums.Gender;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.validators.enumaration.EnumValidator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class AssureRequest extends BaseRequest {
    @NotBlank(message = "Le numéro NIU est obligatoire")
    private String numNiu;

    @NotBlank(message = "Le nom de famille est obligatoire")
    private String lastName;

    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;

    @NotNull(message = "La date de naissance est obligatoire")
    private LocalDate dateNaissance;

    @NotBlank(message = "Le numéro CNI est obligatoire")
    private String numCni;

    @EnumValidator(enumClass = Gender.class, message = "Le genre est invalide")
    private Gender sexe;

    @Email(message = "L'email est invalide")
    private String email;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    private String telephone;

    @NotBlank(message = "L'adresse est obligatoire")
    private String adresse;

    private String signature;

    private Long registrant;
    private Long account;

    private List<Long> dossiers = new ArrayList<>();
    private List<Long> souscriptions = new ArrayList<>();
}

