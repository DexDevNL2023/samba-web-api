package com.teleo.manager.authentification.services.impl;

import com.teleo.manager.assurance.dto.request.AssureRequest;
import com.teleo.manager.assurance.entities.Assure;
import com.teleo.manager.assurance.enums.Gender;
import com.teleo.manager.assurance.repositories.AssureRepository;
import com.teleo.manager.authentification.dto.reponse.AccountResponse;
import com.teleo.manager.authentification.dto.reponse.JwtAuthenticationResponse;
import com.teleo.manager.authentification.dto.reponse.UserResponse;
import com.teleo.manager.authentification.dto.reponse.VerificationTokenResponse;
import com.teleo.manager.authentification.dto.request.*;
import com.teleo.manager.authentification.entities.*;
import com.teleo.manager.authentification.enums.Authority;
import com.teleo.manager.authentification.mapper.AccountMapper;
import com.teleo.manager.authentification.repositories.AccountRepository;
import com.teleo.manager.authentification.repositories.VerifyTokenRepository;
import com.teleo.manager.authentification.security.jwt.JwtUtils;
import com.teleo.manager.authentification.services.AccountService;
import com.teleo.manager.authentification.services.PermissionService;
import com.teleo.manager.generic.email.MailService;
import com.teleo.manager.generic.exceptions.RessourceNotFoundException;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.ImageService;
import com.teleo.manager.generic.service.OcrService;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;
import com.teleo.manager.generic.utils.AppConstants;
import com.teleo.manager.generic.utils.GenericUtils;
import com.teleo.manager.notification.entities.Notification;
import com.teleo.manager.notification.enums.TypeNotification;
import com.teleo.manager.notification.repositories.NotificationRepository;
import com.teleo.manager.prestation.entities.DossierMedical;
import com.teleo.manager.prestation.entities.Fournisseur;
import com.teleo.manager.prestation.repositories.DossierMedicalRepository;
import com.teleo.manager.prestation.repositories.FournisseurRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Slf4j
@Service
@Transactional
public class AccountServiceImpl extends ServiceGenericImpl<AccountRequest, AccountResponse, Account> implements AccountService, UserDetailsService {

    private final AccountRepository repository;
    private final PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);
    private final MailService mailService;
    private final JwtUtils jwtUtils;
    private final VerifyTokenRepository tokenRepository;
    private final PermissionService permissionService;
    private final AccountMapper mapper;
    private final NotificationRepository notificationRepository;
    private final AssureRepository assureRepository;
    private final FournisseurRepository fournisseurRepository;
    private final DossierMedicalRepository dossierMedicalRepository;
    private final ImageService imageService;
    private final OcrService ocrService;

    public AccountServiceImpl(AccountRepository repository, AccountMapper mapper, MailService mailService, JwtUtils jwtUtils, VerifyTokenRepository tokenRepository, PermissionService permissionService, NotificationRepository notificationRepository, AssureRepository assureRepository, FournisseurRepository fournisseurRepository, DossierMedicalRepository dossierMedicalRepository, ImageService imageService, OcrService ocrService) {
        super(Account.class, repository, mapper);
        this.repository = repository;
        this.mailService = mailService;
        this.jwtUtils = jwtUtils;
        this.tokenRepository = tokenRepository;
        this.permissionService = permissionService;
        this.mapper = mapper;
        this.notificationRepository = notificationRepository;
        this.assureRepository = assureRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.dossierMedicalRepository = dossierMedicalRepository;
        this.imageService = imageService;
        this.ocrService = ocrService;
    }

    @Transactional
    @LogExecution
    @Override
    public AccountResponse register(SignupRequest dto) {
        log.debug("Register avec "+dto.getUsingQr());
        //Verifying whether account already exists
        if (repository.existsByEmail(dto.getEmail()))
            throw new RessourceNotFoundException("Un compte existe déjà avec cette e-mail " + dto.getEmail());
        log.debug("1");
        if (!dto.getEmail().isEmpty() && !GenericUtils.isValidEmailAddress(dto.getEmail()))
            throw new RessourceNotFoundException("L'email " + dto.getEmail() + " est invalide.");
        log.debug("2");
        // Create new account's account
        // Mapper Dto
        Account newAccount = mapper.toEntity(dto);

        // Traitement de l'image
        if (dto.getImageUrl() != null && !dto.getImageUrl().isEmpty()) {
            // Logique pour gérer l'image (enregistrer, générer un nom de fichier, etc.)
            String imagePath = imageService.saveBase64File(dto.getImageUrl());
            // On construit l'url absolue du fichier
            newAccount.setImageUrl(imagePath);
        }

        log.debug("3");
        String accountPassword = bCryptPasswordEncoder.encode(dto.getPassword());
        log.debug("4");
        // le mot de passe est vide, donc le compte a été crée par quelqu'un d'autre et c'est sa première connexion
        if (dto.getGeneratePassword() || dto.getPassword().isEmpty() || dto.getPassword().equals(bCryptPasswordEncoder.encode(""))) {
            // on chiffre et enregistre le mot de passe envoyé
            newAccount.setPassword(bCryptPasswordEncoder
                        .encode(GenericUtils.generatedPassWord()));
            log.debug("5");
        } else {
            newAccount.setPassword(accountPassword);
            log.debug("6");
        }
        // Genered url de login
        final String loginURL = GenericUtils.getServerAbsoluteUrl() + "/usingqr?login=" + newAccount.getEmail();
        log.debug("7");
        newAccount.setLoginUrl(loginURL);
        log.debug("8");
        // Build account and Create account's account
        newAccount = createDefaultRolesAndUsers(Authority.CLIENT, newAccount, GenericUtils.getDefaultClientRoles());
        log.debug("9 avec "+newAccount.getUsingQr());

        // Envoi de la notification ou du QR Code
        handlePostRegistration(newAccount);

        // Generer la notification de bienvenue
        Account systemAccount = repository.findById(AppConstants.SYSTEM_ACCOUNT_ID)
                .orElseThrow(() -> new RessourceNotFoundException("Compte système introuvable"));
        log.debug("14");

        generateNotification(systemAccount,
                newAccount,
                "Bienvenue",
                "Bienvenue chez SAMB'A Assurances! Nous sommes ravis de vous compter parmi nos assurés.",
                TypeNotification.INFO);
        log.debug("15");
        return mapper.toDto(newAccount);
    }

    private void handlePostRegistration(Account newAccount) {
        if (newAccount.getUsingQr()) {
            log.debug("10");
            // Send qrcode login with email
            mailService.sendQrCodeLogin(newAccount);
        } else {
            log.debug("11");
            // Genered token
            String token = GenericUtils.generateTokenNumber();
            log.debug("A");
            VerifyToken myToken = new VerifyToken(newAccount, token);
            log.debug("B");
            myToken.setUser(newAccount);
            tokenRepository.save(myToken);
            log.debug("C");
            // Send token activated with email
            mailService.sendVerificationToken(newAccount, token);
            log.debug("12");
        }
        log.debug("13");
    }

    @Transactional
    @LogExecution
    @Override
    public JwtAuthenticationResponse login(LoginRequest dto) {
        //Verifying whether account already exists
        if (!repository.existsByLogin(dto.getUsername()))
            throw new RessourceNotFoundException("Aucun compte n'existe avec le nom d'utilisateur : " + dto.getUsername());
        Account loginAccount = repository.findByLogin(dto.getUsername())
                .orElseThrow(() -> new RessourceNotFoundException("L'utilisateur" + dto.getUsername() + " est introuvable."));
        // On vérifie que le compte utilisateur est activé
        if (!loginAccount.getActived()) throw new RessourceNotFoundException("Le compte utilisateur n'est pas activé!");
        // le mot de passe est vide, donc le compte a été crée par quelqu'un d'autre et c'est sa première connexion
        // Check if the password matches
        if (!bCryptPasswordEncoder.matches(dto.getPassword(), loginAccount.getPassword())) {
            throw new RessourceNotFoundException("Incorrect username or password.");
        }
        // générer le JWT
        String jwt = jwtUtils.generateJwtTokens(loginAccount);
        // Create an Authentication object manually
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginAccount.getUsername(), loginAccount.getPassword(), loginAccount.getAuthorities());
        // Manually set the authentication in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Update account's account
        loginAccount.setAccesToken(jwt);
        loginAccount.setConnected(true);
        repository.save(loginAccount);
        // Mapper Dto avec l'attribut accesToken correctement rempli
        return new JwtAuthenticationResponse(jwt);
    }

    @Transactional
    @LogExecution
    @Override
    public JwtAuthenticationResponse loginUsingQrCode(String userName) {
        //Verifying whether account already exists
        if (!repository.existsByLogin(userName))
            throw new RessourceNotFoundException("Aucun compte n'existe avec le nom d'utilisateur : " + userName);
        Account loginAccount = repository.findByLogin(userName)
                .orElseThrow(() -> new RessourceNotFoundException("L'utilisateur" + userName + " est introuvable."));
        // On vérifie que le compte utilisateur est activé
        if (!loginAccount.getUsingQr())
            throw new RessourceNotFoundException("Compte utilisateur non autorisé à se connecter avec le code QR!");
        // On vérifie que le compte utilisateur est activé
        if (!loginAccount.getActived()) throw new RessourceNotFoundException("Le compte utilisateur n'est pas activé!");
        // générer le JWT
        String jwt = jwtUtils.generateJwtTokens(loginAccount);
        // Create an Authentication object manually
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginAccount.getUsername(), loginAccount.getPassword(), loginAccount.getAuthorities());
        // Manually set the authentication in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Update account's account
        loginAccount.setAccesToken(jwt);
        loginAccount.setConnected(true);
        repository.save(loginAccount);
        // Mapper Dto avec l'attribut accesToken correctement rempli
        return new JwtAuthenticationResponse(jwt);
    }

    @Transactional
    @LogExecution
    @Override
    public Boolean logout() {
        //Verifying whether account already exists
        Account logoutAccount = loadCurrentUser();
        // Update account's account
        logoutAccount.setAccesToken("");
        logoutAccount.setConnected(false);
        repository.save(logoutAccount);
        SecurityContextHolder.clearContext();
        return true;
    }

    @Transactional
    @LogExecution
    @Override
    public AccountResponse save(AccountRequest dto) {
        log.info("1");
        //Verifying whether account already exists
        if (repository.existsByEmail(dto.getEmail()))
            throw new RessourceNotFoundException("Cet e-mail est déjà utilisé par un autre utilisateur!");
        log.info("2");
        if (!dto.getEmail().isEmpty() && !GenericUtils.isValidEmailAddress(dto.getEmail()))
            throw new RessourceNotFoundException("L'email " + dto.getEmail() + " est invalide.");
        log.info("3");
        // Create new account's account
        // Mapper Dto
        Account newAccount = mapper.toEntity(dto);

        // Traitement de l'image
        if (dto.getImageUrl() != null && !dto.getImageUrl().isEmpty()) {
            // Logique pour gérer l'image (enregistrer, générer un nom de fichier, etc.)
            String imagePath = imageService.saveBase64File(dto.getImageUrl());
            // On construit l'url absolue du fichier
            newAccount.setImageUrl(imagePath);
        }

        log.info("4");
        newAccount.setPassword(bCryptPasswordEncoder.encode(""));
        log.info("5");
        // Genered url de login
        final String loginURL = GenericUtils.getServerAbsoluteUrl() + "/usingqr?login=" + newAccount.getEmail();
        newAccount.setLoginUrl(loginURL);
        log.info("6");
        // Build account and Create account's account
        log.info(dto.getAuthority() + "");
        switch (dto.getAuthority()) {
            case CLIENT:
                log.info("A");
                newAccount = createDefaultRolesAndUsers(Authority.CLIENT, newAccount, GenericUtils.getDefaultClientRoles());
                break;
            case AGENT:
                log.info("B");
                newAccount = createDefaultRolesAndUsers(Authority.AGENT, newAccount, GenericUtils.getDefaultAgentRoles());
                break;
            case ADMIN:
                log.info("C");
                newAccount = createDefaultRolesAndUsers(Authority.ADMIN, newAccount, GenericUtils.getDefaultAdminRoles());
                break;
            case PROVIDER:
                log.info("D");
                newAccount = createDefaultRolesAndUsers(Authority.PROVIDER, newAccount, GenericUtils.getDefaultFournisseurRoles());
                break;
            default:
                log.info("E");
                newAccount = createDefaultRolesAndUsers(Authority.CLIENT, newAccount, GenericUtils.getDefaultClientRoles());
                break;
        }

        // Envoi de la notification ou du QR Code
        handlePostRegistration(newAccount);

        // Generer la notification de bienvenue
        Account systemAccount = repository.findById(AppConstants.SYSTEM_ACCOUNT_ID)
                .orElseThrow(() -> new RessourceNotFoundException("Compte système introuvable"));

        generateNotification(systemAccount,
                newAccount,
                "Bienvenue",
                "Bienvenue chez SAMB'A Assurances! Nous sommes ravis de vous compter parmi nos assurés.",
                TypeNotification.INFO);
        return mapper.toDto(newAccount);
    }

    @Transactional
    @LogExecution
    @Override
    public AccountResponse update(AccountRequest dto, Long id) {
        //Verifying whether account already exists
        Account updatedAccount = getById(id);
        // Mapper Dto
        if (updatedAccount.equalsToDto(dto))
            throw new RessourceNotFoundException("L'utilisateur avec les données suivante : " + dto.toString() + " existe déjà");
        updatedAccount.update(mapper.toEntity(dto));

        // Traitement de l'image
        if (dto.getImageUrl() != null && !dto.getImageUrl().isEmpty()) {
            // Logique pour gérer l'image (enregistrer, générer un nom de fichier, etc.)
            String imagePath = imageService.saveBase64File(dto.getImageUrl());
            // On construit l'url absolue du fichier
            updatedAccount.setImageUrl(imagePath);
        }

        // Genered url de login
        final String loginURL = GenericUtils.getServerAbsoluteUrl() + "/usingqr?login=" + updatedAccount.getEmail();
        updatedAccount.setLoginUrl(loginURL);
        // Update account's
        updatedAccount = repository.save(updatedAccount);
        // Mapper Dto
        return mapper.toDto(updatedAccount);
    }

    @Transactional
    @LogExecution
    @Override
    public AccountResponse changePassword(ChangePasswordRequest dto) {
        //Verifying whether account already exists
        Account updatedAccount = loadCurrentUser();
        String lastPass = bCryptPasswordEncoder.encode(dto.getCurrentPassword());
        if (!lastPass.equals(updatedAccount.getPassword()))
            throw new RessourceNotFoundException("Votre mot de passe actuel est différent de celui que vous avez renseigner!");
        // Update account's account with new password
        updatedAccount.setPassword(bCryptPasswordEncoder.encode(dto.getNewPassword()));
        updatedAccount = repository.save(updatedAccount);
        // Mapper Dto
        return mapper.toDto(updatedAccount);
    }

    @Transactional
    @LogExecution
    @Override
    public AccountResponse suspend(Long id) {
        //Verifying whether account already exists
        if (!repository.existsById(id))
            throw new RessourceNotFoundException("L'utilisateur n'existe pas avec cet identifiant " + id);
        Account updatedAccount = repository.findById(id).orElseThrow(() -> new RessourceNotFoundException("L'utilisateur est introuvable."));
        // Update account's account status
        updatedAccount.setActived(false);
        updatedAccount = repository.save(updatedAccount);
        // Mapper Dto
        return mapper.toDto(updatedAccount);
    }

    @Transactional
    @LogExecution
    @Override
    public Account loadCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new RessourceNotFoundException("Impossible de retouver l'utilisateur courant!");
        }
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return repository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(() -> new RessourceNotFoundException("Aucun utilisateur n'existe avec le nom utilisateur " + userPrincipal.getUsername()));
    }

    @Transactional
    @LogExecution
    @Override
    public AccountResponse getCurrentUser() {
        //Verifying whether account already exists
        Account currentAccount = loadCurrentUser();
        // Mapper Dto
        return mapper.toDto(currentAccount);
    }

    @Transactional
    @LogExecution
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account accountUser = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Nom utilisateur invalide"));
        return new User(accountUser.getUsername(), accountUser.getPassword(), accountUser.getAuthorities());
    }

    @Transactional
    @LogExecution
    @Override
    public Account findByUsername(String email) {
        return repository.findByEmail(email)
                .orElse(null);
    }

    @Transactional
    @LogExecution
    @Override
    public Boolean resendVerificationToken(String existingVerificationToken) {
        VerifyToken vToken = tokenRepository.findByToken(existingVerificationToken);
        if (vToken != null) {
            // Genered token
            String token = GenericUtils.generateTokenNumber();
            vToken.updateToken(token);
            vToken = tokenRepository.save(vToken);
            // Send token activated with email
            mailService.sendVerificationToken(vToken.getUser(), vToken.getToken());
            return true;
        }
        return false;
    }

    @Transactional
    @LogExecution
    @Override
    public VerificationTokenResponse validateVerificationToken(String token) {
        VerifyToken vToken = tokenRepository.findByToken(token);
        if (vToken == null) {
            return new VerificationTokenResponse(AppConstants.TOKEN_INVALID);
        }
        Account account = vToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((vToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return new VerificationTokenResponse(AppConstants.TOKEN_EXPIRED);
        }
        account.setActived(true);
        tokenRepository.delete(vToken);
        repository.save(account);
        return new VerificationTokenResponse(AppConstants.TOKEN_VALID);
    }

    @Transactional
    @LogExecution
    @Override
    public Boolean forgotPassword(String email) {
        Account accountAccount = repository.findByEmail(email)
                .orElseThrow(() -> new RessourceNotFoundException("Impossible de trouver un compte utilisateur avec l'e-mail  " + email));
        // Genered token
        String token = GenericUtils.generateTokenNumber();
        accountAccount.setResetPasswordToken(token);
        accountAccount = repository.save(accountAccount);
        // Send token activated with email
        mailService.sendForgotPasswordToken(accountAccount, token);
        return true;
    }

    @Transactional
    @LogExecution
    @Override
    public Boolean resetPassword(ResetPasswordRequest resetPasswordRequest) {
        Account accountAccount = repository.findByResetPasswordToken(resetPasswordRequest.getToken())
                .orElseThrow(() -> new RessourceNotFoundException("Impossible de trouver un compte utilisateur avec le jeton " + resetPasswordRequest.getToken()));
        String encodedPassword = bCryptPasswordEncoder.encode(resetPasswordRequest.getNewPassword());
        accountAccount.setPassword(encodedPassword);
        accountAccount.setResetPasswordToken(null);
        accountAccount = repository.save(accountAccount);
        // Send token activated with email
        mailService.sendResetPassword(accountAccount);
        return true;
    }

    @Transactional
    @LogExecution
    @Override
    public List<AccountResponse> getUserWithRolesById(Long roleId) {
        return mapper.toDto(repository.findUserWithRolesById(roleId));
    }

    @Override
    public UserResponse registerByCni(ScanRequest signUpRequest) {
        try {
            // Appel du service OCR pour extraire les informations de la CNI
            AssureRequest assureRequest = ocrService.extractInfoFromCni(signUpRequest.getImageUrl());

            // Créer un nouveau compte utilisateur basé sur les informations extraites
            return saveProfile(signUpRequest.getEmail(), signUpRequest.getPassword(), signUpRequest.getGeneratePassword(), assureRequest);

        } catch (IOException | TesseractException e) {
            // Gérer l'exception ici, par exemple en journalisant l'erreur et en lançant une exception personnalisée
            throw new RuntimeException("Erreur lors de l'extraction des informations de la CNI : " + e.getMessage(), e);
        }
    }

    @Transactional
    @LogExecution
    public UserResponse saveProfile(String email, String password, Boolean generatePassword, AssureRequest dto) {
        // Vérifier si l'utilisateur existe déjà
        if (repository.existsByEmail(email))
            throw new RessourceNotFoundException("Un compte existe déjà avec cette e-mail " + email);
        log.debug("1");
        if (!email.isEmpty() && !GenericUtils.isValidEmailAddress(email))
            throw new RessourceNotFoundException("L'email " + email + " est invalide.");
        log.debug("2");

        // Créer un nouvel account et assure
        Account newAccount = new Account();
        newAccount.setFullName(dto.getFirstName() + " " + dto.getLastName());
        newAccount.setEmail(email);
        newAccount.setUsingQr(false);

        log.debug("3");
        String accountPassword = bCryptPasswordEncoder.encode(password);
        log.debug("4");
        // le mot de passe est vide, donc le compte a été crée par quelqu'un d'autre et c'est sa première connexion
        if (generatePassword || password.isEmpty() || password.equals(bCryptPasswordEncoder.encode(""))) {
            // on chiffre et enregistre le mot de passe envoyé
            newAccount.setPassword(bCryptPasswordEncoder
                    .encode(GenericUtils.generatedPassWord()));
            log.debug("5");
        } else {
            newAccount.setPassword(accountPassword);
            log.debug("6");
        }
        // Genered url de login
        final String loginURL = GenericUtils.getServerAbsoluteUrl() + "/usingqr?login=" + newAccount.getEmail();
        log.debug("7");
        newAccount.setLoginUrl(loginURL);
        log.debug("8");
        // Build account and Create account's account
        List<Role> roles = new ArrayList<>();
        DefaultRole[] defaultRoles = GenericUtils.getDefaultClientRoles();
        for (DefaultRole defaultRole : defaultRoles) {
            List<Permission> permissions = getPermissionsByIds(defaultRole.getPermissionIds());
            Role role = createRoleWithPermissions(defaultRole.getRoleKey(), defaultRole.getLibelle(), permissions);
            roles.add(role);
        }
        log.info("Created default roles {}", roles);

        newAccount.setAuthority(Authority.CLIENT);
        newAccount.setActived(true);
        newAccount.setConnected(true);
        // Ajouter les rôles à l'utilisateur
        newAccount.addRoles(roles);
        // on verifie que la langue est prise en co;pte dans l'application
        newAccount.setLangKey(GenericUtils.verifieFormatLangue("Fr"));

        // Sauvegarder les modifications du compte
        newAccount = repository.save(newAccount);
        log.debug("9 avec "+newAccount.getUsingQr());

        // Créer l'assuré lié
        Assure assure = new Assure();
        assure.setNumNiu(dto.getNumNiu());
        assure.setLastName(dto.getLastName());
        assure.setFirstName(dto.getFirstName());
        assure.setDateNaissance(dto.getDateNaissance());
        assure.setNumCni(dto.getNumCni());
        assure.setSexe(dto.getSexe());
        assure.setSignature(dto.getSignature());
        assure.setEmail(dto.getEmail());
        assure.setTelephone(dto.getTelephone());
        assure.setAdresse(dto.getAdresse());
        assure.setAccount(newAccount);
        assureRepository.save(assure);

        // Envoi de la notification ou du QR Code
        handlePostRegistration(newAccount);

        // Generer la notification de bienvenue
        Account systemAccount = repository.findById(AppConstants.SYSTEM_ACCOUNT_ID)
                .orElseThrow(() -> new RessourceNotFoundException("Compte système introuvable"));
        log.debug("14");

        generateNotification(systemAccount,
                newAccount,
                "Bienvenue",
                "Bienvenue chez SAMB'A Assurances! Nous sommes ravis de vous compter parmi nos assurés.",
                TypeNotification.INFO);
        log.debug("15");

        // Retourner la réponse de création d'utilisateur
        return buildUser(newAccount);
    }

    @Transactional
    @LogExecution
    @Override
    public Account saveDefault(Account newAccount, String password) {
        newAccount.setPassword(bCryptPasswordEncoder.encode(password));
        newAccount.setActived(true);
        newAccount.setConnected(true);
        newAccount.setUsingQr(false);
        newAccount = repository.save(newAccount);

        // Appel de buildProfile pour créer l'entité associée
        buildProfile(newAccount);
        return newAccount;
    }

    @Transactional
    //@LogExecution
    private Account createDefaultRolesAndUsers(Authority authority, Account newAccount, DefaultRole[] defaultRoles) {
        List<Role> roles = new ArrayList<>();
        for (DefaultRole defaultRole : defaultRoles) {
            List<Permission> permissions = getPermissionsByIds(defaultRole.getPermissionIds());
            Role role = createRoleWithPermissions(defaultRole.getRoleKey(), defaultRole.getLibelle(), permissions);
            roles.add(role);
        }
        log.info("Created default roles {}", roles);

        newAccount.setAuthority(authority);
        newAccount.setActived(true);
        newAccount.setConnected(true);
        // Ajouter les rôles à l'utilisateur
        newAccount.addRoles(roles);
        // on verifie que la langue est prise en co;pte dans l'application
        newAccount.setLangKey(GenericUtils.verifieFormatLangue("Fr"));
        newAccount = repository.save(newAccount);

        // Appel de buildProfile pour créer l'entité associée
        buildProfile(newAccount);
        log.info("Account {}", newAccount);
        return newAccount;
    }

    @Transactional
    public void buildProfile(Account account) {
        log.info("Build {}", account);
        switch (account.getAuthority()) {
            case CLIENT:
                // Generer la notification de bienvenue
                Account systemAccount = repository.findById(AppConstants.SYSTEM_ACCOUNT_ID)
                        .orElseThrow(() -> new RessourceNotFoundException("Compte système introuvable"));
                log.debug("14");

                generateNotification(systemAccount,
                        account,
                        "Bienvenue",
                        "Bienvenue chez SAMB'A Assurances! Nous sommes ravis de vous compter parmi nos assurés.",
                        TypeNotification.INFO);
                log.debug("15");

                initializeAssure(account);
                break;

            case AGENT, ADMIN, SYSTEM:
                // Pour les agents et les administrateurs, nous n'avons pas de détails supplémentaires à mettre à jour
                // ici, car ces rôles sont gérés au niveau du compte
                break;

            case PROVIDER:
                initializeFournisseur(account);
                break;

            default:
                throw new UnsupportedOperationException("Profile non pris en charge");
        }
    }

    private void initializeAssure(Account account) {
        Assure assure = new Assure();
        assure.setAccount(account);
        assure.setNumNiu(GenericUtils.generateNumNiu()); // Générer un NUI unique
        assure.setLastName(account.getFullName()); // Initialiser avec des données réelles
        assure.setFirstName("");
        assure.setDateNaissance(LocalDate.now()); // Exemple de date, mettre une date réelle
        assure.setNumCni(GenericUtils.generateNumCni()); // Générer un CNI unique
        assure.setSexe(Gender.MALE); // Choisir selon besoin
        assure.setEmail(account.getEmail());
        assure.setTelephone("123456789");
        assure.setAdresse("Address example");
        assure.setSignature("Signature example");
        assure = assureRepository.save(assure);

        createDefaultDossierMedicalForAssure(assure);
    }

    public DossierMedical createDefaultDossierMedicalForAssure(Assure assure) {
        // Créer un objet DossierMedical par défaut
        DossierMedical dossierMedical = new DossierMedical();
        dossierMedical.setNumDossierMedical("DM-2024-00123"); // Générer un numéro unique si nécessaire
        dossierMedical.setPatient(assure);
        dossierMedical.setDateUpdated(LocalDate.now());
        dossierMedical.setMaladiesChroniques("Diabète de type 2, Hypertension");
        dossierMedical.setMaladiesHereditaires("Antécédents familiaux de diabète et de cancer");
        dossierMedical.setInterventionsChirurgicales("Appendicectomie en 2010");
        dossierMedical.setHospitalisations("Hospitalisation pour pneumonie en 2018");
        dossierMedical.setAllergies("Allergie aux arachides, intolérance au lactose");
        dossierMedical.setVaccins("Vaccin contre la grippe (2024), Hépatite B (2023)");
        dossierMedical.setHabitudesAlimentaires("Régime méditerranéen, faible consommation de sucre");
        dossierMedical.setConsommationAlcool("Consommation occasionnelle");
        dossierMedical.setConsommationTabac("Non-fumeur");
        dossierMedical.setNiveauActivitePhysique("Modéré, pratique de la marche 3 fois par semaine");
        dossierMedical.setRevenusAnnuels(new BigDecimal(30000));
        dossierMedical.setChargesFinancieres(new BigDecimal(15000));
        dossierMedical.setDeclarationBonneSante(true);
        dossierMedical.setConsentementCollecteDonnees(true);
        dossierMedical.setDeclarationNonFraude(true);

        // Sauvegarder le dossier médical dans la base de données
        return dossierMedicalRepository.save(dossierMedical);
    }

    private void initializeFournisseur(Account account) {
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setAccount(account);
        fournisseur.setNom(account.getFullName());
        fournisseur.setTelephone("123456789");
        fournisseur.setEmail(account.getEmail());
        fournisseur.setAdresse("Address example");
        fournisseur.setServicesFournis("Services example");
        fournisseurRepository.save(fournisseur);
    }

    @Transactional
    @LogExecution
    private List<Permission> getPermissionsByIds(Long[] permissionIds) {
        List<Permission> permissions = new ArrayList<>();
        for (Long permissionId : permissionIds) {
            Permission permission = permissionService.getById(permissionId);
            if (permission != null) {
                permissions.add(permission);
            }
        }
        return permissions;
    }

    @Transactional
    @LogExecution
    private Role createRoleWithPermissions(String roleKey, String libelle, List<Permission> permissions) {
        Role role = new Role();
        role.setRoleKey(roleKey);
        role.setLibelle(libelle);
        role.setPermissions(permissions);
        return role; // Retourne l'objet Role sans persistance ici
    }

    @Transactional
    @LogExecution
    @Override
    public UserResponse updateProfile(HttpServletRequest request, UserRequest dto) {
        // Récupérer l'utilisateur à partir de la requête
        String email = request.getUserPrincipal().getName();
        Account account = repository.findByEmail(email)
                .orElseThrow(() -> new RessourceNotFoundException("Compte non trouvé"));

        // Mettre à jour les informations du compte utilisateur
        account.setFullName(dto.getFullName());
        account.setEmail(dto.getEmail());
        account.setImageUrl(dto.getImageUrl());
        account.setLangKey(dto.getLangKey());
        account.setActived(dto.isActived());
        account.setUsingQr(dto.isUsingQr());

        // Sauvegarder les modifications du compte
        account = repository.save(account);

        // Mettre à jour les détails spécifiques en fonction du rôle
        switch (account.getAuthority()) {
            case CLIENT:
                Assure assure = assureRepository.findAssureByAccountId(account.getId()).orElse(null);
                if (assure != null) {
                    assure.setNumNiu(dto.getNumNiu());
                    assure.setLastName(dto.getLastName());
                    assure.setFirstName(dto.getFirstName());
                    assure.setDateNaissance(dto.getDateNaissance());
                    assure.setNumCni(dto.getNumCni());
                    assure.setSexe(dto.getSexe());
                    assure.setSignature(dto.getSignature());
                    assure.setEmail(dto.getEmail());
                    assure.setTelephone(dto.getTelephone());
                    assure.setAdresse(dto.getAdresse());
                    assure.setAccount(account);
                    assureRepository.save(assure);
                }
                break;

            case AGENT, ADMIN, SYSTEM:
                // Pour les agents et les administrateurs, nous n'avons pas de détails supplémentaires à mettre à jour
                // ici, car ces rôles sont gérés au niveau du compte
                break;

            case PROVIDER:
                Fournisseur fournisseur = fournisseurRepository.findByAccountId(account.getId()).orElse(null);
                if (fournisseur != null) {
                    fournisseur.setNom(dto.getNom());
                    fournisseur.setServicesFournis(dto.getServicesFournis());
                    fournisseur.setEmail(dto.getEmail());
                    fournisseur.setTelephone(dto.getTelephone());
                    fournisseur.setAdresse(dto.getAdresse());
                    fournisseur.setAccount(account);
                    fournisseurRepository.save(fournisseur);
                }
                break;

            default:
                throw new UnsupportedOperationException("Profile non pris en charge");
        }

        // Retourner une réponse avec les informations mises à jour
        return buildUser(account);
    }

    @Override
    public UserResponse getProfile(Long accountId) {
        Account account = repository.findById(accountId)
                .orElseThrow(() -> new RessourceNotFoundException("Compte avec l'ID " + accountId + " non trouvé"));

        // Retourner une réponse avec les informations mises à jour
        return buildUser(account);
    }

    @Transactional
    @LogExecution
    private UserResponse buildUser(Account account) {
        // Initialiser le UserResponse avec les informations de l'Account
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(account.getEmail());
        userResponse.setImageUrl(account.getImageUrl());
        userResponse.setLangKey(account.getLangKey());
        userResponse.setLogin(account.getLogin());
        userResponse.setActived(account.getActived());
        userResponse.setAuthority(account.getAuthority());
        userResponse.setFullName(account.getFullName());

        // Mettre à jour les détails spécifiques en fonction du rôle
        switch (account.getAuthority()) {
            case CLIENT:
                Assure assure = assureRepository.findAssureByAccountId(account.getId()).orElse(null);
                if (assure != null) {
                    userResponse.setNumNiu(assure.getNumNiu());
                    userResponse.setLastName(assure.getLastName());
                    userResponse.setFirstName(assure.getFirstName());
                    userResponse.setDateNaissance(assure.getDateNaissance());
                    userResponse.setNumCni(assure.getNumCni());
                    userResponse.setSexe(assure.getSexe());
                    userResponse.setSignature(assure.getSignature());
                    userResponse.setTelephone(assure.getTelephone());
                    userResponse.setAdresse(assure.getAdresse());
                }
                break;

            case PROVIDER:
                Fournisseur fournisseur = fournisseurRepository.findByAccountId(account.getId()).orElse(null);
                if (fournisseur != null) {
                    userResponse.setNom(fournisseur.getNom());
                    userResponse.setServicesFournis(fournisseur.getServicesFournis());
                    userResponse.setTelephone(fournisseur.getTelephone());
                    userResponse.setAdresse(fournisseur.getAdresse());
                }
                break;

            // Pas de détails supplémentaires pour AGENT et ADMIN
            case AGENT, ADMIN, SYSTEM:
                // Pour les agents et les administrateurs, nous n'avons pas de détails supplémentaires à mettre à jour
                // ici, car ces rôles sont gérés au niveau du compte
                break;

            default:
                throw new UnsupportedOperationException("Profile non pris en charge");
        }

        return userResponse;
    }

    @Transactional
    @LogExecution
    public void generateNotification(Account emetteur, Account destinataire, String titre, String message, TypeNotification type) {
        log.debug("Generating notification");
        log.debug("Titre : " + titre);
        log.debug("Message : " + message);

        try {
            Notification notification = new Notification();
            notification.setTitre(titre);
            notification.setMessage(message);
            notification.setType(type);
            notification.setDateEnvoi(LocalDateTime.now());

            Account systemAccount = repository.findById(AppConstants.SYSTEM_ACCOUNT_ID)
                    .orElseThrow(() -> new RessourceNotFoundException("Compte système introuvable"));

            notification.setEmetteur(emetteur != null ? emetteur : systemAccount);
            notification.setDestinataire(destinataire != null ? destinataire : systemAccount);
            log.debug("Notification to save {}", notification);

            notification = notificationRepository.save(notification);
            log.debug("Notification saved {}", notification);
        } catch (Exception e) {
            log.error("Erreur lors de la génération de la notification", e);
        }
    }
}
