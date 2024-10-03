package com.teleo.manager.authentification.controllers;

import com.teleo.manager.authentification.dto.reponse.AccountResponse;
import com.teleo.manager.authentification.dto.reponse.UserResponse;
import com.teleo.manager.authentification.dto.reponse.VerificationTokenResponse;
import com.teleo.manager.authentification.dto.request.ResetPasswordRequest;
import com.teleo.manager.authentification.dto.request.ScanRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.teleo.manager.authentification.dto.reponse.JwtAuthenticationResponse;
import com.teleo.manager.authentification.dto.request.LoginRequest;
import com.teleo.manager.authentification.dto.request.SignupRequest;
import com.teleo.manager.authentification.services.AccountService;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RefreshScope
@ResponseBody
@RestController
@CrossOrigin("*")
@RequestMapping("/api/authentifications")
@Tag(name = "Authentifications", description = "API de gestion des authentifications")
public class AuthentificationController {

    private final AccountService accountService;

    public AuthentificationController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<JwtAuthenticationResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtAuthenticationResponse jwtDto = accountService.login(loginRequest);
        return new ResponseEntity<>(new RessourceResponse<JwtAuthenticationResponse>("L'utilisateur s'est connecté avec succès!", jwtDto), HttpStatus.OK);
    }

    @PostMapping(value = "/usingqr/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<JwtAuthenticationResponse>> verifyCode(@NotNull @PathVariable(value = "email") String email) {
        JwtAuthenticationResponse jwtDto = accountService.loginUsingQrCode(email);
        return new ResponseEntity<>(new RessourceResponse<JwtAuthenticationResponse>("L'utilisateur s'est connecté avec succès!", jwtDto), HttpStatus.OK);
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<AccountResponse>> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return new ResponseEntity<>(new RessourceResponse<AccountResponse>("Utilisateur enregistré avec succès!", accountService.register(signUpRequest)), HttpStatus.OK);
    }

    @PostMapping(value = "/scan/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<UserResponse>> registerByCni(@RequestBody ScanRequest signUpRequest) {
        return new ResponseEntity<>(new RessourceResponse<UserResponse>("Utilisateur enregistré avec succès!", accountService.registerByCni(signUpRequest)), HttpStatus.OK);
    }

    @PostMapping(value = "/token/verify/{token}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<VerificationTokenResponse>> confirmRegistration(@NotNull @PathVariable(value = "token") String token) {
        VerificationTokenResponse result = accountService.validateVerificationToken(token);
        return new ResponseEntity<>(new RessourceResponse<VerificationTokenResponse>("Vérification de l'enregistrement avec succès!", result), HttpStatus.OK);
    }

    @PostMapping(value = "/token/resend/{token}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<Boolean>> resendRegistrationToken(@NotNull @PathVariable(value = "token") String token) {
        Boolean isVerify = accountService.resendVerificationToken(token);
        if (!isVerify) {
            return new ResponseEntity<>(new RessourceResponse<Boolean>("Jeton introuvable!!", isVerify), HttpStatus.OK);
        }
        return new ResponseEntity<>(new RessourceResponse<Boolean>("Renvoyer le jeton d'enregistrement avec succès!", isVerify), HttpStatus.OK);
    }

    @PostMapping(value = "/password/forgot/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<Boolean>> forgotPassword(@NotNull @PathVariable(value = "email") String email) {
        return new ResponseEntity<>(new RessourceResponse<Boolean>("Jeton de mot de passe oublié avec succès!", accountService.forgotPassword(email)), HttpStatus.OK);
    }

    @PostMapping(value = "/password/reset", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<Boolean>> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return new ResponseEntity<>(new RessourceResponse<Boolean>("Réinitialiser le mot de passe avec succès!", accountService.resetPassword(resetPasswordRequest)), HttpStatus.OK);
    }
}