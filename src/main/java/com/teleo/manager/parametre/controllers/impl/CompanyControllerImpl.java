package com.teleo.manager.parametre.controllers.impl;

import com.teleo.manager.authentification.services.AuthorizationService;
import com.teleo.manager.generic.controller.impl.ControllerGenericImpl;
import com.teleo.manager.parametre.controllers.CompanyController;
import com.teleo.manager.parametre.dto.reponse.CompanyResponse;
import com.teleo.manager.parametre.dto.request.CompanyRequest;
import com.teleo.manager.parametre.entities.Company;
import com.teleo.manager.parametre.services.CompanyService;
import com.teleo.manager.authentification.dto.request.DroitAddRequest;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.generic.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RefreshScope
@ResponseBody
@RestController
@CrossOrigin("*")
@RequestMapping("/api/companies")
@Tag(name = "Companies", description = "API de gestion des companies")
public class CompanyControllerImpl extends ControllerGenericImpl<CompanyRequest, CompanyResponse, Company> implements CompanyController {

    private static final String MODULE_NAME = "COMPANY_MODULE";

    private final CompanyService service;
    private final AuthorizationService authorizationService;

    protected CompanyControllerImpl(CompanyService service, AuthorizationService authorizationService) {
        super(service, authorizationService);
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @Override
    protected Company newInstance() {
        return new Company();
    }

    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<CompanyResponse>> getCurrentCompany() {
        // Vérifiez si l'utilisateur a les permissions nécessaires
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        // Récupérer l'entreprise actuelle en utilisant la méthode du service
        // Retourner la réponse avec les informations de l'entreprise
        return new ResponseEntity<>(new RessourceResponse<>("Entreprise actuelle récupérée avec succès !", service.findFirstCompany()), HttpStatus.OK);
    }
}
