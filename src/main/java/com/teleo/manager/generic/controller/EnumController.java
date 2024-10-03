package com.teleo.manager.generic.controller;

import com.teleo.manager.assurance.enums.*;
import com.teleo.manager.notification.enums.TypeNotification;
import com.teleo.manager.paiement.enums.PaymentMode;
import com.teleo.manager.paiement.enums.PaymentType;
import com.teleo.manager.prestation.enums.FinanceurType;
import com.teleo.manager.prestation.enums.PrestationStatus;
import com.teleo.manager.prestation.enums.PrestationType;
import com.teleo.manager.rapport.enums.RapportType;
import com.teleo.manager.sinistre.enums.SinistreStatus;
import com.teleo.manager.sinistre.enums.StatutReclamation;
import com.teleo.manager.sinistre.enums.TypeReclamation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import com.teleo.manager.authentification.enums.Authority;
import com.teleo.manager.authentification.enums.SocialProvider;
import com.teleo.manager.generic.enums.EnumValue;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.hateoas.MediaTypes;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RefreshScope
@ResponseBody
@RestController
@CrossOrigin("*")
@RequestMapping("/api/enums")
@Tag(name = "Enumerateurs", description = "API de gestion des enumerateurs")
public class EnumController {
    //Ennumerateur
    @GetMapping(value = "/authorities", produces = MediaTypes.HAL_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public List<EnumValue> getRoleNames() {
        return Authority.valuesInList().stream()
                .map(e -> new EnumValue(e.name(), e.getValue()))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/social-provider", produces = MediaTypes.HAL_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public List<EnumValue> getSocialProviders() {
        return SocialProvider.valuesInList().stream()
                .map(e -> new EnumValue(e.name(), e.getValue()))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/contrat-type", produces = MediaTypes.HAL_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public List<EnumValue> getContratTypes() {
        return ContratType.valuesInList().stream()
                .map(e -> new EnumValue(e.name(), e.getValue()))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/garantie-status", produces = MediaTypes.HAL_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public List<EnumValue> getGarantieStatus() {
        return GarantieStatus.valuesInList().stream()
                .map(e -> new EnumValue(e.name(), e.getValue()))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/gender", produces = MediaTypes.HAL_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public List<EnumValue> getGenders() {
        return Gender.valuesInList().stream()
                .map(e -> new EnumValue(e.name(), e.getValue()))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/insurance-type", produces = MediaTypes.HAL_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public List<EnumValue> getInsuranceTypes() {
        return InsuranceType.valuesInList().stream()
                .map(e -> new EnumValue(e.name(), e.getValue()))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/payment-frequency", produces = MediaTypes.HAL_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public List<EnumValue> getPaymentFrequencys() {
        return PaymentFrequency.valuesInList().stream()
                .map(e -> new EnumValue(e.name(), e.getValue()))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/souscription-status", produces = MediaTypes.HAL_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public List<EnumValue> getSouscriptionStatus() {
        return SouscriptionStatus.valuesInList().stream()
                .map(e -> new EnumValue(e.name(), e.getValue()))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/type-notification", produces = MediaTypes.HAL_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public List<EnumValue> getTypeNotifications() {
        return TypeNotification.valuesInList().stream()
                .map(e -> new EnumValue(e.name(), e.getValue()))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/payment-mode", produces = MediaTypes.HAL_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public List<EnumValue> getPaymentModes() {
        return PaymentMode.valuesInList().stream()
                .map(e -> new EnumValue(e.name(), e.getValue()))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/payment-type", produces = MediaTypes.HAL_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public List<EnumValue> getPaymentTypes() {
        return PaymentType.valuesInList().stream()
                .map(e -> new EnumValue(e.name(), e.getValue()))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/financeur-type", produces = MediaTypes.HAL_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public List<EnumValue> getFinanceurTypes() {
        return FinanceurType.valuesInList().stream()
                .map(e -> new EnumValue(e.name(), e.getValue()))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/prestation-status", produces = MediaTypes.HAL_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public List<EnumValue> getPrestationStatus() {
        return PrestationStatus.valuesInList().stream()
                .map(e -> new EnumValue(e.name(), e.getValue()))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/prestation-type", produces = MediaTypes.HAL_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public List<EnumValue> getPrestationTypes() {
        return PrestationType.valuesInList().stream()
                .map(e -> new EnumValue(e.name(), e.getValue()))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/rapport-type", produces = MediaTypes.HAL_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public List<EnumValue> getRapportTypes() {
        return RapportType.valuesInList().stream()
                .map(e -> new EnumValue(e.name(), e.getValue()))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/sinistre-status", produces = MediaTypes.HAL_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public List<EnumValue> getSinistreStatus() {
        return SinistreStatus.valuesInList().stream()
                .map(e -> new EnumValue(e.name(), e.getValue()))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/statut-reclamation", produces = MediaTypes.HAL_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public List<EnumValue> getStatutReclamations() {
        return StatutReclamation.valuesInList().stream()
                .map(e -> new EnumValue(e.name(), e.getValue()))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/type-reclamation", produces = MediaTypes.HAL_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public List<EnumValue> getTypeReclamations() {
        return TypeReclamation.valuesInList().stream()
                .map(e -> new EnumValue(e.name(), e.getValue()))
                .collect(Collectors.toList());
    }
}
