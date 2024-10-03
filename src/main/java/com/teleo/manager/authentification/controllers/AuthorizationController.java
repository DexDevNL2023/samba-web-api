package com.teleo.manager.authentification.controllers;

import com.teleo.manager.authentification.dto.reponse.AccountResponse;
import com.teleo.manager.authentification.dto.reponse.RoleFormResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import com.teleo.manager.authentification.dto.request.DroitAddRequest;
import com.teleo.manager.authentification.dto.request.PermissionFormRequest;
import com.teleo.manager.authentification.services.AuthorizationService;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.generic.utils.AppConstants;
import com.teleo.manager.authentification.security.SecurityUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RefreshScope
@ResponseBody
@RestController
@CrossOrigin("*")
@RequestMapping("/api/autorisations")
@Tag(name = "Authorisations", description = "API de gestion des authorisations")
public class AuthorizationController {

    private static final String MODULE_NAME = "ACCOUNT_MODULE";
    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<RoleFormResponse>>> getAllAutorisations(@NotNull @PathVariable("userId") Long userId) {
        return new ResponseEntity<>(new RessourceResponse<List<RoleFormResponse>>("Permissions trouvées avec succès!", authorizationService.getAutorisations(userId)), HttpStatus.OK);
    }

    @PutMapping(value = "/change/autorisations", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<AccountResponse>> changeAutorisation(@Valid @RequestBody PermissionFormRequest dto) {
		authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.CHANGE_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<AccountResponse>("Permission changée avec succès!", authorizationService.changeAutorisation(dto)), HttpStatus.OK);
    }

    // Rest Client Controllers
    @GetMapping(value = "/current/user/name", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public Optional<String> getCurrentUserLogin() {
        return SecurityUtils.getCurrentUserLogin();
    }

    // Méthode produisant HAL+JSON
    @GetMapping(value = "/current/user/jwt", produces = { "application/hal+json" })
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<?> getCurrentUserJWT() {
        Optional<String> jwt = SecurityUtils.getCurrentUserJWT();
        if (jwt.isPresent()) {
            return ResponseEntity.ok(jwt.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping(value = "/current/user/have/this/authoritie/{authority}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public Boolean hasCurrentUserThisAuthority(@NotNull @PathVariable("authority") String authority) {
        return SecurityUtils.hasCurrentUserThisAuthority(authority);
    }

    @GetMapping(value = "/current/user/any/Of/authorities/{authorities}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public Boolean hasCurrentUserAnyOfAuthorities(@NotNull @PathVariable("authorities") String... authorities) {
        return SecurityUtils.hasCurrentUserAnyOfAuthorities();
    }

    @GetMapping(value = "/current/user/none/Of/authorities/{authorities}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public Boolean hasCurrentUserNoneOfAuthorities(@NotNull @PathVariable("authorities") String... authorities) {
        return SecurityUtils.hasCurrentUserNoneOfAuthorities();
    }

    @GetMapping(value = "/current/user/is/authenticated", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public Boolean isAuthenticated() {
        return SecurityUtils.isAuthenticated();
    }

    @PutMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public void checkIfHasDroit(@Valid @RequestBody DroitAddRequest dto) {
        authorizationService.checkIfHasDroit(dto);
    }
}
