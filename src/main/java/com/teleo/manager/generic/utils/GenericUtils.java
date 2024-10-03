package com.teleo.manager.generic.utils;

import jakarta.annotation.PostConstruct;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.servlet.http.HttpServletRequest;

import com.teleo.manager.authentification.entities.DefaultRole;
import com.teleo.manager.generic.exceptions.RessourceNotFoundException;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;


@Component
public class GenericUtils {

    @Value("${server.address:localhost}")
    private String injectedServerAddress;

    @Value("${server.port:9000}")
    private int injectedServerPort;

    @Value("${spring.profiles.active:dev}")
    private String injectedActiveProfile;

    private static String serverAddress;
    private static int serverPort;
    private static String activeProfile;

    @PostConstruct
    public void init() {
        serverAddress = injectedServerAddress;
        serverPort = injectedServerPort;
        activeProfile = injectedActiveProfile;
    }

    public static String generatedPassWord() {
        String AlphaNumericStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";

        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int index = (int) (AlphaNumericStr.length() * Math.random());
            sb.append(AlphaNumericStr.charAt(index));
        }
        return sb.toString();
    }

    public static String generateTokenNumber() {
        String AlphaNumericStr = "0123456789";

        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            int index = (int) (AlphaNumericStr.length() * Math.random());
            sb.append(AlphaNumericStr.charAt(index));
        }
        return "T-" + sb.toString();
    }

    public static String GenerateNumero(String prefixe) {
        Random random = new Random();
        String number = String.format("%04d", random.nextInt(100000));
        return prefixe+"-"+number;
    }

    public static String getServerAbsoluteUrl() {
        String scheme = "http"; // Valeur par défaut pour développement
        if ("prod".equalsIgnoreCase(activeProfile)) {
            scheme = "https";
        }
        return scheme + "://" + serverAddress + ":" + serverPort;
    }

    public static int calculAge(Date dateNaissance) {
        Calendar current = Calendar.getInstance();
        Calendar birthday = Calendar.getInstance();
        birthday.setTime(dateNaissance);
        int yearDiff = current.get((Calendar.YEAR) - birthday.get(Calendar.YEAR));
        if (birthday.after(current)) {
            yearDiff = yearDiff - 1;
        }
        return yearDiff;
    }

    public static void validatePageNumberAndSize(final Integer page, final Integer size) {
        if (page < 0) {
            throw new RessourceNotFoundException("Le numéro de page ne peut pas être inférieur à zéro.");
        }

        if (size > Integer.getInteger(AppConstants.DEFAULT_PAGE_SIZE)) {
            throw new RessourceNotFoundException("La taille de la page ne doit pas être supérieure à " + AppConstants.DEFAULT_PAGE_SIZE);
        }
    }

    public static Date calculateExpiryDate(final int expiryTimeInMinutes) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    private static String getFileExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        String[] fileNameParts = fileName.split("\\.");

        return fileNameParts[fileNameParts.length - 1];
    }

    public static String storeFile(MultipartFile file, Path fileStorageLocation) {
        // Normalize file name
        String fileName = new Date().getTime() + "-file." + getFileExtension(file.getOriginalFilename());

        try {
            // Check if the filename contains invalid characters
            if (fileName.contains("..")) {
                throw new RuntimeException(
                        "Désolé! Le nom du fichier contient une séquence de chemin non valide" + fileName);
            }

            Path targetLocation = fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Impossible de stocker le fichier " + fileName + ". Veuillez réessayer!", ex);
        }
    }

    public static String verifieFormatLangue(String langKey) {
        return switch (langKey) {
            case "En" -> "En";
            case "Esp" -> "Esp";
            default -> "Fr"; // default language
        };
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public static DefaultRole[] getDefaultAdminRoles() {
        return new DefaultRole[]{
            // Administrateur
            new DefaultRole("ASSURANCE_MODULE", "Gestion des assurances", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("POLICE_ASSURANCE_MODULE", "Gestion des polices d\'assurance", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("GARANTIE_MODULE", "Gestion des garanties", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("ASSURE_MODULE", "Gestion des assurés", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("CONTRAT_MODULE", "Gestion des contrats d'assurance", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("SOUSCRIPTION_MODULE", "Gestion des souscriptions", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("SINISTRE_MODULE", "Gestion des sinistres", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("RECLAMATION_MODULE", "Gestion des réclamations", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("DOCUMENT_MODULE", "Gestion des documents", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("NOTIFICATION_MODULE", "Gestion des notifications", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("DOSSIER_MEDICAUX_MODULE", "Gestion des dossiers médicaux", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("PAIEMENT_MODULE", "Gestion des paiements", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("RECU_PAIEMENT_MODULE", "Gestion des caisses", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("REPORTING_MODULE", "Gestion des rapports", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("ACCOUNT_MODULE", "Gestion des utilisateurs", new Long[]{1L, 2L, 3L, 4L, 5L, 6L, 7L}),
            new DefaultRole("BRANCHE_MODULE", "Gestion des branches", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("COMPANY_MODULE", "Gestion de la société", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("FOURNISSEUR_MODULE", "Gestion des partenaires", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("PRESTATION_MODULE", "Gestion des prestations", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("FINANCEUR_MODULE", "Gestion des financeurs", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("DASHBOARD_MODULE", "Dashboard", new Long[]{1L, 2L, 3L, 4L, 5L})
        };
    }

    public static DefaultRole[] getDefaultAgentRoles() {
        return new DefaultRole[]{
            // Agent
            new DefaultRole("DASHBOARD_MODULE", "Dashboard", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("ASSURANCE_MODULE", "Gestion des assurances", new Long[]{4L, 5L}),
            new DefaultRole("POLICE_ASSURANCE_MODULE", "Gestion des polices d\'assurance", new Long[]{4L, 5L}),
            new DefaultRole("GARANTIE_MODULE", "Gestion des garanties", new Long[]{4L, 5L}),
            new DefaultRole("ASSURE_MODULE", "Gestion des assurés", new Long[]{1L, 2L, 4L, 5L}),
            new DefaultRole("CONTRAT_MODULE", "Gestion des contrats d'assurance", new Long[]{4L, 5L}),
            new DefaultRole("SOUSCRIPTION_MODULE", "Gestion des souscriptions", new Long[]{1L, 2L, 4L, 5L}),
            new DefaultRole("SINISTRE_MODULE", "Gestion des sinistres", new Long[]{1L, 2L, 4L, 5L}),
            new DefaultRole("RECLAMATION_MODULE", "Gestion des réclamations", new Long[]{1L, 2L, 4L, 5L}),
            new DefaultRole("DOSSIER_MEDICAUX_MODULE", "Gestion des dossiers médicaux", new Long[]{1L, 2L, 4L, 5L}),
            new DefaultRole("PAIEMENT_MODULE", "Gestion des paiements", new Long[]{1L, 2L, 4L, 5L}),
            new DefaultRole("RECU_PAIEMENT_MODULE", "Gestion des caisses", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("ACCOUNT_MODULE", "Gestion des utilisateurs", new Long[]{2L, 4L}),
            new DefaultRole("DOCUMENT_MODULE", "Gestion des documents", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("COMPANY_MODULE", "Gestion de la société", new Long[]{4L}),
            new DefaultRole("NOTIFICATION_MODULE", "Gestion des notifications", new Long[]{1L, 4L, 5L})
        };
    }

    public static DefaultRole[] getDefaultClientRoles() {
        return new DefaultRole[]{
            // Client (Assuré)
            new DefaultRole("ASSURANCE_MODULE", "Gestion des assurances", new Long[]{4L, 5L}),
            new DefaultRole("ASSURE_MODULE", "Gestion des assurés", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("CONTRAT_MODULE", "Gestion des contrats d'assurance", new Long[]{4L, 5L}),
            new DefaultRole("POLICE_ASSURANCE_MODULE", "Gestion des polices d'assurance", new Long[]{4L, 5L}),
            new DefaultRole("GARANTIE_MODULE", "Gestion des garanties", new Long[]{4L, 5L}),
            new DefaultRole("SOUSCRIPTION_MODULE", "Gestion des souscriptions", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("SINISTRE_MODULE", "Gestion des sinistres", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("RECLAMATION_MODULE", "Gestion des réclamations", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("DOCUMENT_MODULE", "Gestion des documents", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("NOTIFICATION_MODULE", "Gestion des notifications", new Long[]{1L, 4L, 5L}),
            new DefaultRole("ACCOUNT_MODULE", "Gestion des utilisateurs", new Long[]{2L, 4L}),
            new DefaultRole("DOSSIER_MEDICAUX_MODULE", "Gestion des dossiers médicaux", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("COMPANY_MODULE", "Gestion de la société", new Long[]{4L}),
            new DefaultRole("PAIEMENT_MODULE", "Gestion des paiements", new Long[]{1L, 2L, 3L, 4L, 5L})
        };
    }

    public static DefaultRole[] getDefaultFournisseurRoles() {
        return new DefaultRole[]{
            // Fournisseur de Services
            new DefaultRole("DASHBOARD_MODULE", "Dashboard", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("SINISTRE_MODULE", "Gestion des sinistres", new Long[]{1L, 4L, 5L}),
            new DefaultRole("PRESTATION_MODULE", "Gestion des prestations", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("FINANCEUR_MODULE", "Gestion des financeurs", new Long[]{1L, 4L, 5L}),
            new DefaultRole("DOSSIER_MEDICAUX_MODULE", "Gestion des dossiers médicaux", new Long[]{1L, 2L, 4L, 5L}),
            new DefaultRole("DOCUMENT_MODULE", "Gestion des documents", new Long[]{1L, 2L, 3L, 4L, 5L}),
            new DefaultRole("ACCOUNT_MODULE", "Gestion des utilisateurs", new Long[]{2L, 4L}),
            new DefaultRole("COMPANY_MODULE", "Gestion de la société", new Long[]{4L}),
            new DefaultRole("NOTIFICATION_MODULE", "Gestion des notifications", new Long[]{1L, 4L, 5L})
        };
    }
}
