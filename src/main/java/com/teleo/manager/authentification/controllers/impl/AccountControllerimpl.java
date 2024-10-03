package com.teleo.manager.authentification.controllers.impl;

import com.teleo.manager.authentification.dto.reponse.AccountResponse;
import com.teleo.manager.authentification.dto.reponse.UserResponse;
import com.teleo.manager.authentification.dto.request.UserRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import com.teleo.manager.authentification.controllers.AccountController;
import com.teleo.manager.authentification.dto.request.DroitAddRequest;
import com.teleo.manager.authentification.dto.request.AccountRequest;
import com.teleo.manager.authentification.dto.request.ChangePasswordRequest;
import com.teleo.manager.authentification.entities.Account;
import com.teleo.manager.authentification.services.AccountService;
import com.teleo.manager.authentification.services.AuthorizationService;
import com.teleo.manager.generic.controller.impl.ControllerGenericImpl;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import com.teleo.manager.generic.utils.AppConstants;
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
@RequestMapping("/api/accounts")
@Tag(name = "Utilisateurs", description = "API de gestion des utilisateurs")
public class AccountControllerimpl extends ControllerGenericImpl<AccountRequest, AccountResponse, Account> implements AccountController {

	private static final String MODULE_NAME = "ACCOUNT_MODULE";

	private final AccountService service;
	private final AuthorizationService authorizationService;

    protected AccountControllerimpl(AccountService service, AuthorizationService authorizationService) {
        super(service, authorizationService);
		this.service = service;
        this.authorizationService = authorizationService;
    }

    @Override
    protected Account newInstance() {
        return new Account();
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
	@SecurityRequirement(name = "Authorization")
	public ResponseEntity<RessourceResponse<AccountResponse>> getCurrentUser() {
		return new ResponseEntity<>(new RessourceResponse<AccountResponse>("Utilisateur trouvé avec succès!", service.getCurrentUser()), HttpStatus.OK);
	}

	@PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
	@SecurityRequirement(name = "Authorization")
	public ResponseEntity<RessourceResponse<Boolean>> logout() {
		return new ResponseEntity<>(new RessourceResponse<Boolean>("Déconnexion de l'utilisateur réussie!", service.logout()), HttpStatus.OK);
	}

	@PostMapping(value = "/update/profile", produces = MediaType.APPLICATION_JSON_VALUE)
	@SecurityRequirement(name = "Authorization")
	public ResponseEntity<RessourceResponse<UserResponse>> updateProfile(@Valid @RequestBody UserRequest dto, HttpServletRequest request) {
		authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
		return new ResponseEntity<>(new RessourceResponse<>("Profile mise a jour avec succès!", service.updateProfile(request, dto)), HttpStatus.OK);
	}

	@GetMapping(value = "/get/profile/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@SecurityRequirement(name = "Authorization")
	public ResponseEntity<RessourceResponse<UserResponse>> getProfile(@NotNull @PathVariable("accountId") Long accountId) {
		authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
		return new ResponseEntity<>(new RessourceResponse<>("Profile retrouvé avec succès!", service.getProfile(accountId)), HttpStatus.OK);
	}

	@PutMapping(value = "/change/password", produces = MediaType.APPLICATION_JSON_VALUE)
	@SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<AccountResponse>> changePassword(@Valid @RequestBody ChangePasswordRequest userFormPasswordRequest) {
		return new ResponseEntity<>(new RessourceResponse<AccountResponse>("Mot de passe utilisateur mis à jour avec succès!", service.changePassword(userFormPasswordRequest)), HttpStatus.OK);
	}

	@DeleteMapping(value = "/suspend/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<AccountResponse>> suspend(@NotNull @PathVariable("userId") Long userId) {
		authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.ACTIVE_ACCOUNT_PERMISSION));
        return new ResponseEntity<>(new RessourceResponse<AccountResponse>("Utilisateur suspendu avec succès!", service.suspend(userId)), HttpStatus.OK);
	}

	@GetMapping(value = "/find/by/role/{roleId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@SecurityRequirement(name = "Authorization")
	public ResponseEntity<RessourceResponse<List<AccountResponse>>> getUserWithRolesById(@NotNull @PathVariable("roleId") Long roleId) {
		authorizationService.checkIfHasDroit(new DroitAddRequest(MODULE_NAME, AppConstants.READ_PERMISSION));
		return new ResponseEntity<>(new RessourceResponse<>("Utilisateur retrouvé avec succès!", service.getUserWithRolesById(roleId)), HttpStatus.OK);
	}
}