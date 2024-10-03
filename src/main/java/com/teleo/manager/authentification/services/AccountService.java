package com.teleo.manager.authentification.services;

import com.teleo.manager.authentification.dto.reponse.AccountResponse;
import com.teleo.manager.authentification.dto.reponse.JwtAuthenticationResponse;
import com.teleo.manager.authentification.dto.reponse.VerificationTokenResponse;
import com.teleo.manager.authentification.dto.request.*;
import com.teleo.manager.authentification.entities.Account;
import com.teleo.manager.generic.service.ServiceGeneric;

import com.teleo.manager.authentification.dto.reponse.UserResponse;
import com.teleo.manager.generic.exceptions.RessourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

public interface AccountService extends ServiceGeneric<AccountRequest, AccountResponse, Account>, UserDetailsService {
    AccountResponse register(SignupRequest dto);
    JwtAuthenticationResponse login(LoginRequest dto);
    JwtAuthenticationResponse loginUsingQrCode(String email);
    Boolean logout();
    UserResponse updateProfile(HttpServletRequest request, UserRequest dto);
    UserResponse getProfile(Long accountId);
    AccountResponse changePassword(ChangePasswordRequest dto);
    AccountResponse suspend(Long id);
    Account loadCurrentUser();
    AccountResponse getCurrentUser();
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
    Account findByUsername(String email);
    Boolean resendVerificationToken(String existingVerificationToken);
    VerificationTokenResponse validateVerificationToken(String token);
    Boolean forgotPassword(String email);
    Boolean resetPassword(ResetPasswordRequest resetPasswordRequest);
    Account saveDefault(Account newAccount, String password);
    List<AccountResponse> getUserWithRolesById(Long roleId);
    UserResponse registerByCni(ScanRequest signUpRequest);
}