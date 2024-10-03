package com.teleo.manager.authentification.controllers;

import com.teleo.manager.authentification.dto.reponse.AccountResponse;
import com.teleo.manager.authentification.dto.reponse.UserResponse;
import com.teleo.manager.authentification.dto.request.UserRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import com.teleo.manager.authentification.dto.request.AccountRequest;
import com.teleo.manager.authentification.dto.request.ChangePasswordRequest;
import com.teleo.manager.authentification.entities.Account;
import com.teleo.manager.generic.controller.ControllerGeneric;
import com.teleo.manager.generic.dto.reponse.RessourceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface AccountController extends ControllerGeneric<AccountRequest, AccountResponse, Account> {
	ResponseEntity<RessourceResponse<AccountResponse>> getCurrentUser();
	ResponseEntity<RessourceResponse<Boolean>> logout();
	ResponseEntity<RessourceResponse<UserResponse>> updateProfile(@Valid @RequestBody UserRequest dto, HttpServletRequest request);
	ResponseEntity<RessourceResponse<UserResponse>> getProfile(@NotNull @PathVariable("accountId") Long accountId);
	ResponseEntity<RessourceResponse<AccountResponse>> changePassword(@Valid @RequestBody ChangePasswordRequest dto);
	ResponseEntity<RessourceResponse<AccountResponse>> suspend(@NotNull @PathVariable("userId") Long userId);
	ResponseEntity<RessourceResponse<List<AccountResponse>>> getUserWithRolesById(@NotNull @PathVariable("roleId") Long roleId);
}