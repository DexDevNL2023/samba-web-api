package com.teleo.manager.prestation.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FournisseurRequest extends BaseRequest {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "L'email est obligatoire")
    private String email;

    private String telephone;
    private String adresse;
    private String servicesFournis;
    private Long account;

    private List<Long> prestations = new ArrayList<>();
    private List<Long> branches = new ArrayList<>();
    private List<Long> registrants = new ArrayList<>();
}
