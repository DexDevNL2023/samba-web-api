package com.teleo.manager.sinistre.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleo.manager.generic.dto.request.BaseRequest;
import com.teleo.manager.generic.validators.enumaration.EnumValidator;
import com.teleo.manager.paiement.entities.Paiement;
import com.teleo.manager.sinistre.enums.StatutReclamation;
import com.teleo.manager.sinistre.enums.TypeReclamation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReclamationRequest extends BaseRequest {

    @NotBlank(message = "Le numéro de réclamation est obligatoire")
    private String numeroReclamation;

    @EnumValidator(enumClass = TypeReclamation.class, message = "Le type de reclamation est invalide")
    private TypeReclamation type;

    private LocalDate dateReclamation;

    @EnumValidator(enumClass = StatutReclamation.class, message = "Le status de la reclamation est invalide")
    private StatutReclamation status;

    private String description;

    private BigDecimal montantReclame;

    private BigDecimal montantApprouve;

    private LocalDate dateEvaluation;

    private String agentEvaluateur;

    private String justification;

    @NotNull(message = "La souscription est obligatoire")
    private Long souscription;
    private Long sinistre;
    private Long prestation;
    private List<Long> paiements = new ArrayList<>();
}
