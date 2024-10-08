package com.teleo.manager.generic.config;

import com.teleo.manager.assurance.entities.Assurance;
import com.teleo.manager.assurance.entities.Garantie;
import com.teleo.manager.assurance.entities.PoliceAssurance;
import com.teleo.manager.assurance.enums.GarantieStatus;
import com.teleo.manager.assurance.enums.InsuranceType;
import com.teleo.manager.assurance.services.AssuranceService;
import com.teleo.manager.assurance.services.GarantieService;
import com.teleo.manager.assurance.services.PoliceAssuranceService;
import com.teleo.manager.authentification.entities.Account;
import com.teleo.manager.authentification.entities.DefaultRole;
import com.teleo.manager.authentification.entities.Permission;
import com.teleo.manager.authentification.entities.Role;
import com.teleo.manager.authentification.enums.Authority;
import com.teleo.manager.authentification.services.AccountService;
import com.teleo.manager.authentification.services.PermissionService;
import com.teleo.manager.generic.utils.AppConstants;
import com.teleo.manager.generic.utils.GenericUtils;
import com.teleo.manager.parametre.entities.Company;
import com.teleo.manager.parametre.services.CompanyService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;
    private final AccountService accountService;
    private final PermissionService permissionService;
    private final CompanyService companyService;
    private final AssuranceService assuranceService;
    private final PoliceAssuranceService policeAssuranceService;
    private final GarantieService garantieService;

    public SetupDataLoader(AccountService accountService, PermissionService permissionService, CompanyService companyService, AssuranceService assuranceService, PoliceAssuranceService policeAssuranceService, GarantieService garantieService) {
        this.accountService = accountService;
        this.permissionService = permissionService;
        this.companyService = companyService;
        this.assuranceService = assuranceService;
        this.policeAssuranceService = policeAssuranceService;
        this.garantieService = garantieService;
    }

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        alreadySetup = true;

        addDefaultCompany();
        addDefaultPermissions();
        addDefaultUsers();
        addDefaultInsurancesAndPolicies();
    }

    @Transactional
    private void addDefaultCompany() {
        // Check if there is any company in the database
        if (companyService.findFirstCompany() == null) {
            Company defaultCompany = new Company();
            defaultCompany.setName("SAMB'A Assurances Gabon S.A");
            defaultCompany.setSigle("SAMB'A");
            defaultCompany.setEmail("contact@samba-assurances.ga");
            defaultCompany.setTelephone("+241 01 23 45 67");
            defaultCompany.setSite("www.samba-assurances.ga");
            defaultCompany.setTelephone2("+241 01 23 45 68");
            defaultCompany.setAdresse("9137 3rd Lane");
            defaultCompany.setVille("California City");
            defaultCompany.setBp("93504");
            defaultCompany.setLogo("assets/layout/images/logo-couleur.png");
            defaultCompany.setEnteteGauche("SAMB'A Assurances Gabon S.A;9137 3rd Lane;California City, CA 93504;U.S.A.");
            defaultCompany.setEnteteDroite("TEL: +241 01 23 45 67;P.B: 93504;California City;U.S.A.");
            defaultCompany.setPiedPage("© 2024 SAMB'A Assurances Gabon S.A. All rights reserved.");

            companyService.saveDefault(defaultCompany);
        }
    }

    @Transactional
    private void addDefaultPermissions() {
        Permission[] defaultPermissions = {
            new Permission(1L, AppConstants.WRITE_PERMISSION, "Autorisation de ajouter"),
            new Permission(2L, AppConstants.EDIT_PERMISSION, "Autorisation de modifier"),
            new Permission(3L, AppConstants.DELET_PERMISSION, "Autorisation de supprimer"),
            new Permission(4L, AppConstants.READ_PERMISSION, "Autorisation de consulter"),
            new Permission(5L, AppConstants.PRINT_PERMISSION, "Autorisation de imprimer"),
            new Permission(6L, AppConstants.ACTIVE_ACCOUNT_PERMISSION, "Autorisation d'activé ou désactivé un compte"),
            new Permission(7L, AppConstants.CHANGE_PERMISSION, "Autorisation de changer les permission d'un compte")
        };

        for (Permission defaultPermission : defaultPermissions) {
            if (permissionService.findByPermissionKey(defaultPermission.getPermissionKey()) == null) {
                permissionService.saveDefault(defaultPermission);
            }
        }
    }

    @Transactional
    public void addDefaultUsers() {
        // Create default system account if it does not exist
        createDefaultSystemAccount();

        // Création des rôles et des utilisateurs par défaut
        createDefaultRolesAndUsers(Authority.ADMIN, "Victor Nlang", "admin", "admin@example.com", "admin", GenericUtils.getDefaultAdminRoles());
        createDefaultRolesAndUsers(Authority.AGENT, "Jane Smith", "agent", "agent@example.com", "agent", GenericUtils.getDefaultAgentRoles());
        createDefaultRolesAndUsers(Authority.CLIENT, "John Doe", "client", "client@example.com", "client", GenericUtils.getDefaultClientRoles());
        createDefaultRolesAndUsers(Authority.PROVIDER, "Care Provider", "fournisseur", "fournisseur@example.com", "fournisseur", GenericUtils.getDefaultFournisseurRoles());
    }

    private void createDefaultSystemAccount() {
        // Check if the system account exists
        if (accountService.getById(AppConstants.SYSTEM_ACCOUNT_ID) == null) {
            Account systemAccount = new Account();
            systemAccount.setId(AppConstants.SYSTEM_ACCOUNT_ID);
            systemAccount.setFullName("System");
            systemAccount.setLogin("system");
            systemAccount.setEmail("system@example.com");
            systemAccount.setAuthority(Authority.SYSTEM);
            systemAccount.setActived(true);
            systemAccount.setLangKey("Fr");
            systemAccount.setUsingQr(false);
            systemAccount.setLoginUrl(GenericUtils.getServerAbsoluteUrl() + "/usingqr?login=system@example.com");

            accountService.saveDefault(systemAccount, "password"); // Set a default password
        }
    }

    private void createDefaultRolesAndUsers(Authority authority, String fullName, String login, String email, String password, DefaultRole[] defaultRoles) {
        List<Role> roles = new ArrayList<>();
        for (DefaultRole defaultRole : defaultRoles) {
            List<Permission> permissions = getPermissionsByIds(defaultRole.getPermissionIds());
            Role role = createRoleWithPermissions(defaultRole.getRoleKey(), defaultRole.getLibelle(), permissions);
            roles.add(role);
        }
        if (accountService.findByUsername(email) == null) {
            Account newAccount = new Account();
            newAccount.setFullName(fullName);
            newAccount.setLogin(login);
            newAccount.setEmail(email);
            newAccount.setAuthority(authority);
            // Ajouter les rôles à l'utilisateur
            newAccount.addRoles(roles);
            // on verifie que la langue est prise en co;pte dans l'application
            newAccount.setLangKey(GenericUtils.verifieFormatLangue("Fr"));
            // Genered url de login
            final String loginURL = GenericUtils.getServerAbsoluteUrl() + "/usingqr?login=" + email;
            newAccount.setLoginUrl(loginURL);
            accountService.saveDefault(newAccount, password);
        }
    }

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

    private Role createRoleWithPermissions(String roleKey, String libelle, List<Permission> permissions) {
        Role role = new Role();
        role.setRoleKey(roleKey);
        role.setLibelle(libelle);
        role.setPermissions(permissions);
        return role; // Retourne l'objet Role sans persistance ici
    }

    @Transactional
    public void addDefaultInsurancesAndPolicies() {
        // Liste des types d'assurance à ajouter
        InsuranceType[] insuranceTypes = {
                InsuranceType.PERSONNE, InsuranceType.BIEN, InsuranceType.AGRICOLE,
                InsuranceType.AUTOMOBILE, InsuranceType.HABITATION, InsuranceType.VIE,
                InsuranceType.ACCIDENT, InsuranceType.VOYAGE, InsuranceType.SANTE
        };

        // Pour chaque type d'assurance, créer des polices et leurs garanties associées
        for (InsuranceType insuranceType : insuranceTypes) {
            Assurance assurance = createDefaultAssurance(insuranceType);
            switch (insuranceType) {
                case PERSONNE :
                    break;

                case BIEN :
                    addSambABoxPolicy();
                    break;

                case AGRICOLE :
                    addSambAAgrikolPolicy();
                    break;

                case AUTOMOBILE :
                    break;

                case HABITATION :
                    addSambAElectrikPolicy();
                    addSambANkamaPolicy();
                    break;

                case VIE :
                    addSambAPreventionPolicy();
                    addSambAParrainePolicy();
                    addSambaProtegePolicy();
                    addSambADiasporaPolicy();
                    break;


                case ACCIDENT :
                    break;

                case VOYAGE :
                    break;

                case SANTE :
                    addSambAHospitPolicy();
                    addSambASantePolicy();
                    break;
            }
        }
    }

    // Créer une assurance par défaut en fonction du type
    private Assurance createDefaultAssurance(InsuranceType insuranceType) {
        Assurance assurance = assuranceService.findByType(insuranceType);

        if (assurance == null) {
            assurance = new Assurance();
            assurance.setNom(getAssuranceLabel(insuranceType));
            assurance.setType(insuranceType);
            assurance.setDescription(getAssuranceDescription(insuranceType));
            assurance = assuranceService.saveDefault(assurance);
        }
        return assurance;
    }

    // Méthode pour obtenir le libellé d'une assurance
    private String getAssuranceLabel(InsuranceType insuranceType) {
        return switch (insuranceType) {
            case PERSONNE -> "Protection individuelle";
            case BIEN -> "Sécurité des biens de valeur";
            case AGRICOLE -> "Protection des récoltes et du bétail";
            case AUTOMOBILE -> "Assurance Véhicule Essentiel";
            case HABITATION -> "Protection du logement";
            case VIE -> "Plan d'épargne pour l'avenir";
            case ACCIDENT -> "Assistance en cas d'accident";
            case VOYAGE -> "Couverture de déplacement";
            case SANTE -> "Couverture Santé de Base";
            default -> "Assurance";
        };
    }

    // Méthode pour obtenir la description d'une assurance
    private String getAssuranceDescription(InsuranceType insuranceType) {
        return switch (insuranceType) {
            case PERSONNE ->
                    "Offre de protection pour l'intégrité physique de l'assuré, couvrant des événements tels que l'invalidité temporaire, la perte de revenu due à une maladie ou un accident mineur.";
            case BIEN ->
                    "Couverture pour les pertes ou dommages causés aux biens personnels ou professionnels de petite valeur, comme les équipements agricoles, les outils de travail ou les marchandises stockées.";
            case AGRICOLE ->
                    "Protection pour les petits exploitants agricoles contre les risques climatiques, la perte de bétail ou les incidents naturels pouvant affecter les cultures.";
            case AUTOMOBILE ->
                    "Couverture de base pour les véhicules personnels ou utilitaires légers, incluant la responsabilité civile et les dommages causés à des tiers.";
            case HABITATION ->
                    "Assurance pour les habitations simples, couvrant les risques de vol, d'incendie ou de dégâts des eaux pour les petites propriétés résidentielles.";
            case VIE ->
                    "Assurance vie à faible coût offrant une épargne à long terme pour les imprévus, avec des prestations pour la famille en cas de décès prématuré de l'assuré.";
            case ACCIDENT ->
                    "Assistance financière en cas d'accident de travail ou de la vie courante, couvrant les frais médicaux, les blessures ou les incapacités temporaires.";
            case VOYAGE ->
                    "Protection pour les déplacements de courte durée, incluant les frais de rapatriement, les pertes de bagages ou les incidents durant le voyage.";
            case SANTE ->
                    "Accès à des soins de santé primaires, consultations médicales et médicaments essentiels pour les familles à revenus modestes, avec des prestations en cas d'hospitalisation.";
            default -> "Description de l'assurance";
        };
    }

    private void addSambABoxPolicy() {
        PoliceAssurance policeAssurance = policeAssuranceService.findByNumeroPolice("CODE CIMA [70|701]");

        if (policeAssurance == null) {
            // Création de la police d'assurance SAMB'A BOX
            PoliceAssurance sambABoxPolicy = new PoliceAssurance();
            sambABoxPolicy.setNumeroPolice("CODE CIMA [70|701]");
            sambABoxPolicy.setLabel("SAMB'A BOX");
            sambABoxPolicy.setDureeCouverture(12); // Durée en mois
            sambABoxPolicy.setMontantSouscription(new BigDecimal("42000.00")); // Cotisation annuelle
            sambABoxPolicy.setConditions(
                    "SAMB’A BOX a pour objet d’assurer la marchandise, les biens matériels, mobiliers, immobiliers et les espèces encaissées du Point de Vente ou Box assuré contre les risques d’incendie et de vol.\n" +
                    "Cette assurance est destinée aux Commerçants propriétaires de Points de vente, Boutiques ou de Box commerciaux.\n" +
                    "La garantie est acquise uniquement pour les Boutiques ou Box et Points de Vente ayant installé un système de vidéosurveillance fonctionnel.");

            // Charger l'image de la police depuis les ressources
            String imageUrl = loadBase64Image("1727118644_1");

            // Ajout de la police à l'assurance correspondante
            Assurance assuranceBien = assuranceService.findByType(InsuranceType.BIEN);
            sambABoxPolicy.setAssurance(assuranceBien);

            // Création des garanties pour la police SAMB'A BOX
            List<Garantie> garanties = new ArrayList<>();

            // Garantie pour vol de biens
            Garantie garantieVol = new Garantie();
            garantieVol.setNumeroGarantie(GenericUtils.GenerateNumero("GAR"));
            garantieVol.setLabel("Garantie Vol avec agression pour SAMB'A BOX");
            garantieVol.setPercentage(100.0); // 100% du plafond
            garantieVol.setPlafondAssure(new BigDecimal("500000.00")); // Plafond
            garantieVol.setTermes("Couvre les vols de biens y compris les vols avec agression.");
            garantieVol.setStatus(GarantieStatus.ACTIVEE);
            garantieVol.setDateDebut(LocalDate.now());
            garantieVol.setDateFin(LocalDate.now().plusYears(1));

            // Garantie pour dommages aux biens - incendie
            Garantie garantieIncendie = new Garantie();
            garantieIncendie.setNumeroGarantie(GenericUtils.GenerateNumero("GAR"));
            garantieIncendie.setLabel("Garantie Incendie pour SAMB'A BOX");
            garantieIncendie.setPercentage(100.0); // 100% du plafond
            garantieIncendie.setPlafondAssure(new BigDecimal("500000.00")); // Plafond
            garantieIncendie.setTermes("Couvre les dommages causés par incendie.");
            garantieIncendie.setStatus(GarantieStatus.ACTIVEE);
            garantieIncendie.setDateDebut(LocalDate.now());
            garantieIncendie.setDateFin(LocalDate.now().plusYears(1));

            // Ajouter les garanties à la liste
            garanties.add(garantieVol);
            garanties.add(garantieIncendie);

            // Associer les garanties à la police
            sambABoxPolicy.setGaranties(garanties);

            // Sauvegarder la police d'assurance et les garanties
            policeAssuranceService.savePolice(sambABoxPolicy, imageUrl);
            garantieService.saveDefault(garantieVol);
            garantieService.saveDefault(garantieIncendie);
        }
    }

    private void addSambAPreventionPolicy() {
        PoliceAssurance policeAssurance = policeAssuranceService.findByNumeroPolice("CODE CIMA [11|112]");

        if (policeAssurance == null) {
            // Création de la police d'assurance SAMB'A PRÉVOYANCE
            PoliceAssurance sambAPreventionPolicy = new PoliceAssurance();
            sambAPreventionPolicy.setNumeroPolice("CODE CIMA [11|112]");
            sambAPreventionPolicy.setLabel("SAMB'A PRÉVOYANCE");
            sambAPreventionPolicy.setDureeCouverture(12); // Durée en mois
            sambAPreventionPolicy.setMontantSouscription(new BigDecimal("30000.00")); // Cotisation annuelle
            sambAPreventionPolicy.setConditions(
                    "SAMB’A PREVOYANCE est un contrat d'assurance en cas de Décès qui permet au Souscripteur de prévoir un capital pour l'organisation et le financement de ses obsèques.\n" +
                    "Ce contrat offre deux types de prestations au choix du souscripteur : les prestations d’obsèques et le paiement d’un capital forfaitaire.\n" +
                    "Cette garantie est destinée aux Particuliers, Chefs de famille, Commerçants et Associations.");

            // Charger l'image de la police depuis les ressources
            String imageUrl = loadBase64Image("1727118613_4");

            // Ajout de la police à l'assurance correspondante
            Assurance assuranceVie = assuranceService.findByType(InsuranceType.VIE);
            sambAPreventionPolicy.setAssurance(assuranceVie);

            // Création des garanties pour la police SAMB'A PRÉVOYANCE
            List<Garantie> garanties = new ArrayList<>();

            // Garantie pour adultes
            Garantie garantieAdulte = new Garantie();
            garantieAdulte.setNumeroGarantie(GenericUtils.GenerateNumero("GAR"));
            garantieAdulte.setLabel("Garantie Décès pour Adultes pour SAMB'A PRÉVOYANCE");
            garantieAdulte.setPercentage(100.0); // 100% du plafond
            garantieAdulte.setPlafondAssure(new BigDecimal("500000.00")); // Plafond pour 2 adultes
            garantieAdulte.setTermes("Couvre le capital en cas de décès d'un adulte.");
            garantieAdulte.setStatus(GarantieStatus.ACTIVEE);
            garantieAdulte.setDateDebut(LocalDate.now());
            garantieAdulte.setDateFin(LocalDate.now().plusYears(1));

            // Garantie pour enfants
            Garantie garantieEnfant = new Garantie();
            garantieEnfant.setNumeroGarantie(GenericUtils.GenerateNumero("GAR"));
            garantieEnfant.setLabel("Garantie Décès pour Enfants pour SAMB'A PRÉVOYANCE");
            garantieEnfant.setPercentage(100.0); // 100% du plafond
            garantieEnfant.setPlafondAssure(new BigDecimal("200000.00")); // Plafond pour 5 enfants
            garantieEnfant.setTermes("Couvre le capital en cas de décès d'un enfant.");
            garantieEnfant.setStatus(GarantieStatus.ACTIVEE);
            garantieEnfant.setDateDebut(LocalDate.now());
            garantieEnfant.setDateFin(LocalDate.now().plusYears(1));

            // Ajouter les garanties à la liste
            garanties.add(garantieAdulte);
            garanties.add(garantieEnfant);

            // Associer les garanties à la police
            sambAPreventionPolicy.setGaranties(garanties);

            // Sauvegarder la police d'assurance et les garanties
            policeAssuranceService.savePolice(sambAPreventionPolicy, imageUrl);
            garantieService.saveDefault(garantieAdulte);
            garantieService.saveDefault(garantieEnfant);
        }
    }

    private void addSambAElectrikPolicy() {
        PoliceAssurance policeAssurance = policeAssuranceService.findByNumeroPolice("CODE CIMA [40|703]");

        if (policeAssurance == null) {
            // Création de la police d'assurance SAMB'A ELECTRIK
            PoliceAssurance sambAElectrikPolicy = new PoliceAssurance();
            sambAElectrikPolicy.setNumeroPolice("CODE CIMA [40|703]"); // CODE CIMA spécifique pour cette police
            sambAElectrikPolicy.setLabel("SAMB'A ELECTRIK");
            sambAElectrikPolicy.setDureeCouverture(12); // Durée en mois
            sambAElectrikPolicy.setMontantSouscription(new BigDecimal("12000.00")); // Cotisation annuelle
            sambAElectrikPolicy.setConditions(
                    "SAMB’A ELECTRIK a pour objet d’assurer les appareils électriques endommagés suite à une variation de la tension électrique, telle que définie dans le contrat.\n" +
                    "Cette garantie est destinée aux Particuliers, Commerçants, Chefs d’entreprises. Seuls les cinq(5) appareils électriques déclarés par l'Assuré font l’objet de la garantie proposée pour un capital maximum garanti pour les cinq(5) appareils de FCFA 3.000.000..");

            // Charger l'image de la police depuis les ressources
            String imageUrl = loadBase64Image("1727118581_3");

            // Ajout de la police à l'assurance correspondante
            Assurance assuranceElectrik = assuranceService.findByType(InsuranceType.HABITATION); // Assurez-vous que le type d'assurance est défini
            sambAElectrikPolicy.setAssurance(assuranceElectrik);

            // Création des garanties pour la police SAMB'A ELECTRIK
            List<Garantie> garanties = new ArrayList<>();

            // Garantie pour dommages électriques
            Garantie garantieDommages = new Garantie();
            garantieDommages.setNumeroGarantie(GenericUtils.GenerateNumero("GAR"));
            garantieDommages.setLabel("Garantie Dommages Électriques pour SAMB'A ELECTRIK");
            garantieDommages.setPercentage(100.0); // 100% du plafond
            garantieDommages.setPlafondAssure(new BigDecimal("3000000.00")); // Plafond
            garantieDommages.setTermes("Couvre les dommages causés aux appareils électriques suite à une variation de la tension électrique.");
            garantieDommages.setStatus(GarantieStatus.ACTIVEE);
            garantieDommages.setDateDebut(LocalDate.now());
            garantieDommages.setDateFin(LocalDate.now().plusYears(1));

            // Ajouter la garantie à la liste
            garanties.add(garantieDommages);

            // Associer les garanties à la police
            sambAElectrikPolicy.setGaranties(garanties);

            // Sauvegarder la police d'assurance et les garanties
            policeAssuranceService.savePolice(sambAElectrikPolicy, imageUrl);
            garantieService.saveDefault(garantieDommages);
        }
    }

    private void addSambAAgrikolPolicy() {
        PoliceAssurance policeAssurance = policeAssuranceService.findByNumeroPolice("CODE CIMA [60|601]");

        if (policeAssurance == null) {
            // Création de la police d'assurance SAMB'A AGRIKOL
            PoliceAssurance sambAAgrikolPolicy = new PoliceAssurance();
            sambAAgrikolPolicy.setNumeroPolice("CODE CIMA [60|601]");
            sambAAgrikolPolicy.setLabel("SAMB'A AGRIKOL");
            sambAAgrikolPolicy.setDureeCouverture(12); // Durée en mois
            sambAAgrikolPolicy.setMontantSouscription(new BigDecimal("42000.00")); // Cotisation par trajet
            sambAAgrikolPolicy.setConditions(
                    "SAMB’A AGRIKOL permet d’assurer pendant leur trajet les dommages subis par les produits de l’agriculture (vivres) et de l’élevage (animaux, œufs, etc.), du lieu de production à celui de distribution et de commercialisation en cas d’accident ou d’immobilisation du moyen de transport utilisé.\n" +
                    "Ce contrat couvre également le remboursement de la location du moyen de transport utilisé par l’assuré, son hospitalisation, ses frais médicaux en cas de maladie lors du trajet, ainsi que le versement d’un capital en cas de décès ou d’invalidité absolue et définitive de l’assuré.\n" +
                    "Cette assurance est destinée aux Agriculteurs, Éleveurs, Commerçants(tes) et Transporteurs de produits agricoles et d’élevage.");

            // Charger l'image de la police depuis les ressources
            String imageUrl = loadBase64Image("1727118541_2");

            // Ajout de la police à l'assurance correspondante
            Assurance assuranceAgricole = assuranceService.findByType(InsuranceType.AGRICOLE);
            sambAAgrikolPolicy.setAssurance(assuranceAgricole);

            // Création des garanties pour la police SAMB'A AGRIKOL
            List<Garantie> garanties = new ArrayList<>();

            // Garantie pour dommages sur produits agricoles et d’élevage
            Garantie garantieProduits = new Garantie();
            garantieProduits.setNumeroGarantie(GenericUtils.GenerateNumero("GAR"));
            garantieProduits.setLabel("Garantie Dommages sur Produits Agricoles et d'Élevage");
            garantieProduits.setPercentage(100.0); // 100% du plafond
            garantieProduits.setPlafondAssure(new BigDecimal("750000.00")); // Plafond
            garantieProduits.setTermes("Couvre les dommages subis par les produits agricoles et d'élevage pendant le transport.");
            garantieProduits.setStatus(GarantieStatus.ACTIVEE);
            garantieProduits.setDateDebut(LocalDate.now());
            garantieProduits.setDateFin(LocalDate.now().plusYears(1));

            // Garantie pour location du moyen de transport
            Garantie garantieLocation = new Garantie();
            garantieLocation.setNumeroGarantie(GenericUtils.GenerateNumero("GAR"));
            garantieLocation.setLabel("Garantie Location du Moyen de Transport");
            garantieLocation.setPercentage(100.0); // 100% du plafond
            garantieLocation.setPlafondAssure(new BigDecimal("750000.00")); // Plafond
            garantieLocation.setTermes("Couvre le remboursement de la location du moyen de transport utilisé par l’assuré.");
            garantieLocation.setStatus(GarantieStatus.ACTIVEE);
            garantieLocation.setDateDebut(LocalDate.now());
            garantieLocation.setDateFin(LocalDate.now().plusYears(1));

            // Ajouter les garanties à la liste
            garanties.add(garantieProduits);
            garanties.add(garantieLocation);

            // Associer les garanties à la police
            sambAAgrikolPolicy.setGaranties(garanties);

            // Sauvegarder la police d'assurance et les garanties
            policeAssuranceService.savePolice(sambAAgrikolPolicy, imageUrl);
            garantieService.saveDefault(garantieProduits);
            garantieService.saveDefault(garantieLocation);
        }
    }

    private void addSambAHospitPolicy() {
        PoliceAssurance policeAssurance = policeAssuranceService.findByNumeroPolice("CODE CIMA [50|702]");

        if (policeAssurance == null) {
            // Création de la police d'assurance SAMB'A HOSPIT
            PoliceAssurance sambAHospitPolicy = new PoliceAssurance();
            sambAHospitPolicy.setNumeroPolice("CODE CIMA [50|702]"); // Assuming CODE CIMA for hospital insurance
            sambAHospitPolicy.setLabel("SAMB'A HOSPIT");
            sambAHospitPolicy.setDureeCouverture(12); // Durée en mois
            sambAHospitPolicy.setMontantSouscription(new BigDecimal("42000.00")); // Cotisation annuelle
            sambAHospitPolicy.setConditions(
                    "SAMB'A HOSPIT a pour objet de garantir à la personne assurée le paiement d’une indemnité forfaitaire journalière d’hospitalisation prévue au contrat, " +
                    "à partir du 2ème jour d’hospitalisation, pour une durée maximale d’indemnisation de 30 jours par an, le premier jour étant à la charge de l’assuré.\n" +
                    "Cette garantie est destinée aux particuliers, Chefs de famille, Associations, Commerçants, Chefs d’entreprises.");

            // Charger l'image de la police depuis les ressources
            String imageUrl = loadBase64Image("1727118188_6");

            // Ajout de la police à l'assurance correspondante
            Assurance assuranceSante = assuranceService.findByType(InsuranceType.SANTE);
            sambAHospitPolicy.setAssurance(assuranceSante);

            // Création des garanties pour la police SAMB'A HOSPIT
            List<Garantie> garanties = new ArrayList<>();

            // Garantie d'hospitalisation
            Garantie garantieHospitalisation = new Garantie();
            garantieHospitalisation.setNumeroGarantie(GenericUtils.GenerateNumero("GAR"));
            garantieHospitalisation.setLabel("Garantie d'Hospitalisation pour SAMB'A HOSPIT");
            garantieHospitalisation.setPercentage(100.0); // 100% du plafond
            garantieHospitalisation.setPlafondAssure(new BigDecimal("6000000.00")); // Plafond global par an
            garantieHospitalisation.setTermes("Indemnité d’hospitalisation de 20 000 FCFA par jour, pour un maximum de 30 jours par an.");
            garantieHospitalisation.setStatus(GarantieStatus.ACTIVEE);
            garantieHospitalisation.setDateDebut(LocalDate.now());
            garantieHospitalisation.setDateFin(LocalDate.now().plusYears(1));

            // Garantie invalidité permanente
            Garantie garantieInvalidite = new Garantie();
            garantieInvalidite.setNumeroGarantie(GenericUtils.GenerateNumero("GAR"));
            garantieInvalidite.setLabel("Garantie Invalidité Permanente Totale pour SAMB'A HOSPIT");
            garantieInvalidite.setPercentage(100.0); // 100% du plafond
            garantieInvalidite.setPlafondAssure(new BigDecimal("2000000.00")); // Plafond pour invalidité
            garantieInvalidite.setTermes("Indemnité forfaitaire d'invalidité permanente totale de 2 000 000 FCFA.");
            garantieInvalidite.setStatus(GarantieStatus.ACTIVEE);
            garantieInvalidite.setDateDebut(LocalDate.now());
            garantieInvalidite.setDateFin(LocalDate.now().plusYears(1));

            // Ajouter les garanties à la liste
            garanties.add(garantieHospitalisation);
            garanties.add(garantieInvalidite);

            // Associer les garanties à la police
            sambAHospitPolicy.setGaranties(garanties);

            // Sauvegarder la police d'assurance et les garanties
            policeAssuranceService.savePolice(sambAHospitPolicy, imageUrl);
            garantieService.saveDefault(garantieHospitalisation);
            garantieService.saveDefault(garantieInvalidite);
        }
    }

    private void addSambASantePolicy() {
        PoliceAssurance policeAssurance = policeAssuranceService.findByNumeroPolice("CODE CIMA [50|703]");

        if (policeAssurance == null) {
            // Création de la police d'assurance SAMB'A SANTE
            PoliceAssurance sambASantePolicy = new PoliceAssurance();
            sambASantePolicy.setNumeroPolice("CODE CIMA [50|703]"); // Code CIMA correspondant
            sambASantePolicy.setLabel("SAMB'A SANTE");
            sambASantePolicy.setDureeCouverture(12); // Durée en mois
            sambASantePolicy.setMontantSouscription(new BigDecimal("42000.00")); // Cotisation annuelle
            sambASantePolicy.setConditions(
                    "SAMB’A SANTE a pour objet de garantir à la personne assurée le paiement d’une indemnité pour rembourser les frais de consultations, d’examens médicaux, d’ordonnances pharmaceutiques en cas d’accident ou de maladie.\n" +
                    "Cette garantie est destinée aux Particuliers, Associations, Chefs de famille, Chefs d’entreprises.");

            // Charger l'image de la police depuis les ressources
            String imageUrl = loadBase64Image("1727118976_5");

            // Ajout de la police à l'assurance correspondante
            Assurance assuranceSante = assuranceService.findByType(InsuranceType.SANTE); // Assurez-vous que le type existe
            sambASantePolicy.setAssurance(assuranceSante);

            // Création des garanties pour la police SAMB'A SANTE
            List<Garantie> garanties = new ArrayList<>();

            // Garantie pour consultations
            Garantie garantieConsultation = new Garantie();
            garantieConsultation.setNumeroGarantie(GenericUtils.GenerateNumero("GAR"));
            garantieConsultation.setLabel("Garantie Consultation pour SAMB'A SANTE");
            garantieConsultation.setPercentage(80.0); // 80% du plafond
            garantieConsultation.setPlafondAssure(new BigDecimal("12000.00")); // Plafond par acte
            garantieConsultation.setTermes("Couvre 80% des frais réels pour consultations, plafonnés à 12 000 FCFA par acte.");
            garantieConsultation.setStatus(GarantieStatus.ACTIVEE);
            garantieConsultation.setDateDebut(LocalDate.now());
            garantieConsultation.setDateFin(LocalDate.now().plusYears(1));

            // Garantie pour examens médicaux
            Garantie garantieExamen = new Garantie();
            garantieExamen.setNumeroGarantie(GenericUtils.GenerateNumero("GAR"));
            garantieExamen.setLabel("Garantie Examen Médical pour SAMB'A SANTE");
            garantieExamen.setPercentage(80.0); // 80% du plafond
            garantieExamen.setPlafondAssure(new BigDecimal("15000.00")); // Plafond par acte
            garantieExamen.setTermes("Couvre 80% des frais réels pour examens, plafonnés à 15 000 FCFA par acte.");
            garantieExamen.setStatus(GarantieStatus.ACTIVEE);
            garantieExamen.setDateDebut(LocalDate.now());
            garantieExamen.setDateFin(LocalDate.now().plusYears(1));

            // Garantie pour pharmacie
            Garantie garantiePharmacie = new Garantie();
            garantiePharmacie.setNumeroGarantie(GenericUtils.GenerateNumero("GAR"));
            garantiePharmacie.setLabel("Garantie Pharmacie pour SAMB'A SANTE");
            garantiePharmacie.setPercentage(80.0); // 80% du plafond
            garantiePharmacie.setPlafondAssure(new BigDecimal("50000.00")); // Plafond annuel
            garantiePharmacie.setTermes("Couvre 80% des frais réels de pharmacie, plafonnés à 50 000 FCFA par an.");
            garantiePharmacie.setStatus(GarantieStatus.ACTIVEE);
            garantiePharmacie.setDateDebut(LocalDate.now());
            garantiePharmacie.setDateFin(LocalDate.now().plusYears(1));

            // Garantie pour le total des frais médicaux
            Garantie garantieTotal = new Garantie();
            garantieTotal.setNumeroGarantie(GenericUtils.GenerateNumero("GAR"));
            garantieTotal.setLabel("Garantie Total des Frais Médicaux pour SAMB'A SANTE");
            garantieTotal.setPercentage(100.0); // 100% du plafond
            garantieTotal.setPlafondAssure(new BigDecimal("250000.00")); // Plafond global annuel
            garantieTotal.setTermes("Couvre le total des frais médicaux, plafonné à 250 000 FCFA par an.");
            garantieTotal.setStatus(GarantieStatus.ACTIVEE);
            garantieTotal.setDateDebut(LocalDate.now());
            garantieTotal.setDateFin(LocalDate.now().plusYears(1));

            // Ajouter les garanties à la liste
            garanties.add(garantieConsultation);
            garanties.add(garantieExamen);
            garanties.add(garantiePharmacie);
            garanties.add(garantieTotal);

            // Associer les garanties à la police
            sambASantePolicy.setGaranties(garanties);

            // Sauvegarder la police d'assurance et les garanties
            policeAssuranceService.savePolice(sambASantePolicy, imageUrl);
            garantieService.saveDefault(garantieConsultation);
            garantieService.saveDefault(garantieExamen);
            garantieService.saveDefault(garantiePharmacie);
            garantieService.saveDefault(garantieTotal);
        }
    }

    private void addSambANkamaPolicy() {
        PoliceAssurance policeAssurance = policeAssuranceService.findByNumeroPolice("CODE CIMA [40|702]");

        if (policeAssurance == null) {
            // Création de la police d'assurance SAMB'A NKAMA
            PoliceAssurance sambANkamaPolicy = new PoliceAssurance();
            sambANkamaPolicy.setNumeroPolice("CODE CIMA [40|702]"); // Utiliser un code CIMA approprié
            sambANkamaPolicy.setLabel("SAMB'A NKAMA");
            sambANkamaPolicy.setDureeCouverture(12); // Durée en mois
            sambANkamaPolicy.setMontantSouscription(new BigDecimal("15000.00")); // Cotisation annuelle
            sambANkamaPolicy.setConditions(
                    "SAMB’A NKAMA est une assurance multirisques habitation qui a pour objet d’assurer les biens matériels, mobiliers et immobiliers du logement, " +
                    "assurés contre les risques d’incendie, dégâts des eaux, bris de glace, dommages électriques, inondations.\n" +
                    "Cette garantie est destinée aux Particuliers, Propriétaires de maisons, Locataires, Agences et Sociétés immobilières.");

            // Charger l'image de la police depuis les ressources
            String imageUrl = loadBase64Image("1727120297_10");

            // Ajout de la police à l'assurance correspondante
            Assurance assuranceHabitation = assuranceService.findByType(InsuranceType.HABITATION); // Assurez-vous que le type d'assurance existe
            sambANkamaPolicy.setAssurance(assuranceHabitation);

            // Création des garanties pour la police SAMB'A NKAMA
            List<Garantie> garanties = new ArrayList<>();

            // Garantie pour incendie et risques annexes
            Garantie garantieIncendie = new Garantie();
            garantieIncendie.setNumeroGarantie(GenericUtils.GenerateNumero("GAR"));
            garantieIncendie.setLabel("Garantie Incendie et Risques Annexes pour SAMB'A NKAMA");
            garantieIncendie.setPercentage(100.0); // 100% du plafond
            garantieIncendie.setPlafondAssure(new BigDecimal("600000.00")); // Plafond pour incendies
            garantieIncendie.setTermes("Couvre les dommages causés par incendie et risques annexes tels que dégâts des eaux, bris de glace.");
            garantieIncendie.setStatus(GarantieStatus.ACTIVEE);
            garantieIncendie.setDateDebut(LocalDate.now());
            garantieIncendie.setDateFin(LocalDate.now().plusYears(1));

            // Garantie pour dégâts des eaux et autres dommages
            Garantie garantieDegatsEaux = new Garantie();
            garantieDegatsEaux.setNumeroGarantie(GenericUtils.GenerateNumero("GAR"));
            garantieDegatsEaux.setLabel("Garantie Dégâts des Eaux et Dommages Électriques pour SAMB'A NKAMA");
            garantieDegatsEaux.setPercentage(100.0); // 100% du plafond
            garantieDegatsEaux.setPlafondAssure(new BigDecimal("2500000.00")); // Plafond annuel
            garantieDegatsEaux.setTermes("Couvre les dégâts des eaux, inondations, dommages aux appareils électriques, et autres risques.");
            garantieDegatsEaux.setStatus(GarantieStatus.ACTIVEE);
            garantieDegatsEaux.setDateDebut(LocalDate.now());
            garantieDegatsEaux.setDateFin(LocalDate.now().plusYears(1));

            // Ajouter les garanties à la liste
            garanties.add(garantieIncendie);
            garanties.add(garantieDegatsEaux);

            // Associer les garanties à la police
            sambANkamaPolicy.setGaranties(garanties);

            // Sauvegarder la police d'assurance et les garanties
            policeAssuranceService.savePolice(sambANkamaPolicy, imageUrl);
            garantieService.saveDefault(garantieIncendie);
            garantieService.saveDefault(garantieDegatsEaux);
        }
    }

    private void addSambAParrainePolicy() {
        PoliceAssurance policeAssurance = policeAssuranceService.findByNumeroPolice("CODE CIMA [11|113]");

        if (policeAssurance == null) {
            // Création de la police d'assurance SAMB'A PARRAINE
            PoliceAssurance sambAParrainePolicy = new PoliceAssurance();
            sambAParrainePolicy.setNumeroPolice("CODE CIMA [11|113]"); // Hypothèse pour SAMB'A PARRAINE
            sambAParrainePolicy.setLabel("SAMB'A PARRAINE");
            sambAParrainePolicy.setDureeCouverture(12); // Durée en mois
            sambAParrainePolicy.setMontantSouscription(new BigDecimal("10000.00")); // Cotisation annuelle

            // Compléter les conditions avec la description fournie
            sambAParrainePolicy.setConditions(
                    "SAMB’A PARRAINE a pour objet de garantir en cas de décès accidentel de l’Assuré, titulaire d’un ou de plusieurs comptes domiciliés dans un établissement de crédit, " +
                    "le versement d’une indemnité aux bénéficiaires, dans les limites prévues au contrat.\n" +
                    "Cette garantie est destinée aux particuliers, Chefs de famille, Associations, Commerçants, Chefs d’entreprises.");

            // Charger l'image de la police depuis les ressources
            String imageUrl = loadBase64Image("1727120510_7");

            // Ajout de la police à l'assurance correspondante (Vie)
            Assurance assuranceVie = assuranceService.findByType(InsuranceType.VIE); // Déterminé comme assurance de type "VIE"
            sambAParrainePolicy.setAssurance(assuranceVie);

            // Création des garanties pour la police SAMB'A PARRAINE
            List<Garantie> garanties = new ArrayList<>();

            // Garantie pour décès accidentel
            Garantie garantieDecesAccidentel = new Garantie();
            garantieDecesAccidentel.setNumeroGarantie(GenericUtils.GenerateNumero("GAR"));
            garantieDecesAccidentel.setLabel("Garantie Décès Accidentel pour SAMB'A PARRAINE");
            garantieDecesAccidentel.setPercentage(50.0); // 50% du solde créditeur
            garantieDecesAccidentel.setPlafondAssure(new BigDecimal("1000000.00")); // Plafond maximum 1 000 000 FCFA
            garantieDecesAccidentel.setTermes("Couvre le décès accidentel avec une indemnité de 50% du solde créditeur du compte, jusqu'à 1 000 000 FCFA.");
            garantieDecesAccidentel.setStatus(GarantieStatus.ACTIVEE);
            garantieDecesAccidentel.setDateDebut(LocalDate.now());
            garantieDecesAccidentel.setDateFin(LocalDate.now().plusYears(1));

            // Ajouter la garantie à la liste
            garanties.add(garantieDecesAccidentel);

            // Associer les garanties à la police
            sambAParrainePolicy.setGaranties(garanties);

            // Sauvegarder la police d'assurance et la garantie
            policeAssuranceService.savePolice(sambAParrainePolicy, imageUrl);
            garantieService.saveDefault(garantieDecesAccidentel);
        }
    }

    private void addSambaProtegePolicy() {
        PoliceAssurance policeAssurance = policeAssuranceService.findByNumeroPolice("CODE CIMA [11|114]");

        if (policeAssurance == null) {
            // Création de la police d'assurance SAMB'A PROTEGE
            PoliceAssurance sambaProtegePolicy = new PoliceAssurance();
            sambaProtegePolicy.setNumeroPolice("CODE CIMA [11|114]"); // Numéro à ajuster si nécessaire
            sambaProtegePolicy.setLabel("SAMB'A PROTEGE");
            sambaProtegePolicy.setDureeCouverture(12); // Durée en mois
            sambaProtegePolicy.setMontantSouscription(new BigDecimal("20000.00")); // Cotisation annuelle
            sambaProtegePolicy.setConditions(
                    "SAMB’A PROTEGE a pour objet de garantir en cas de décès ou d’invalidité absolue et définitive de l’Assuré, titulaire d’un ou de plusieurs comptes domiciliés dans un établissement de crédit, le versement d’une indemnité aux bénéficiaires, dont le montant est prévu au contrat. " +
                    "Cette garantie est destinée aux Particuliers et Chefs d’entreprises, titulaires de comptes dans les établissements de crédit.\n" +
                    "Capital garanti en cas de décès ou d’invalidité absolue et définitive de l’assuré : 100 % du solde créditeur du compte de l’Assuré, limité à 1 000 000 FCFA par compte, pour un maximum de 2 comptes.");

            // Charger l'image de la police depuis les ressources (mettre à jour l'image correspondante)
            String imageUrl = loadBase64Image("1727120766_11");

            // Ajout de la police à l'assurance correspondante
            Assurance assuranceVie = assuranceService.findByType(InsuranceType.VIE);
            sambaProtegePolicy.setAssurance(assuranceVie);

            // Création des garanties pour la police SAMB'A PROTEGE
            List<Garantie> garanties = new ArrayList<>();

            // Garantie décès
            Garantie garantieDeces = new Garantie();
            garantieDeces.setNumeroGarantie(GenericUtils.GenerateNumero("GAR"));
            garantieDeces.setLabel("Garantie Décès pour SAMB'A PROTEGE");
            garantieDeces.setPercentage(100.0); // 100% du solde du compte, limité à 1 000 000 FCFA
            garantieDeces.setPlafondAssure(new BigDecimal("1000000.00")); // Plafond pour chaque compte
            garantieDeces.setTermes("Couvre le décès de l'assuré, avec un plafond de 1 000 000 FCFA par compte, jusqu'à 2 comptes maximum.");
            garantieDeces.setStatus(GarantieStatus.ACTIVEE);
            garantieDeces.setDateDebut(LocalDate.now());
            garantieDeces.setDateFin(LocalDate.now().plusYears(1));

            // Garantie invalidité
            Garantie garantieInvalidite = new Garantie();
            garantieInvalidite.setNumeroGarantie(GenericUtils.GenerateNumero("GAR"));
            garantieInvalidite.setLabel("Garantie Invalidité Absolue et Définitive pour SAMB'A PROTEGE");
            garantieInvalidite.setPercentage(100.0); // 100% du solde du compte, limité à 1 000 000 FCFA
            garantieInvalidite.setPlafondAssure(new BigDecimal("1000000.00")); // Plafond pour chaque compte
            garantieInvalidite.setTermes("Couvre l'invalidité absolue et définitive de l'assuré, avec un plafond de 1 000 000 FCFA par compte, jusqu'à 2 comptes maximum.");
            garantieInvalidite.setStatus(GarantieStatus.ACTIVEE);
            garantieInvalidite.setDateDebut(LocalDate.now());
            garantieInvalidite.setDateFin(LocalDate.now().plusYears(1));

            // Ajouter les garanties à la liste
            garanties.add(garantieDeces);
            garanties.add(garantieInvalidite);

            // Associer les garanties à la police
            sambaProtegePolicy.setGaranties(garanties);

            // Sauvegarder la police d'assurance et les garanties
            policeAssuranceService.savePolice(sambaProtegePolicy, imageUrl);
            garantieService.saveDefault(garantieDeces);
            garantieService.saveDefault(garantieInvalidite);
        }
    }

    private void addSambADiasporaPolicy() {
        PoliceAssurance policeAssurance = policeAssuranceService.findByNumeroPolice("CODE CIMA [11|115]");

        if (policeAssurance == null) {
            // Création de la police d'assurance SAMB'A DIASPORA
            PoliceAssurance sambADiasporaPolicy = new PoliceAssurance();
            sambADiasporaPolicy.setNumeroPolice("CODE CIMA [11|115]");
            sambADiasporaPolicy.setLabel("SAMB'A DIASPORA");
            sambADiasporaPolicy.setDureeCouverture(12); // Durée en mois
            sambADiasporaPolicy.setMontantSouscription(new BigDecimal("42000.00")); // Cotisation annuelle
            sambADiasporaPolicy.setConditions(
                    "SAMB’A DIASPORA garantit l’organisation des funérailles du souscripteur soit dans son pays de résidence habituelle, soit dans son pays d’origine. " +
                    "Ce contrat prend en charge :\n" +
                    "- Le transport du corps depuis le pays de résidence jusqu’au pays d’origine du défunt\n" +
                    "- Les frais annexes au transport\n" +
                    "- Les frais funéraires et le cercueil.\n" +
                    "\n" +
                    "Cette garantie est destinée aux Particuliers, Chefs de famille, Associations, Communautés étrangères, Chefs d’entreprises.");

            // Charger l'image de la police depuis les ressources
            String imageUrl = loadBase64Image("1727120897_9");

            // Ajout de la police à l'assurance correspondante
            Assurance assuranceBien = assuranceService.findByType(InsuranceType.VIE);
            sambADiasporaPolicy.setAssurance(assuranceBien);

            // Création des garanties pour la police SAMB'A DIASPORA
            List<Garantie> garanties = new ArrayList<>();

            // Garantie pour les funérailles
            Garantie garantieFunerailles = new Garantie();
            garantieFunerailles.setNumeroGarantie(GenericUtils.GenerateNumero("GAR"));
            garantieFunerailles.setLabel("Garantie Funérailles pour SAMB'A DIASPORA");
            garantieFunerailles.setPercentage(100.0); // 100% du plafond
            garantieFunerailles.setPlafondAssure(new BigDecimal("2500000.00")); // Plafond
            garantieFunerailles.setTermes("Couvre les frais de transport du corps, les frais funéraires et le cercueil.");
            garantieFunerailles.setStatus(GarantieStatus.ACTIVEE);
            garantieFunerailles.setDateDebut(LocalDate.now());
            garantieFunerailles.setDateFin(LocalDate.now().plusYears(1));

            // Ajouter la garantie à la liste
            garanties.add(garantieFunerailles);

            // Associer les garanties à la police
            sambADiasporaPolicy.setGaranties(garanties);

            // Sauvegarder la police d'assurance et les garanties
            policeAssuranceService.savePolice(sambADiasporaPolicy, imageUrl);
            garantieService.saveDefault(garantieFunerailles);
        }
    }

    // Fonction pour charger l'image d'une police en fonction du type d'assurance en Base64
    private String loadBase64Image(String fileName) {
        String imagePath = "/polices-images/" + fileName + ".png";
        try (InputStream inputStream = getClass().getResourceAsStream(imagePath)) {
            if (inputStream == null) {
                throw new RuntimeException("Image not found: " + imagePath);
            }
            byte[] imageBytes = inputStream.readAllBytes();  // Java 9+
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException("Error loading image: " + e.getMessage(), e);
        }
    }
}