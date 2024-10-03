package com.teleo.manager.prestation.controllers.impl;

import com.teleo.manager.authentification.dto.request.DroitAddRequest;
import com.teleo.manager.authentification.services.AuthorizationService;
import com.teleo.manager.generic.controller.impl.ControllerGenericImpl;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.generic.utils.AppConstants;
import com.teleo.manager.prestation.controllers.RegistrantController;
import com.teleo.manager.prestation.dto.reponse.LiteRegistrantResponse;
import com.teleo.manager.prestation.dto.reponse.RegistrantResponse;
import com.teleo.manager.prestation.dto.request.RegistrantRequest;
import com.teleo.manager.prestation.entities.Registrant;
import com.teleo.manager.prestation.services.RegistrantService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RefreshScope
@ResponseBody
@RestController
@CrossOrigin("*")
@RequestMapping("/api/registrants")
@Tag(name = "Registrants", description = "API de gestion des registrants")
public class RegistrantControllerImpl extends ControllerGenericImpl<RegistrantRequest, LiteRegistrantResponse, Registrant> implements RegistrantController {

    private static final String MODULE_NAME = "FOURNISSEUR_MODULE";

    private final RegistrantService service;
    private final AuthorizationService authorizationService;

    protected RegistrantControllerImpl(RegistrantService service, AuthorizationService authorizationService) {
        super(service, authorizationService);
        this.service = service;
        this.authorizationService = authorizationService;
    }

    @Override
    protected Registrant newInstance() {
        return new Registrant();
    }

    @GetMapping(value = "/find/by/branche/{brancheId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<LiteRegistrantResponse>>> getAllByBrancheId(@NotNull @PathVariable("brancheId") Long brancheId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Registrant retrouvé avec succès!", service.findAllByBrancheId(brancheId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/fournisseur/{fournisseurId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<LiteRegistrantResponse>>> getAllByFournisseurId(@NotNull @PathVariable("fournisseurId") Long fournisseurId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Registrant retrouvé avec succès!", service.findAllByFournisseurId(fournisseurId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/branche/{brancheId}/fournisseur/{fournisseurId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<LiteRegistrantResponse>> getByBrancheIdAndFournisseurId(@NotNull @PathVariable("brancheId") Long brancheId,
                                                                                                @NotNull @PathVariable("fournisseurId") Long fournisseurId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Registrant retrouvé avec succès!", service.findByBrancheIdAndFournisseurId(brancheId, fournisseurId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/assure/{assureId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<LiteRegistrantResponse>> getByAssureId(@NotNull @PathVariable("assureId") Long assureId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Registrant retrouvé avec succès!", service.findByAssureId(assureId)), HttpStatus.OK);
    }

    @GetMapping(value = "/find/by/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<RegistrantResponse>> getByUserId(@NotNull @PathVariable("userId") Long userId) {
        authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<>("Registrant retrouvé avec succès!", service.findByUserId(userId)), HttpStatus.OK);
    }
}
