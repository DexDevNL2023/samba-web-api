package com.teleo.manager.assurance.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PoliceAssuranceRequest extends BaseRequest {
    @NotBlank(message = "Le numéro de police est obligatoire")
    private String numeroPolice;

    @Pattern(regexp = "^data:image/(png|jpg|jpeg);base64,[A-Za-z0-9+/=]*$", message = "Format d'image non valide")
    private String imageUrl;

    @NotBlank(message = "Le label est obligatoire")
    private String label;

    @NotNull(message = "La durée de couverture de la police est obligatoire")
    private Integer dureeCouverture;

    private String conditions;

    @NotNull(message = "Le montant de souscription est obligatoire")
    private BigDecimal montantSouscription;

    @NotNull(message = "L'assurance est obligatoire")
    private Long assurance;

    private List<Long> garanties = new ArrayList<>();
    private List<Long> souscriptions = new ArrayList<>();
}
