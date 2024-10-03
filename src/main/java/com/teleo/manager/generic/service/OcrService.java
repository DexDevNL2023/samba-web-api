package com.teleo.manager.generic.service;

import com.teleo.manager.assurance.dto.request.AssureRequest;

import com.teleo.manager.assurance.enums.Gender;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OcrService {

    private final Tesseract tesseract;

    @Value("${tesseract.datapath}")
    private String tessdataPath;

    @Value("${tesseract.language}")
    private String language;

    public OcrService() {
        this.tesseract = new Tesseract();
        // Spécifiez le chemin vers les données linguistiques
        // Utiliser le chemin absolu du dossier tessdata dans les ressources
        this.tesseract.setDatapath(tessdataPath);
        this.tesseract.setLanguage(language);
    }

    public AssureRequest extractInfoFromCni(String base64Image) throws IOException, TesseractException {
        BufferedImage image = decodeBase64ToImage(base64Image);

        // Extraire le texte de l'image
        String ocrResult = tesseract.doOCR(image);

        // Parser les informations pour créer un AssureRequest
        return parseOcrResult(ocrResult);
    }

    private BufferedImage decodeBase64ToImage(String base64Image) throws IOException {
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        try (ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes)) {
            return ImageIO.read(bis);
        }
    }

    private AssureRequest parseOcrResult(String ocrResult) {
        AssureRequest assureRequest = new AssureRequest();

        // Utiliser des expressions régulières pour extraire les informations spécifiques
        assureRequest.setNumNiu(extractField(ocrResult, "Numéro NIU"));
        assureRequest.setLastName(extractField(ocrResult, "Nom de famille"));
        assureRequest.setFirstName(extractField(ocrResult, "Prénom"));

        // Conversion de la date de naissance en LocalDate
        String dateNaissanceStr = extractField(ocrResult, "Date de naissance");
        assureRequest.setDateNaissance(parseDate(dateNaissanceStr));

        assureRequest.setNumCni(extractField(ocrResult, "Numéro de CNI"));

        // Conversion du genre (Homme/Femme) en enum Gender
        String sexeStr = extractField(ocrResult, "Sexe");
        assureRequest.setSexe(parseGender(sexeStr));

        assureRequest.setTelephone(extractField(ocrResult, "Numéro de téléphone"));
        assureRequest.setAdresse(extractField(ocrResult, "Adresse"));
        assureRequest.setSignature(extractField(ocrResult, "Signature"));

        return assureRequest;
    }

    private String extractField(String ocrResult, String fieldName) {
        Pattern pattern = Pattern.compile(fieldName + "\\s*:\\s*(.+)");
        Matcher matcher = pattern.matcher(ocrResult);
        return matcher.find() ? matcher.group(1).trim() : null;
    }

    // Méthode utilitaire pour convertir une chaîne en LocalDate
    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            // Gérer l'erreur de format de date (ou choisir une stratégie différente)
            return null;
        }
    }

    // Méthode utilitaire pour convertir une chaîne en Genre
    private Gender parseGender(String genderStr) {
        if (genderStr == null) {
            return null;
        }
        if (genderStr.equalsIgnoreCase("Homme")) {
            return Gender.MALE;
        } else if (genderStr.equalsIgnoreCase("Femme")) {
            return Gender.FEMALE;
        } else {
            return null;  // Ou lever une exception pour les valeurs non valides
        }
    }
}
